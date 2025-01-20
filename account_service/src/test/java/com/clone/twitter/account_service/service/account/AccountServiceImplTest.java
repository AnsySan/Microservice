package com.clone.twitter.account_service.service.account;

import com.clone.twitter.account_service.dto.account.AccountCreateDto;
import com.clone.twitter.account_service.dto.account.AccountDto;
import com.clone.twitter.account_service.dto.account.AccountDtoToUpdate;
import com.clone.twitter.account_service.exception.NotFoundException;
import com.clone.twitter.account_service.mapper.AccountMapper;
import com.clone.twitter.account_service.model.Account;
import com.clone.twitter.account_service.model.Owner;
import com.clone.twitter.account_service.model.enums.AccountStatus;
import com.clone.twitter.account_service.repository.AccountRepository;
import com.clone.twitter.account_service.repository.OwnerRepository;
import com.clone.twitter.account_service.service.account_number.AccountNumberService;
import com.clone.twitter.account_service.validator.AccountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountValidator accountValidator;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private AccountNumberService accountNumberService;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountCreateDto accountCreateDto;
    private AccountDto accountDto;
    private AccountDtoToUpdate accountDtoToUpdate;
    private Account account;
    private Owner owner;

    @BeforeEach
    void setUp() {
        accountCreateDto = new AccountCreateDto();
        accountDtoToUpdate = new AccountDtoToUpdate();
        account = new Account();
        owner = new Owner();
        account.setOwner(owner);
        account.setVersion(1L);
        accountDto = new AccountDto();
    }

    @Test
    void testOpen() {
        when(accountMapper.toEntity(accountCreateDto)).thenReturn(account);
        doNothing().when(accountValidator).validateCreate(account);
        when(ownerRepository.findByAccountIdAndOwnerType(anyLong(), any())).thenReturn(Optional.empty());
        when(ownerRepository.save(any(Owner.class))).thenReturn(owner);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountDto);

        AccountDto result = accountService.open(accountCreateDto);

        assertNotNull(result);
        verify(accountRepository).save(account);
        verify(accountMapper).toDto(account);
        verify(accountValidator).validateCreate(account);
        verify(accountNumberService).getUniqueAccountNumber(any(), eq(accountCreateDto.getAccountType()));
    }

    @Test
    void testUpdate() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        doNothing().when(accountMapper).update(accountDtoToUpdate, account);
        when(accountMapper.toDto(any())).thenReturn(accountDto);

        AccountDto result = accountService.update(1L, accountDtoToUpdate);

        assertNotNull(result);
        verify(accountRepository).save(account);
        verify(accountMapper).update(accountDtoToUpdate, account);
        verify(accountMapper).toDto(account);
    }

    @Test
    void testGet() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountMapper.toDto(any())).thenReturn(accountDto);

        AccountDto result = accountService.get(1L);

        assertNotNull(result);
        verify(accountMapper).toDto(account);
    }

    @Test
    void testBlock() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        doNothing().when(accountValidator).validateBlock(account);

        accountService.block(1L);

        assertEquals(AccountStatus.FROZEN, account.getAccountStatus());
        verify(accountRepository).save(account);
        verify(accountValidator).validateBlock(account);
    }

    @Test
    void testUnblock() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        doNothing().when(accountValidator).validateUnblock(account);

        accountService.unBlock(1L);

        assertEquals(AccountStatus.ACTIVE, account.getAccountStatus());
        verify(accountRepository).save(account);
        verify(accountValidator).validateUnblock(account);
    }

    @Test
    void testClose() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        doNothing().when(accountValidator).validateClose(account);

        accountService.close(1L);

        assertEquals(AccountStatus.CLOSED, account.getAccountStatus());
        assertNotNull(account.getClosedAt());
        verify(accountRepository).save(account);
        verify(accountValidator).validateClose(account);
    }

    @Test
    void testFindAccountById() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        Account result = accountService.findAccountById(1L);

        assertNotNull(result);
        assertEquals(account, result);
    }

    @Test
    void testFindAccountById_NotFound() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.findAccountById(1L));
    }
}

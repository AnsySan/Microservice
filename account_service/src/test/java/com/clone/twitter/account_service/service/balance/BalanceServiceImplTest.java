package com.clone.twitter.account_service.service.balance;

import com.clone.twitter.account_service.dto.balance.BalanceDto;
import com.clone.twitter.account_service.dto.balance.BalanceUpdateDto;
import com.clone.twitter.account_service.exception.NotFoundException;
import com.clone.twitter.account_service.mapper.BalanceMapper;
import com.clone.twitter.account_service.model.Account;
import com.clone.twitter.account_service.model.Balance;
import com.clone.twitter.account_service.repository.AccountRepository;
import com.clone.twitter.account_service.repository.BalanceRepository;
import com.clone.twitter.account_service.service.balance_audit.BalanceAuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceServiceImplTest {

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BalanceMapper balanceMapper;

    @Mock
    private BalanceAuditService balanceAuditService;

    @InjectMocks
    private BalanceServiceImpl balanceService;

    private Account account;
    private Balance balance;
    private BalanceDto balanceDto;
    private BalanceUpdateDto balanceUpdateDto;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        balance = new Balance();
        balance.setAccount(account);
        balance.setActualBalance(BigDecimal.ZERO);
        balance.setAuthorizationBalance(BigDecimal.ZERO);

        balanceDto = new BalanceDto();

        balanceUpdateDto = BalanceUpdateDto.builder()
                .id(1L)
                .accountId(2L)
                .actualBalance(3L)
                .authorizedBalance(4L)
                .paymentNumber(5L)
                .build();
    }

    @Test
    void testCreateBalance() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(balanceRepository.save(any(Balance.class))).thenReturn(balance);

        balanceService.createBalance(1L);

        verify(balanceRepository).save(any(Balance.class));
        verifyNoMoreInteractions(balanceRepository);
    }

    @Test
    void testUpdateBalance() {
        when(balanceRepository.findById(anyLong())).thenReturn(Optional.of(balance));
        when(balanceMapper.toEntity(any(BalanceUpdateDto.class))).thenReturn(balance);
        when(balanceRepository.save(any(Balance.class))).thenReturn(balance);
        when(balanceMapper.toDto(any(Balance.class))).thenReturn(balanceDto);

        BalanceDto result = balanceService.updateBalance(balanceUpdateDto);

        assertNotNull(result);
        verify(balanceMapper).toEntity(balanceUpdateDto);
        verify(balanceAuditService).createNewAudit(balanceUpdateDto);
        verify(balanceRepository).save(balance);
        verify(balanceMapper).toDto(balance);
    }

    @Test
    void testGetBalance() {
        when(balanceRepository.findById(anyLong())).thenReturn(Optional.of(balance));
        when(balanceMapper.toDto(any(Balance.class))).thenReturn(balanceDto);

        BalanceDto result = balanceService.getBalance(1L);

        assertNotNull(result);
        verify(balanceRepository).findById(1L);
        verify(balanceMapper).toDto(balance);
    }

    @Test
    void testGetBalance_NotFound() {
        when(balanceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> balanceService.getBalance(1L));
    }
}

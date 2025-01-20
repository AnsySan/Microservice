package com.clone.twitter.account_service.validator;

import com.clone.twitter.account_service.client.ProjectServiceClient;
import com.clone.twitter.account_service.client.UserServiceClient;
import com.clone.twitter.account_service.config.context.user.UserContext;
import com.clone.twitter.account_service.exception.DataValidationException;
import com.clone.twitter.account_service.exception.NotFoundException;
import com.clone.twitter.account_service.model.Account;
import com.clone.twitter.account_service.model.Owner;
import com.clone.twitter.account_service.model.enums.AccountStatus;
import com.clone.twitter.account_service.model.enums.OwnerType;
import com.clone.twitter.account_service.repository.AccountRepository;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountValidatorTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ProjectServiceClient projectServiceClient;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private AccountValidator accountValidator;

    private Account account;
    private Owner owner;

    @BeforeEach
    void setUp() {
        owner = new Owner();
        owner.setOwnerType(OwnerType.USER);
        owner.setAccountId(1L);

        account = new Account();
        account.setId(1L);
        account.setOwner(owner);
        account.setNumber("12345");
        account.setAccountStatus(AccountStatus.ACTIVE);
    }

    @Test
    void testValidateCreate_Valid() {
        when(accountRepository.checkNumberIsUnique(anyString())).thenReturn(true);
        when(userServiceClient.getUser(anyLong())).thenReturn(null);
        when(userContext.getUserId()).thenReturn(1L);

        assertDoesNotThrow(() -> accountValidator.validateCreate(account));
    }

    @Test
    void testValidateCreate_NumberNotUnique() {
        when(accountRepository.checkNumberIsUnique(anyString())).thenReturn(false);

        DataValidationException exception = assertThrows(DataValidationException.class,
                () -> accountValidator.validateCreate(account));

        assertEquals("Number is not unique 12345", exception.getMessage());
    }

    @Test
    void testValidateCreate_UserNotExist() {
        when(accountRepository.checkNumberIsUnique(anyString())).thenReturn(true);
        when(userServiceClient.getUser(anyLong())).thenThrow(FeignException.NotFound.class);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> accountValidator.validateCreate(account));

        assertEquals("USER with id 1 not found", exception.getMessage());
    }

    @Test
    void testValidateCreate_UserContextDifferent() {
        when(accountRepository.checkNumberIsUnique(anyString())).thenReturn(true);
        when(userServiceClient.getUser(anyLong())).thenReturn(null);
        when(userContext.getUserId()).thenReturn(2L);

        DataValidationException exception = assertThrows(DataValidationException.class,
                () -> accountValidator.validateCreate(account));

        assertEquals("UserContext and accountId is different, only author could create new account", exception.getMessage());
    }

    @Test
    void testValidateCreate_ProjectOwnerNotMember() {
        owner.setOwnerType(OwnerType.PROJECT);
        when(accountRepository.checkNumberIsUnique(anyString())).thenReturn(true);
        when(projectServiceClient.getProject(anyLong())).thenReturn(null);
        when(projectServiceClient.checkProjectOwner(anyLong(), anyLong())).thenReturn(false);
        when(userContext.getUserId()).thenReturn(1L);

        DataValidationException exception = assertThrows(DataValidationException.class,
                () -> accountValidator.validateCreate(account));

        assertEquals("Only project owner could create account for project 1", exception.getMessage());
    }

    @Test
    void testValidateBlock_Valid() {
        assertDoesNotThrow(() -> accountValidator.validateBlock(account));
    }

    @Test
    void testValidateBlock_InvalidStatus() {
        account.setAccountStatus(AccountStatus.FROZEN);

        DataValidationException exception = assertThrows(DataValidationException.class,
                () -> accountValidator.validateBlock(account));

        assertEquals("Status should be ACTIVE, but account: 1 is FROZEN", exception.getMessage());
    }

    @Test
    void testValidateUnblock_Valid() {
        account.setAccountStatus(AccountStatus.FROZEN);
        assertDoesNotThrow(() -> accountValidator.validateUnblock(account));
    }

    @Test
    void testValidateUnblock_InvalidStatus() {
        account.setAccountStatus(AccountStatus.ACTIVE);

        DataValidationException exception = assertThrows(DataValidationException.class,
                () -> accountValidator.validateUnblock(account));

        assertEquals("Status should be FROZEN, but account: 1 is ACTIVE", exception.getMessage());
    }

    @Test
    void testValidateClose_Valid() {
        assertDoesNotThrow(() -> accountValidator.validateClose(account));
    }

    @Test
    void testValidateClose_AlreadyClosed() {
        account.setAccountStatus(AccountStatus.CLOSED);

        DataValidationException exception = assertThrows(DataValidationException.class,
                () -> accountValidator.validateClose(account));

        assertEquals("Account: 1 already closed", exception.getMessage());
    }
}

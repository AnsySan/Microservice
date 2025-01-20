package com.clone.twitter.account_service.service.account_number.generation;

import com.clone.twitter.account_service.model.enums.AccountType;
import com.clone.twitter.account_service.repository.FreeAccountNumbersRepository;
import com.clone.twitter.account_service.service.account_number.AccountNumberService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountNumberGenerationServiceImplTest {

    @Mock
    private FreeAccountNumbersRepository freeAccountNumbersRepository;
    @Mock
    private AccountNumberService accountNumberService;

    @InjectMocks
    private AccountNumberGenerationServiceImpl accountNumberGenerationService;

    private final AccountType accountType = AccountType.INDIVIDUAL;

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 100000 })
    void generateFreeAccountNumbersGood(int amount) {

        when(freeAccountNumbersRepository.countByType(accountType)).thenReturn(0L);

        accountNumberGenerationService.generateFreeAccountNumbers(amount, accountType);

        InOrder inOrder = inOrder(freeAccountNumbersRepository, accountNumberService);
        inOrder.verify(freeAccountNumbersRepository).countByType(accountType);
        inOrder.verify(accountNumberService, times(amount)).generateAccountNumber(accountType);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1, -100000 })
    void generateFreeAccountNumbersBad(int amount) {

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> accountNumberGenerationService.generateFreeAccountNumbers(amount, accountType));
        assertEquals("Amount must be greater than 0", e.getMessage());

        InOrder inOrder = inOrder(freeAccountNumbersRepository, accountNumberService);
        inOrder.verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 100000 })
    public void generateFreeAccountNumbersBadCount(int amount) {

        when(freeAccountNumbersRepository.countByType(accountType)).thenReturn(Long.valueOf(amount));

        RuntimeException e = assertThrows(RuntimeException.class,
                () -> accountNumberGenerationService.generateFreeAccountNumbers(amount, accountType));

        assertEquals("There are no free accounts: currentNumbersAmount=" + amount + ", dif=" + 0, e.getMessage());

        InOrder inOrder = inOrder(freeAccountNumbersRepository, accountNumberService);
        inOrder.verify(freeAccountNumbersRepository).countByType(accountType);
        inOrder.verifyNoMoreInteractions();
    }
}
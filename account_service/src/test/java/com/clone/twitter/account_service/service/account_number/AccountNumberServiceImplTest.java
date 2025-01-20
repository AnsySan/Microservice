package com.clone.twitter.account_service.service.account_number;

import com.clone.twitter.account_service.model.account_number.AccountNumberSequence;
import com.clone.twitter.account_service.model.account_number.FreeAccountNumber;
import com.clone.twitter.account_service.model.account_number.FreeAccountNumberId;
import com.clone.twitter.account_service.model.enums.AccountType;
import com.clone.twitter.account_service.repository.AccountNumbersSequenceRepository;
import com.clone.twitter.account_service.repository.FreeAccountNumbersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountNumberServiceImplTest {

    @Mock
    private AccountNumbersSequenceRepository accountNumbersSequenceRepository;
    @Mock
    private FreeAccountNumbersRepository freeAccountNumbersRepository;

    @InjectMocks
    private AccountNumberServiceImpl accountNumberService;

    private Map<AccountType, AccountNumberSequence> accountNumberSequenceMap;
    private Map<AccountType, FreeAccountNumber> freeAccountNumberMap;

    @BeforeEach
    void setUp() {

       accountNumberSequenceMap = Map.of(
                AccountType.CORPORATE, AccountNumberSequence.builder().count(9999_9999L).accountType(AccountType.CORPORATE).build(),
                AccountType.INDIVIDUAL, AccountNumberSequence.builder().count(1000_0000_0000L).accountType(AccountType.INDIVIDUAL).build(),
                AccountType.INVESTMENT, AccountNumberSequence.builder().count(1000_0000L).accountType(AccountType.INVESTMENT).build(),
                AccountType.SAVINGS, AccountNumberSequence.builder().count(0).accountType(AccountType.SAVINGS).build()
        );

        freeAccountNumberMap = Map.of(
                AccountType.CORPORATE, new FreeAccountNumber(new FreeAccountNumberId(AccountType.CORPORATE, "123412341234")),
                AccountType.INDIVIDUAL, new FreeAccountNumber(new FreeAccountNumberId(AccountType.INDIVIDUAL, "432143214321")),
                AccountType.INVESTMENT, new FreeAccountNumber(new FreeAccountNumberId(AccountType.INVESTMENT, "143514351435")),
                AccountType.SAVINGS, new FreeAccountNumber(new FreeAccountNumberId(AccountType.SAVINGS, "143514351435"))
        );
    }

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void getUniqueAccountNumber(AccountType accountType) {

        FreeAccountNumber number = freeAccountNumberMap.get(accountType);

        when(freeAccountNumbersRepository.getAndDeleteFirst(accountType)).thenReturn(Optional.of(number));

        accountNumberService.getUniqueAccountNumber(
                accountNumber -> assertEquals(number, accountNumber),
                accountType
        );

        InOrder inOrder = inOrder(freeAccountNumbersRepository, accountNumbersSequenceRepository);
        inOrder.verify(freeAccountNumbersRepository).getAndDeleteFirst(accountType);
    }

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void generateAccountNumber(AccountType accountType) {

        AccountNumberSequence sequence = accountNumberSequenceMap.get(accountType);

        when(accountNumbersSequenceRepository.findByAccountType(accountType)).thenReturn(Optional.empty());
        when(accountNumbersSequenceRepository.createAndGetSequence(accountType)).thenReturn(sequence);
        when(accountNumbersSequenceRepository.incrementIfEquals(sequence.getCount(), accountType)).thenReturn(true);

        FreeAccountNumber generatedAccountNumber = accountNumberService.generateAccountNumber(accountType);
        assertEquals(accountType, generatedAccountNumber.getId().getType());

        InOrder inOrder = inOrder(freeAccountNumbersRepository, accountNumbersSequenceRepository);
        inOrder.verify(accountNumbersSequenceRepository).findByAccountType(accountType);
        inOrder.verify(accountNumbersSequenceRepository).createAndGetSequence(accountType);
        inOrder.verify(accountNumbersSequenceRepository).incrementIfEquals(sequence.getCount(), accountType);
    }
}
package com.clone.twitter.account_service.service.account_number;

import com.clone.twitter.account_service.model.account_number.AccountNumberSequence;
import com.clone.twitter.account_service.model.account_number.FreeAccountNumber;
import com.clone.twitter.account_service.model.account_number.FreeAccountNumberId;
import com.clone.twitter.account_service.model.enums.AccountType;
import com.clone.twitter.account_service.repository.AccountNumbersSequenceRepository;
import com.clone.twitter.account_service.repository.FreeAccountNumbersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
public class AccountNumberServiceImplIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:13.6");

    @Autowired
    private AccountNumbersSequenceRepository accountNumbersSequenceRepository;
    @Autowired
    private FreeAccountNumbersRepository freeAccountNumbersRepository;
    @Autowired
    private AccountNumberServiceImpl accountNumberService;

    private Map<AccountType, FreeAccountNumber> freeAccountNumberMap;

    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void setUp() {

        assertTrue(postgreSQLContainer.isRunning());

        accountNumbersSequenceRepository.deleteAll();
        List<AccountNumberSequence> accountNumberSequenceMap = List.of(
                AccountNumberSequence.builder().count(9999_9999L).accountType(AccountType.CORPORATE).build(),
                AccountNumberSequence.builder().count(1000_0000_0000L).accountType(AccountType.INDIVIDUAL).build(),
                AccountNumberSequence.builder().count(1000_0000L).accountType(AccountType.INVESTMENT).build(),
                AccountNumberSequence.builder().count(0).accountType(AccountType.SAVINGS).build()
        );
        accountNumbersSequenceRepository.saveAll(accountNumberSequenceMap);

        freeAccountNumbersRepository.deleteAll();
        freeAccountNumberMap = Map.of(
                AccountType.CORPORATE, new FreeAccountNumber(new FreeAccountNumberId(AccountType.CORPORATE, "123412341234")),
                AccountType.INDIVIDUAL, new FreeAccountNumber(new FreeAccountNumberId(AccountType.INDIVIDUAL, "432143214321")),
                AccountType.INVESTMENT, new FreeAccountNumber(new FreeAccountNumberId(AccountType.INVESTMENT, "143514351435")),
                AccountType.SAVINGS, new FreeAccountNumber(new FreeAccountNumberId(AccountType.SAVINGS, "143514351435"))
        );
        freeAccountNumbersRepository.saveAll(freeAccountNumberMap.values());
    }

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void getUniqueAccountNumber(AccountType accountType) {

        FreeAccountNumber number = freeAccountNumberMap.get(accountType);

        accountNumberService.getUniqueAccountNumber(
                accountNumber -> assertEquals(number, accountNumber),
                accountType
        );
    }

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void generateAccountNumber(AccountType accountType) {

        FreeAccountNumber generatedAccountNumber = accountNumberService.generateAccountNumber(accountType);

        assertEquals(accountType, generatedAccountNumber.getId().getType());
    }
}

package com.clone.twitter.account_service.service.account_number.generation;

import com.clone.twitter.account_service.model.enums.AccountType;
import com.clone.twitter.account_service.repository.FreeAccountNumbersRepository;
import com.clone.twitter.account_service.service.account_number.AccountNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountNumberGenerationServiceImpl implements AccountNumberGenerationService {

    private final FreeAccountNumbersRepository freeAccountNumbersRepository;
    private final AccountNumberService accountNumberService;

    @Override
    @Transactional
    public void generateFreeAccountNumbers(int amount, AccountType accountType) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        long currentNumbersAmount = freeAccountNumbersRepository.countByType(accountType);
        long dif = amount - currentNumbersAmount;

        if (dif <= 0) {
            throw new RuntimeException("There are no free accounts: currentNumbersAmount=" + currentNumbersAmount + ", dif=" + dif);
        }

        for (int i = 0; i < dif; i++) {
            accountNumberService.generateAccountNumber(accountType);
        }
    }

}

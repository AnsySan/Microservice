package com.clone.twitter.account_service.scheduler.account_number;

import com.clone.twitter.account_service.model.enums.AccountType;
import com.clone.twitter.account_service.property.AccountNumberGenerationAmountProperties;
import com.clone.twitter.account_service.service.account_number.generation.AccountNumberGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountNumberGenerationScheduler {

    private final AccountNumberGenerationAmountProperties accountNumberGenerationAmountProperties;
    private final AccountNumberGenerationService accountNumberGenerationService;

    @Scheduled(cron = "${scheduling.account-number-generation.cron}")
    public void getAccountNumberGenerationService() {

        for (Map.Entry<AccountType, Integer> entry : accountNumberGenerationAmountProperties.getAmounts().entrySet()) {

            accountNumberGenerationService.generateFreeAccountNumbers(entry.getValue(), entry.getKey());

            log.info("{} account numbers generation for {}", entry.getKey(), entry.getValue());
        }
    }
}

package com.clone.twitter.account_service.service.account_number.generation;

import com.clone.twitter.account_service.model.enums.AccountType;

public interface AccountNumberGenerationService {

    void generateFreeAccountNumbers(int amount, AccountType accountType);
}

package com.clone.twitter.account_service.service.account_number;

import com.clone.twitter.account_service.model.account_number.FreeAccountNumber;
import com.clone.twitter.account_service.model.enums.AccountType;

import java.util.function.Consumer;

public interface AccountNumberService {

    void getUniqueAccountNumber(Consumer<FreeAccountNumber> action, AccountType accountType);

    FreeAccountNumber generateAccountNumber(AccountType accountType);
}

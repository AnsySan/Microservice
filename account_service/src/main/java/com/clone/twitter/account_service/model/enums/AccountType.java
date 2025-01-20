package com.clone.twitter.account_service.model.enums;

import lombok.Getter;

@Getter
public enum AccountType {
    INDIVIDUAL(4200, 20),
    SAVINGS(5236, 20),
    CORPORATE(1040, 16),
    INVESTMENT(8238, 16);

    private final int type;
    private final int length;

    AccountType(int type, int length) {
        this.type = type;
        this.length = length;
    }
}
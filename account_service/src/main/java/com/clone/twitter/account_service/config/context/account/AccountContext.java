package com.clone.twitter.account_service.config.context.account;

import org.springframework.stereotype.Component;

@Component
public class AccountContext {

    private final ThreadLocal<Long> accountIdHolder = new ThreadLocal<>();

    public void setAccountId(long accountId) {
        accountIdHolder.set(accountId);
    }

    public long getAccountId() {
        return accountIdHolder.get();
    }

    public void clear() {
        accountIdHolder.remove();
    }
}

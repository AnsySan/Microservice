package com.clone.twitter.account_service.service.account;

import com.clone.twitter.account_service.dto.account.AccountCreateDto;
import com.clone.twitter.account_service.dto.account.AccountDto;
import com.clone.twitter.account_service.dto.account.AccountDtoToUpdate;
import org.springframework.stereotype.Component;

@Component
public interface AccountService {
    AccountDto open(AccountCreateDto accountCreateDto);

    AccountDto update(long accountId, AccountDtoToUpdate accountDto);

    AccountDto get(long accountId);

    void block(long accountId);

    void unBlock(long accountId);

    void close(long accountId);
}

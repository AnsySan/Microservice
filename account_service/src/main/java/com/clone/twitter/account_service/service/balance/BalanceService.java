package com.clone.twitter.account_service.service.balance;

import com.clone.twitter.account_service.dto.balance.BalanceDto;
import com.clone.twitter.account_service.dto.balance.BalanceUpdateDto;
import org.springframework.stereotype.Component;

@Component
public interface BalanceService {
    BalanceDto createBalance(long accountId);

    BalanceDto updateBalance(BalanceUpdateDto balanceUpdateDto);

    BalanceDto getBalance(long balanceId);

    void deleteBalance(long balanceId);
}

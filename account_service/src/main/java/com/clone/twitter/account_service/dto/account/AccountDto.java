package com.clone.twitter.account_service.dto.account;

import com.clone.twitter.account_service.dto.OwnerDto;
import com.clone.twitter.account_service.model.enums.AccountType;
import com.clone.twitter.account_service.model.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    private String number;
    private OwnerDto owner;
    private AccountType accountType;
    private Currency currency;
}
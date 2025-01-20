package com.clone.twitter.account_service.mapper;

import com.clone.twitter.account_service.dto.balance.BalanceDto;
import com.clone.twitter.account_service.dto.balance.BalanceUpdateDto;
import com.clone.twitter.account_service.model.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BalanceMapper {

    @Mapping(source = "account.id", target = "accountId")
    BalanceDto toDto(Balance balance);

    @Mapping(target = "account", ignore = true)
    Balance toEntity(BalanceUpdateDto balanceUpdateDto);
}
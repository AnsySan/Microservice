package com.clone.twitter.account_service.mapper;

import com.clone.twitter.account_service.dto.account.AccountCreateDto;
import com.clone.twitter.account_service.dto.account.AccountDto;
import com.clone.twitter.account_service.dto.account.AccountDtoToUpdate;
import com.clone.twitter.account_service.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    Account toEntity(AccountCreateDto accountCreateDto);

    AccountDto toDto(Account account);

    void update(AccountDtoToUpdate dto, @MappingTarget Account account);
}
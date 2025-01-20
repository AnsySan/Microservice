package com.clone.twitter.account_service.mapper;

import com.clone.twitter.account_service.dto.balance.BalanceUpdateDto;
import com.clone.twitter.account_service.dto.balance_audit.BalanceAuditDto;
import com.clone.twitter.account_service.model.BalanceAudit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BalanceAuditMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "accountId", target = "account.id")
    BalanceAudit toAudit(BalanceUpdateDto balanceUpdateDto);

    @Mapping(source = "account.id", target = "accountId")
    BalanceAuditDto toDto(BalanceAudit balanceAudit);
}

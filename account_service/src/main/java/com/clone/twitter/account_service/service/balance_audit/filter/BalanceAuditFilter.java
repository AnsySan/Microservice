package com.clone.twitter.account_service.service.balance_audit.filter;

import com.clone.twitter.account_service.dto.balance_audit.BalanceAuditFilterDto;
import com.clone.twitter.account_service.model.BalanceAudit;

import java.util.stream.Stream;

public interface BalanceAuditFilter {

    boolean isAcceptable(BalanceAuditFilterDto balanceAuditFilterDto);

    Stream<BalanceAudit> accept(Stream<BalanceAudit> balanceAuditFilterStream, BalanceAuditFilterDto balanceAuditFilterDto);
}

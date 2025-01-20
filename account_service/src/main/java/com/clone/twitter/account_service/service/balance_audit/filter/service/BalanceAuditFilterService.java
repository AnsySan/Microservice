package com.clone.twitter.account_service.service.balance_audit.filter.service;

import com.clone.twitter.account_service.dto.balance_audit.BalanceAuditFilterDto;
import com.clone.twitter.account_service.model.BalanceAudit;

import java.util.stream.Stream;

public interface BalanceAuditFilterService {

    Stream<BalanceAudit> acceptAll(Stream<BalanceAudit> balanceAuditStream, BalanceAuditFilterDto balanceAuditFilterDto);
}

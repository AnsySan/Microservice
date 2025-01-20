package com.clone.twitter.account_service.service.balance_audit;

import com.clone.twitter.account_service.dto.balance.BalanceUpdateDto;
import com.clone.twitter.account_service.dto.balance_audit.BalanceAuditDto;
import com.clone.twitter.account_service.dto.balance_audit.BalanceAuditFilterDto;

import java.util.List;

public interface BalanceAuditService {

    void createNewAudit(BalanceUpdateDto balanceUpdateDto);

    List<BalanceAuditDto> findByAccountId(long accountId, BalanceAuditFilterDto balanceAuditFilterDto);
}

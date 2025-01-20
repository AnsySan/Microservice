package com.clone.twitter.account_service.service.balance_audit.filter;

import com.clone.twitter.account_service.dto.balance_audit.BalanceAuditFilterDto;
import com.clone.twitter.account_service.model.BalanceAudit;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class BalanceAuditEndDateFilter implements BalanceAuditFilter {

    @Override
    public boolean isAcceptable(BalanceAuditFilterDto balanceAuditFilterDto) {
        return balanceAuditFilterDto.getEndDate() != null;
    }

    @Override
    public Stream<BalanceAudit> accept(Stream<BalanceAudit> balanceAuditFilterStream, BalanceAuditFilterDto balanceAuditFilterDto) {
        return balanceAuditFilterStream
                .filter(balanceAuditDto -> balanceAuditFilterDto.getEndDate().isBefore(balanceAuditFilterDto.getEndDate()));
    }
}

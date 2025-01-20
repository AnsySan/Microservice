package com.clone.twitter.account_service.service.balance_audit.filter.service;

import com.clone.twitter.account_service.dto.balance_audit.BalanceAuditFilterDto;
import com.clone.twitter.account_service.model.BalanceAudit;
import com.clone.twitter.account_service.service.balance_audit.filter.BalanceAuditFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BalanceAuditFilterServiceImpl implements BalanceAuditFilterService {

    private final List<BalanceAuditFilter> filters;

    @Override
    public Stream<BalanceAudit> acceptAll(Stream<BalanceAudit> balanceAuditStream, BalanceAuditFilterDto balanceAuditFilterDto) {

        if (balanceAuditFilterDto == null) {
            return balanceAuditStream;
        }

        return filters.stream()
                .filter(filter -> filter.isAcceptable(balanceAuditFilterDto))
                .flatMap(filter -> filter.accept(balanceAuditStream, balanceAuditFilterDto));
    }
}

package com.clone.twitter.account_service.controller.balance_audit;

import com.clone.twitter.account_service.config.context.account.AccountContext;
import com.clone.twitter.account_service.dto.balance_audit.BalanceAuditDto;
import com.clone.twitter.account_service.dto.balance_audit.BalanceAuditFilterDto;
import com.clone.twitter.account_service.service.balance_audit.BalanceAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/balance_audits")
public class BalanceAuditController {

    private final BalanceAuditService balanceAuditService;
    private final AccountContext accountContext;

    @GetMapping()
    public List<BalanceAuditDto> findAll(BalanceAuditFilterDto balanceAuditFilterDto) {

        long accountId = accountContext.getAccountId();
        return balanceAuditService.findByAccountId(accountId, balanceAuditFilterDto);
    }
}

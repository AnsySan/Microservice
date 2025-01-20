package com.clone.twitter.account_service.config.context.account;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccountHeaderFilter implements Filter {

    private final AccountContext accountContext;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        String accountId = req.getHeader("x-account-id");
        if (accountId != null) {
            accountContext.setAccountId(Long.parseLong(accountId));
        }
        try {
            chain.doFilter(request, response);
        } finally {
            accountContext.clear();
        }
    }
}

package com.clone.twitter.account_service.service.balance;

import com.clone.twitter.account_service.dto.balance.BalanceDto;
import com.clone.twitter.account_service.dto.balance.BalanceUpdateDto;
import com.clone.twitter.account_service.exception.NotFoundException;
import com.clone.twitter.account_service.mapper.BalanceMapper;
import com.clone.twitter.account_service.model.Account;
import com.clone.twitter.account_service.model.Balance;
import com.clone.twitter.account_service.repository.AccountRepository;
import com.clone.twitter.account_service.repository.BalanceRepository;
import com.clone.twitter.account_service.service.balance_audit.BalanceAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final AccountRepository accountRepository;
    private final BalanceAuditService balanceAuditService;
    private final BalanceMapper balanceMapper;

    @Override
    @Transactional
    public BalanceDto createBalance(long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Account with id %d not found", accountId)));

        Balance balance = new Balance();
        balance.setAccount(account);
        balance.setActualBalance(new BigDecimal(0));
        balance.setAuthorizationBalance(new BigDecimal(0));

        balanceRepository.save(balance);
        log.info("Created balance for account: {}", account.getId());
        return balanceMapper.toDto(balance);
    }

    @Override
    @Transactional
    public BalanceDto updateBalance(BalanceUpdateDto balanceUpdateDto) {

        Balance balance = balanceRepository.findById(balanceUpdateDto.getId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Balance not found %d", balanceUpdateDto.getId())));

        balanceRepository.save(balanceMapper.toEntity(balanceUpdateDto));

        balanceAuditService.createNewAudit(balanceUpdateDto);

        return balanceMapper.toDto(balance);
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceDto getBalance(long balanceId) {
        Balance balance = balanceRepository.findById(balanceId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Balance: %d not found", balanceId)));
        return balanceMapper.toDto(balance);
    }

    @Override
    @Transactional()
    public void deleteBalance(long balanceId) {
        Balance balance = balanceRepository.findById(balanceId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Balance: %d not found", balanceId)));
        balanceRepository.delete(balance);
    }
}
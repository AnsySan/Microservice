package com.clone.twitter.account_service.service.account_number;

import com.clone.twitter.account_service.model.account_number.AccountNumberSequence;
import com.clone.twitter.account_service.model.account_number.FreeAccountNumber;
import com.clone.twitter.account_service.model.account_number.FreeAccountNumberId;
import com.clone.twitter.account_service.model.enums.AccountType;
import com.clone.twitter.account_service.repository.AccountNumbersSequenceRepository;
import com.clone.twitter.account_service.repository.FreeAccountNumbersRepository;
import com.clone.twitter.account_service.util.AccountNumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountNumberServiceImpl implements AccountNumberService {

    private final AccountNumbersSequenceRepository accountNumbersSequenceRepository;
    private final FreeAccountNumbersRepository freeAccountNumbersRepository;

    @Override
    @Transactional
    @Retryable(retryFor = OptimisticLockingFailureException.class, backoff = @Backoff(delay = 3000L))
    public void getUniqueAccountNumber(Consumer<FreeAccountNumber> action, AccountType accountType) {

        FreeAccountNumber accountNumber = freeAccountNumbersRepository.getAndDeleteFirst(accountType)
                .orElseGet(() -> generateAccountNumber(accountType));

        action.accept(accountNumber);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(retryFor = OptimisticLockingFailureException.class, backoff = @Backoff(delay = 3000L))
    public FreeAccountNumber generateAccountNumber(AccountType accountType) {

        int code = accountType.getType();
        int length = accountType.getLength();

        AccountNumberSequence accountNumberSequence = accountNumbersSequenceRepository.findByAccountType(accountType)
                .orElseGet(() -> accountNumbersSequenceRepository.createAndGetSequence(accountType));

        long count = accountNumberSequence.getCount();

        boolean canIncrement = accountNumbersSequenceRepository.incrementIfEquals(count, accountType);
        if (!canIncrement) {
            throw new OptimisticLockingFailureException("Account number=" + count + " already in use");
        }

        String value = AccountNumberUtil.getAccountNumber(code, length, count);
        FreeAccountNumber freeAccountNumber = new FreeAccountNumber(new FreeAccountNumberId(accountType, value));

        freeAccountNumbersRepository.saveAndFlush(freeAccountNumber);

        return freeAccountNumber;
    }
}

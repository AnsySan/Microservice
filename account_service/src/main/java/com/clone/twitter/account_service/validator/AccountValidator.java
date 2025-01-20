package com.clone.twitter.account_service.validator;

import com.clone.twitter.account_service.client.ProjectServiceClient;
import com.clone.twitter.account_service.client.UserServiceClient;
import com.clone.twitter.account_service.config.context.user.UserContext;
import com.clone.twitter.account_service.exception.DataValidationException;
import com.clone.twitter.account_service.exception.NotFoundException;
import com.clone.twitter.account_service.model.Account;
import com.clone.twitter.account_service.model.enums.AccountStatus;
import com.clone.twitter.account_service.model.enums.OwnerType;
import com.clone.twitter.account_service.repository.AccountRepository;
import feign.FeignException;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountValidator {
    private final UserServiceClient userServiceClient;
    private final ProjectServiceClient projectServiceClient;
    private final AccountRepository accountRepository;
    private final UserContext userContext;

    public void validateCreate(Account account) {
        validateNumberIsUnique(account);
        validateUserOrProjectExist(account);
        validateAccountIdForUserIsCorrect(account);
        validateUserIsProjectMember(account);
    }

    public void validateBlock(Account account) {
        validateStatus(account, AccountStatus.ACTIVE);
    }

    public void validateUnblock(Account account) {
        validateStatus(account, AccountStatus.FROZEN);
    }

    public void validateClose(Account account) {
        validateStatusForClose(account);
    }

    private void validateNumberIsUnique(Account account) {
        boolean numberIsUnique = accountRepository.checkNumberIsUnique(account.getNumber());
        if (!numberIsUnique) {
            throw new DataValidationException(String.format("Number is not unique %s", account.getNumber()));
        }
    }

    private void validateAccountIdForUserIsCorrect(Account account) {

        if (OwnerType.USER.equals(account.getOwner().getOwnerType())) {
            long userId = userContext.getUserId();
            if (userId != account.getOwner().getAccountId()) {
                throw new DataValidationException(
                        "UserContext and accountId is different, only author could create new account");
            }
        }
    }

    private void validateUserIsProjectMember(Account account) {

        if (OwnerType.PROJECT.equals(account.getOwner().getOwnerType())) {
            boolean isOwner = projectServiceClient.checkProjectOwner(
                    account.getOwner().getAccountId(),
                    userContext.getUserId());

            if (!isOwner) {
                throw new DataValidationException(
                        String.format("Only project owner could create account for project %d",
                                account.getOwner().getAccountId()));
            }
        }
    }

    @Retryable(retryFor = ReadTimeoutException.class, backoff = @Backoff(delay = 2000))
    private void validateUserOrProjectExist(Account account) {
        try {
            if (OwnerType.USER.equals(account.getOwner().getOwnerType())) {
                userServiceClient.getUser(account.getOwner().getAccountId());
            } else if (OwnerType.PROJECT.equals(account.getOwner().getOwnerType())) {
                projectServiceClient.getProject(account.getOwner().getAccountId());
            }
        } catch (FeignException ex) {
            throw new NotFoundException(String.format("%s with id %d not found",
                    account.getOwner().getOwnerType(), account.getOwner().getAccountId()));
        }
    }

    private void validateStatus(Account account, AccountStatus status) {
        if (!status.equals(account.getAccountStatus())) {
            throw new DataValidationException(
                    String.format("Status should be %s, but account: %d is %s",
                            status,
                            account.getId(),
                            account.getAccountStatus()));
        }
    }

    private void validateStatusForClose(Account account) {
        if (AccountStatus.CLOSED.equals(account.getAccountStatus())) {
            throw new DataValidationException(String.format("Account: %d already closed", account.getId()));
        }
    }
}
package com.clone.twitter.account_service.repository;

import com.clone.twitter.account_service.model.account_number.FreeAccountNumber;
import com.clone.twitter.account_service.model.account_number.FreeAccountNumberId;
import com.clone.twitter.account_service.model.enums.AccountType;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FreeAccountNumbersRepository extends JpaRepository<FreeAccountNumber, FreeAccountNumberId> {

    Optional<FreeAccountNumber> findByIdType(AccountType type);

    void deleteById(@NonNull FreeAccountNumberId freeAccountNumberId);

    @Query("SELECT count(f) FROM FreeAccountNumber f WHERE f.id.type = ?1")
    long countByType(AccountType type);

    @Transactional
    default Optional<FreeAccountNumber> getAndDeleteFirst(AccountType accountType) {
        FreeAccountNumber freeAccountNumber = findByIdType(accountType)
                .orElse(null);

        if (freeAccountNumber == null) {
            return Optional.empty();
        }

        deleteById(freeAccountNumber.getId());
        return Optional.of(freeAccountNumber);
    }
}

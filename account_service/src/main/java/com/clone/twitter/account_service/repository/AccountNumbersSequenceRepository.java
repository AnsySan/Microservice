package com.clone.twitter.account_service.repository;

import com.clone.twitter.account_service.exception.NotFoundException;
import com.clone.twitter.account_service.model.account_number.AccountNumberSequence;
import com.clone.twitter.account_service.model.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AccountNumbersSequenceRepository extends JpaRepository<AccountNumberSequence, Long> {

    Optional<AccountNumberSequence> findByAccountType(AccountType accountType);

    @Query(nativeQuery = true, value = """
            INSERT INTO account_numbers_sequence (account_type)
            VALUES (:#{#accountType.toString()})
            """)
    @Modifying
    void createSequence(AccountType accountType);

    @Query(nativeQuery = true, value = """
            UPDATE account_numbers_sequence SET count = count + 1
            WHERE count = :count AND account_type = :#{#accountType.toString()}
            """)
    @Modifying
    int incrementIfEqualsCountAndType(long count, AccountType accountType);

    @Transactional
    default AccountNumberSequence createAndGetSequence(AccountType accountType) {
        createSequence(accountType);
        return findByAccountType(accountType)
                .orElseThrow(() -> new NotFoundException("Sequence with accountType=" + accountType + " not found"));
    }

    @Transactional
    default boolean incrementIfEquals(long count, AccountType accountType) {
        int rowsAffected = incrementIfEqualsCountAndType(count, accountType);
        return rowsAffected > 0;
    }
}

package com.clone.twitter.payment_service.repository;

import com.clone.twitter.payment_service.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {


    @Query("SELECT b FROM Balance b JOIN b.account a WHERE a.number = :accountNumber")
    Optional<Balance> findBalanceByAccountNumber(String accountNumber);
}

package com.clone.twitter.account_service.repository;

import com.clone.twitter.account_service.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long>  {
}
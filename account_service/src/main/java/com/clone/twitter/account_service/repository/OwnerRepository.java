package com.clone.twitter.account_service.repository;

import com.clone.twitter.account_service.model.Owner;
import com.clone.twitter.account_service.model.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Optional<Owner> findByAccountIdAndOwnerType(long accountId, OwnerType ownerType);
}
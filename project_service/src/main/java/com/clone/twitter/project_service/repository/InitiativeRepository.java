package com.clone.twitter.project_service.repository;

import com.clone.twitter.project_service.model.initiative.Initiative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InitiativeRepository extends JpaRepository<Initiative, Long> {
}

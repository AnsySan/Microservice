package com.clone.twitter.project_service.repository;

import com.clone.twitter.project_service.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    boolean existsByKey(String key);
}

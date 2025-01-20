package com.clone.twitter.project_service.repository;

import com.clone.twitter.project_service.model.stage.StageRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StageRolesRepository extends JpaRepository<StageRoles, Long> {
    List<StageRoles> findAllByStageStageId(long stageId);
}

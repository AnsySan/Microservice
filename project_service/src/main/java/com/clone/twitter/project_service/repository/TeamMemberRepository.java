package com.clone.twitter.project_service.repository;

import com.clone.twitter.project_service.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    @Query(
            "SELECT tm FROM TeamMember tm JOIN tm.team t " +
                    "WHERE tm.userId = :userId " +
                    "AND t.project.id = :projectId"
    )
    Optional<TeamMember> findByUserIdAndProjectId(long userId, long projectId);

    List<TeamMember> findByUserId(long userId);

    @Query("""
                    SELECT CASE WHEN COUNT(tm) > 0 THEN TRUE ELSE FALSE END
                    FROM TeamMember tm JOIN tm.team t
                    WHERE tm.userId = :userId
                    AND t.project.id = :projectId
            """)
    boolean existsByUserIdAndProjectId(long userId, long projectId);
}
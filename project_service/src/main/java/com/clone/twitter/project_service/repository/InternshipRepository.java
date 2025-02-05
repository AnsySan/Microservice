package com.clone.twitter.project_service.repository;

import com.clone.twitter.project_service.model.Internship;
import com.clone.twitter.project_service.model.InternshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {

    List<Internship> findByStatus(InternshipStatus status);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO internship_interns (internship_id, team_member_id) VALUES (:internshipId, :teamMemberId)", nativeQuery = true)
    void addInternToInternship(long internshipId, long teamMemberId);

    @Query(value = "SELECT * FROM internship WHERE project_id = :projectId", nativeQuery = true)
    List<Internship> findByProjectId(@Param("projectId") long projectId);

    @Modifying
    @Query(value = "DELETE FROM internship_interns WHERE team_member_id = :teamMemberId AND internship_id = :internshipId", nativeQuery = true)
    void removeInternFromInternship(@Param("teamMemberId") long teamMemberId, @Param("internshipId") long internshipId);
}

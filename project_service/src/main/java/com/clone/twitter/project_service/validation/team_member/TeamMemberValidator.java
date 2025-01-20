package com.clone.twitter.project_service.validation.team_member;

public interface TeamMemberValidator {
    void validateExistence(long id);

    void validateExistenceByUserIdAndProjectId(long userId, long projectId);
}

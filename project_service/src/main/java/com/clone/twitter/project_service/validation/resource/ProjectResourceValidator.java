package com.clone.twitter.project_service.validation.resource;

import com.clone.twitter.project_service.model.Project;
import com.clone.twitter.project_service.model.Resource;
import com.clone.twitter.project_service.model.TeamMember;

public interface ProjectResourceValidator {

    void validateMaxStorageSize(Project project, long newFileLength);

    void validateDeletePermission(TeamMember teamMember, Resource resource);
}

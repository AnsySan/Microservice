package com.clone.twitter.project_service.validation.resource;

import com.clone.twitter.project_service.exceptions.DataValidationException;
import com.clone.twitter.project_service.exceptions.NoAccessException;
import com.clone.twitter.project_service.model.Project;
import com.clone.twitter.project_service.model.Resource;
import com.clone.twitter.project_service.model.TeamMember;
import com.clone.twitter.project_service.model.TeamRole;
import com.clone.twitter.project_service.property.AmazonS3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public class ProjectResourceValidatorImpl implements ProjectResourceValidator {

    private final AmazonS3Properties amazonS3Properties;

    @Override
    public void validateMaxStorageSize(Project project, long newFileLength) {
        BigInteger newLength = project.getStorageSize().add(BigInteger.valueOf(newFileLength));
        if (newLength.compareTo(amazonS3Properties.getMaxFreeStorageSizeBytes()) > 0) {
            throw new DataValidationException("Project with projectId=" + project.getId() + " storage is full");
        }
    }

    @Override
    public void validateDeletePermission(TeamMember teamMember, Resource resource) {
        if (!resource.getCreatedBy().equals(teamMember) && !teamMember.getRoles().contains(TeamRole.MANAGER)) {
            throw new NoAccessException("TeamMember with id=" + teamMember.getId() +
                    " has no permissions to delete resource with id=" + resource.getId());
        }
    }
}

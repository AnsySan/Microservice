package com.clone.twitter.project_service.service.project;

import com.clone.twitter.project_service.dto.project.ProjectCoverDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

public interface ProjectCoverService {

    ProjectCoverDto save(long projectId, MultipartFile file);

    InputStreamResource getByProjectId(long projectId);

    ProjectCoverDto changeProjectCover(long projectId,  MultipartFile file);


    ProjectCoverDto deleteByProjectId(long projectId);
}

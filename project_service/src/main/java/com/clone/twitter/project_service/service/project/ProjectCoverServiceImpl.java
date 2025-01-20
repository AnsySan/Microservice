package com.clone.twitter.project_service.service.project;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.clone.twitter.project_service.config.context.UserContext;
import com.clone.twitter.project_service.dto.project.ProjectCoverDto;
import com.clone.twitter.project_service.exceptions.DataValidationException;
import com.clone.twitter.project_service.mapper.ProjectCoverMapper;
import com.clone.twitter.project_service.model.Project;
import com.clone.twitter.project_service.repository.ProjectRepository;
import com.clone.twitter.project_service.service.s3.AmazonS3Service;
import com.clone.twitter.project_service.util.project.ImageProcessor;
import com.clone.twitter.project_service.validation.project.ProjectValidator;
import com.clone.twitter.project_service.validation.team_member.TeamMemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectCoverServiceImpl implements ProjectCoverService {
    private final TeamMemberValidator teamMemberValidator;
    private final ProjectCoverMapper projectCoverMapper;
    private final ProjectRepository projectRepository;
    private final ProjectValidator projectValidator;
    private final AmazonS3Service amazonS3Service;
    private final ImageProcessor imageProcessor;
    private final UserContext userContext;


    @Override
    @Transactional
    public ProjectCoverDto save(long projectId, MultipartFile file) {
        validateCurrentUser(projectId);
        String coverImageId = uploadCover(projectId, file);

        Project project = getById(projectId);
        project.setCoverImageId(coverImageId);
        project.setUpdatedAt(LocalDateTime.now());
        project = projectRepository.save(project);

        return projectCoverMapper.toDto(project);
    }

    @Override
    @Transactional(readOnly = true)
    public InputStreamResource getByProjectId(long projectId) {
        long currentUserId = userContext.getUserId();
        teamMemberValidator.validateExistenceByUserIdAndProjectId(currentUserId, projectId);
        Project project = getById(projectId);

        return amazonS3Service.downloadFile(project.getCoverImageId());
    }

    @Override
    @Transactional
    public ProjectCoverDto changeProjectCover(long projectId, MultipartFile file) {
        validateCurrentUser(projectId);

        Project project = getById(projectId);
        deleteOldCoverImage(project);

        String coverImageId = uploadCover(projectId, file);
        project.setCoverImageId(coverImageId);
        project.setUpdatedAt(LocalDateTime.now());
        project = projectRepository.save(project);

        return projectCoverMapper.toDto(project);
    }

    @Override
    @Transactional
    public ProjectCoverDto deleteByProjectId(long projectId) {
        validateCurrentUser(projectId);
        Project project = getById(projectId);

        String imageCoverId = project.getCoverImageId();
        amazonS3Service.deleteFile(imageCoverId);

        project.setCoverImageId(null);
        project = projectRepository.save(project);

        return projectCoverMapper.toDto(project);
    }

    public Project getById(long projectId) {
        Optional<Project> optional = projectRepository.findById(projectId);
        return optional.orElseThrow(() -> {
            String message = String.format("a project with %d does not exist", projectId);

            return new DataValidationException(message);
        });
    }

    private void validateCurrentUser(long projectId) {
        long currentUserId = userContext.getUserId();
        projectValidator.validateProjectIdAndCurrentUserId(projectId, currentUserId);
    }

    private String uploadCover(long projectId, MultipartFile file) {
        Pair<InputStream, ObjectMetadata> processedFile = imageProcessor.processCover(file);
        String path = String.format("project/cover/%d", projectId);

        return amazonS3Service.uploadFile(path, processedFile);
    }

    private void deleteOldCoverImage(Project project) {
        String oldImageCoverId = project.getCoverImageId();

        if (StringUtils.hasText(oldImageCoverId)) {
            amazonS3Service.deleteFile(oldImageCoverId);
        }
    }
}

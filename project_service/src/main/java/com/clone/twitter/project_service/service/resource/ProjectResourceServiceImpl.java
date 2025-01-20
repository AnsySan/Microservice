package com.clone.twitter.project_service.service.resource;

import com.clone.twitter.project_service.dto.resource.ResourceDto;
import com.clone.twitter.project_service.exceptions.NotFoundException;
import com.clone.twitter.project_service.mapper.ResourceMapper;
import com.clone.twitter.project_service.model.*;
import com.clone.twitter.project_service.repository.ResourceRepository;
import com.clone.twitter.project_service.repository.TeamMemberRepository;
import com.clone.twitter.project_service.service.s3.AmazonS3Service;
import com.clone.twitter.project_service.validation.resource.ProjectResourceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProjectResourceServiceImpl implements ProjectResourceService {

    private final AmazonS3Service amazonS3Service;
    private final ResourceRepository resourceRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ProjectResourceValidator projectResourceValidator;
    private final ResourceMapper resourceMapper;

    @Override
    @Transactional
    public ResourceDto saveFile(long userId, long projectId, MultipartFile file) {

        TeamMember teamMember = findTeamMemberByUserIdAndProjectId(userId, projectId);
        Project project = teamMember.getTeam().getProject();

        projectResourceValidator.validateMaxStorageSize(project, file.getSize());

        String path = "project/" + projectId + "/";
        String key = amazonS3Service.uploadFile(path, file);

        Resource resource = buildResource(key, file, teamMember);
        resource = resourceRepository.save(resource);

        return resourceMapper.toDto(resource);
    }

    @Override
    @Transactional(readOnly = true)
    public InputStreamResource getFile(long userId, long projectId, long resourceId) {

        Resource resource = findResourceById(resourceId);
        findTeamMemberByUserIdAndProjectId(userId, projectId);

        return amazonS3Service.downloadFile(resource.getKey());
    }

    @Override
    @Transactional
    public void deleteFile(long userId, long projectId, long resourceId) {

        Resource resource = findResourceById(resourceId);
        TeamMember teamMember = findTeamMemberByUserIdAndProjectId(userId, projectId);

        projectResourceValidator.validateDeletePermission(teamMember, resource);

        amazonS3Service.deleteFile(resource.getKey());

        Project project = resource.getProject();
        project.setStorageSize(project.getStorageSize().subtract(resource.getSize()));

        resource.setKey(null);
        resource.setSize(null);
        resource.setStatus(ResourceStatus.DELETED);
        resource.setUpdatedBy(teamMember);

        resourceRepository.save(resource);
    }

    private Resource findResourceById(long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Resource with id=" + id + " not found"));
    }

    private TeamMember findTeamMemberByUserIdAndProjectId(long userId, long projectId) {
        return teamMemberRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new NotFoundException("TeamMember with userId=" + userId + " and projectId= " + projectId + " not found"));
    }

    private Resource buildResource(String key, MultipartFile file, TeamMember creator) {

        return Resource.builder()
                .key(key)
                .name(file.getName())
                .size(BigInteger.valueOf(file.getSize()))
                .allowedRoles(creator.getRoles())
                .type(ResourceType.valueOf(file.getContentType()))
                .status(ResourceStatus.ACTIVE)
                .createdBy(creator)
                .updatedBy(creator)
                .createdAt(LocalDateTime.now())
                .project(creator.getTeam().getProject())
                .build();
    }
}

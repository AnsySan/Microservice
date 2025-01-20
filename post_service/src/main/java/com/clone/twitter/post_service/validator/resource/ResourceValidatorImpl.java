package com.clone.twitter.post_service.validator.resource;

import com.clone.twitter.post_service.exception.DataValidationException;
import com.clone.twitter.post_service.exception.NotFoundException;
import com.clone.twitter.post_service.property.AmazonS3Properties;
import com.clone.twitter.post_service.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResourceValidatorImpl implements ResourceValidator {

    private final ResourceRepository resourceRepository;
    private final AmazonS3Properties amazonS3Properties;

    @Override
    @Transactional
    public void validateCountFilesPerPost(Long postId, int filesToAdd) {
        if (resourceRepository.countAllByPost_Id(postId) + filesToAdd > amazonS3Properties.getMaxFilesAmount()) {
            throw new DataValidationException(String.format("Max files per post = %s", amazonS3Properties.getMaxFilesAmount()));
        }
    }

    @Override
    @Transactional
    public void validateExistenceByKey(String key) {
        if (!resourceRepository.existsByKey(key)) {
            throw new NotFoundException(String.format("Resource with key $s not found", key));
        }
    }

    @Override
    public void validatePostAuthorAndResourceAuthor(Long postAuthorId, Long postProjectId, Long resourceUserId) {
        if (!postAuthorId.equals(resourceUserId) && !postProjectId.equals(resourceUserId)) {
            throw new NotFoundException("Mismatch postAuthorIdId and resourceUserId");
        }
    }
}

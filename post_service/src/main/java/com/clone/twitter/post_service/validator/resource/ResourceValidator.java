package com.clone.twitter.post_service.validator.resource;

public interface ResourceValidator {

    void validateCountFilesPerPost(Long postId, int filesToAdd);

    void validateExistenceByKey(String key);

    void validatePostAuthorAndResourceAuthor(Long postAuthorId, Long postProjectId, Long resourceUserId);
}

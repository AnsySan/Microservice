package com.clone.twitter.post_service.service.resource;

import com.clone.twitter.post_service.dto.resource.ResourceDto;
import com.clone.twitter.post_service.model.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface ResourceService {

    Resource findById(Long id);

    List<ResourceDto> create(Long postId, Long userId, List<MultipartFile> files);

    InputStream downloadResource(String key);

    void deleteFile(String key, Long userId);
}

package com.clone.twitter.post_service.mapper.resource;

import com.clone.twitter.post_service.dto.resource.ResourceDto;
import com.clone.twitter.post_service.model.Resource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

    ResourceDto toDto(Resource resource);
}

package com.clone.twitter.user_service.mapper.contact;

import com.clone.twitter.user_service.dto.contact.ContactPreferenceDto;
import com.clone.twitter.user_service.entity.contact.ContactPreference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContactPreferenceMapper {

    @Mapping(source = "userId", target = "user.id")
    ContactPreference toEntity(ContactPreferenceDto contactPreferenceDto);

    @Mapping(source = "user.id", target = "userId")
    ContactPreferenceDto toDto(ContactPreference contact);
}

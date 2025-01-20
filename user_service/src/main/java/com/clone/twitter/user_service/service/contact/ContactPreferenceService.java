package com.clone.twitter.user_service.service.contact;


import com.clone.twitter.user_service.dto.contact.ContactPreferenceDto;

public interface ContactPreferenceService {

    ContactPreferenceDto getContact(String username);
}

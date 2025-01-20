package com.clone.twitter.user_service.service.contact;

import com.clone.twitter.user_service.dto.contact.ContactPreferenceDto;
import com.clone.twitter.user_service.entity.contact.ContactPreference;
import com.clone.twitter.user_service.exception.NotFoundException;
import com.clone.twitter.user_service.mapper.contact.ContactPreferenceMapper;
import com.clone.twitter.user_service.repository.contact.ContactPreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactPreferenceServiceImpl implements ContactPreferenceService {

    private final ContactPreferenceRepository contactRepository;
    private final ContactPreferenceMapper contactPreferenceMapper;

    @Override
    @Transactional(readOnly = true)
    public ContactPreferenceDto getContact(String username) {
        ContactPreference contactPreference = contactRepository.findByUserUsername(username)
                .orElseThrow(() -> new NotFoundException("Contact with username " + username + " not found"));

        return contactPreferenceMapper.toDto(contactPreference);
    }
}

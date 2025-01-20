package com.clone.twitter.user_service.service.contact;

import com.clone.twitter.user_service.dto.contact.ContactPreferenceDto;
import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.entity.contact.ContactPreference;
import com.clone.twitter.user_service.entity.contact.PreferredContact;
import com.clone.twitter.user_service.exception.NotFoundException;
import com.clone.twitter.user_service.mapper.contact.ContactPreferenceMapper;
import com.clone.twitter.user_service.repository.contact.ContactPreferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactPreferenceServiceImplTest {

    @Mock
    private ContactPreferenceRepository contactRepository;
    @Mock
    private ContactPreferenceMapper contactPreferenceMapper;

    @InjectMocks
    private ContactPreferenceServiceImpl contactPreferenceService;

    private final String username = "username";
    private ContactPreferenceDto contactPreferenceDto;
    private ContactPreference contactPreference;

    @BeforeEach
    void setUp() {
        contactPreference = ContactPreference.builder()
                .id(1L)
                .preference(PreferredContact.TELEGRAM)
                .user(User.builder().id(2L).build())
                .build();

        contactPreferenceDto = ContactPreferenceDto.builder()
                .id(1L)
                .preference(PreferredContact.TELEGRAM)
                .userId(2L)
                .build();
    }

    @Test
    void getContact() {
        when(contactRepository.findByUserUsername(username)).thenReturn(Optional.of(contactPreference));
        when(contactPreferenceMapper.toDto(contactPreference)).thenReturn(contactPreferenceDto);

        ContactPreferenceDto actual = contactPreferenceService.getContact(username);
        assertEquals(contactPreferenceDto, actual);

        InOrder inOrder = inOrder(contactPreferenceMapper, contactRepository);
        inOrder.verify(contactRepository).findByUserUsername(username);
        inOrder.verify(contactPreferenceMapper).toDto(contactPreference);
    }

    @Test
    void getContactNotFoundException() {
        when(contactRepository.findByUserUsername(username)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> contactPreferenceService.getContact(username));
        assertEquals("Contact with username " + username + " not found", e.getMessage());
    }
}
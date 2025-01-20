package com.clone.twitter.user_service.controller.user;

import com.clone.twitter.user_service.config.context.UserContext;
import com.clone.twitter.user_service.config.context.UserHeaderFilter;
import com.clone.twitter.user_service.dto.user.UserDto;
import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.mapper.UserMapper;
import com.clone.twitter.user_service.service.avatar.ProfilePicService;
import com.clone.twitter.user_service.service.csv.CSVFileService;
import com.clone.twitter.user_service.service.csv.CsvFileConverter;
import com.clone.twitter.user_service.service.user.UserService;
import com.json.student.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private CSVFileService csvFileService;
    @Mock
    private CsvFileConverter converter;
    @Mock
    private ProfilePicService profilePicService;
    @Mock
    private UserContext userContext;
    @Mock
    private UserHeaderFilter userHeaderFilter;

    private User user;
    private UserDto userDto;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        user = User.builder().id(1L).build();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        userDto = UserDto.builder().username("name").phone("56891234").email("test@mail.ru").password("password").build();
    }

    @Test
    public void testConvertCsvFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "name,\nJohn".getBytes());
        List<Person> persons = Collections.singletonList(new Person());

        when(converter.convertCsvToPerson(any(MultipartFile.class))).thenReturn(persons);
        doNothing().when(csvFileService).convertCsvFile(anyList());

        mockMvc.perform(multipart("/users/add/file").file(file)).andExpect(status().isOk());
    }

    @Test
    public void testCreateUser() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);
        doNothing().when(profilePicService).generateAndSetPic(any(UserDto.class));
        String json = "{"
                      + "\n\"username\": \"" + userDto.getUsername() + "\","
                      + "\n\"email\": \"" + userDto.getEmail() + "\","
                      + "\n\"phone\": \"" + userDto.getPhone() + "\","
                      + "\n\"password\": \"" + userDto.getPassword() + "\"\n}";

        mockMvc.perform(post("/users/creature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(userDto.getUsername()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.phone").value(userDto.getPhone()))
                .andExpect(jsonPath("$.password").value(userDto.getPassword()));

        verify(userService, times(1)).createUser(userDto);
    }
}

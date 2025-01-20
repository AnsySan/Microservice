package com.clone.twitter.user_service.service.avatar;

import com.amazonaws.services.s3.model.S3Object;
import com.clone.twitter.user_service.dto.avatar.UserProfilePicDto;
import com.clone.twitter.user_service.dto.user.UserDto;
import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.entity.UserProfilePic;
import com.clone.twitter.user_service.exception.DataValidationException;
import com.clone.twitter.user_service.mapper.avatar.PictureMapper;
import com.clone.twitter.user_service.repository.UserRepository;
import com.clone.twitter.user_service.service.cloud.S3Service;
import com.clone.twitter.user_service.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfilePicServiceImplTest {
    @InjectMocks
    private ProfilePicServiceImpl profilePicService;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private S3Service s3Service;
    @Mock
    private RestTemplate restTemplate;
    @Spy
    private PictureMapper pictureMapper = Mappers.getMapper(PictureMapper.class);
    @Captor
    private ArgumentCaptor<String> forPictures;
    private User user;
    private UserDto userDto;
    private final String backetName = "user-bucket";

    private byte[] getImageBytes() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.RED);
        graphics.fill(new Rectangle2D.Double(0, 0, 50, 50));
        byte[] bytes;
        try {
            ImageIO.write(image, "jpg", outputStream);
            bytes = outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    @BeforeEach
    void init() {
        user = User.builder().id(1L).username("name").email("test@mail.ru").password("password").userProfilePic(new UserProfilePic("Big picture", "Small picture")).build();
        userDto = UserDto.builder().id(1L).username("name").email("test@mail.ru").password("password").build();
    }

    @Test
    public void testGenerateAndSetPicWithException(){
        when(restTemplate.getForObject(anyString(),eq(byte[].class))).thenReturn(null);
        var exception = assertThrows(DataValidationException.class, ()->profilePicService.generateAndSetPic(userDto));
        assertEquals(exception.getMessage(), "Failed to get the generated image");
    }

    @Test
    public void testGenerateAndSetPicWithSetting(){
        when(restTemplate.getForObject(anyString(),eq(byte[].class))).thenReturn(getImageBytes());
        doNothing().when(s3Service).uploadFile(anyString(), anyString(), any(InputStream.class));
        ReflectionTestUtils.setField(profilePicService, "smallSize", 170);
        ReflectionTestUtils.setField(profilePicService, "bucketName", backetName);

        profilePicService.generateAndSetPic(userDto);
        UserProfilePic generated = userDto.getUserProfilePic();
        assertNull(generated.getFileId());
        assertNotNull(generated.getSmallFileId());

        InOrder inorder = inOrder(restTemplate, s3Service);
        inorder.verify(restTemplate, times(1)).getForObject(anyString(),eq(byte[].class));
        inorder.verify(s3Service, times(1)).uploadFile(anyString(), anyString(), any(InputStream.class));
    }

    @Test
    public void testSaveProfilePicWithSaving() {
        when(userService.findUserById(user.getId())).thenReturn(user);
        MockMultipartFile file = new MockMultipartFile("file", "example.jpg", MediaType.IMAGE_JPEG_VALUE, getImageBytes());
        ReflectionTestUtils.setField(profilePicService, "smallSize", 170);
        ReflectionTestUtils.setField(profilePicService, "largeSize", 1080);
        ReflectionTestUtils.setField(profilePicService, "bucketName", backetName);

        UserProfilePicDto result = profilePicService.saveProfilePic(user.getId(), file);

        verify(userService, times(1)).findUserById(user.getId());
        verify(s3Service, times(2)).uploadFile(eq(backetName), forPictures.capture(), any(InputStream.class));
        var pictures = forPictures.getAllValues();
        String forSmallPicture = pictures.get(0);
        String forLargePicture = pictures.get(1);
        verify(userRepository, times(1)).save(user);
        verify(pictureMapper, times(1)).toDto(UserProfilePic.builder().smallFileId(forSmallPicture).fileId(forLargePicture).build());
        assertEquals(result.getFileId(), forLargePicture);
        assertEquals(result.getSmallFileId(), forSmallPicture);
    }

    @Test
    public void testGetProfilePic() {
        when(userService.findUserById(user.getId())).thenReturn(user);
        ReflectionTestUtils.setField(profilePicService, "bucketName", backetName);
        S3Object s3Object = new S3Object();
        s3Object.setObjectContent(new ByteArrayInputStream(getImageBytes()));
        when(s3Service.getFile(backetName, user.getUserProfilePic().getFileId())).thenReturn(s3Object);

        InputStreamResource result = profilePicService.getProfilePic(user.getId());
        verify(userService, times(1)).findUserById(user.getId());
        verify(s3Service, times(1)).getFile(backetName, user.getUserProfilePic().getFileId());
        assertNotNull(result);
    }

    @Test
    public void testDeleteProfilePic() {
        when(userService.findUserById(user.getId())).thenReturn(user);
        ReflectionTestUtils.setField(profilePicService, "bucketName", backetName);

        profilePicService.deleteProfilePic(user.getId());
        verify(userService, times(1)).findUserById(user.getId());
        verify(s3Service, times(2)).deleteFile(eq(backetName), anyString());
        verify(userRepository, times(1)).save(user);
        assertNull(user.getUserProfilePic().getFileId());
        assertNull(user.getUserProfilePic().getSmallFileId());
    }
}

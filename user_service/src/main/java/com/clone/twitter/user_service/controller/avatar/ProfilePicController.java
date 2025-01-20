package com.clone.twitter.user_service.controller.avatar;

import com.clone.twitter.user_service.dto.avatar.UserProfilePicDto;
import com.clone.twitter.user_service.service.avatar.ProfilePicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pic")
@Tag(name = "Avatar")
public class ProfilePicController {
    private final ProfilePicService profilePicService;

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save profile picture")
    public @Valid UserProfilePicDto saveProfilePic(@Positive @Parameter @PathVariable long userId,
                                                   @NotEmpty @RequestParam("file") MultipartFile file) {
        return profilePicService.saveProfilePic(userId, file);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get profile picture")
    public InputStreamResource getProfilePic(@Positive @Parameter @PathVariable long userId) {
        return profilePicService.getProfilePic(userId);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete profile picture")
    public UserProfilePicDto deleteProfilePic(@Positive @Parameter @PathVariable long userId) {
        return profilePicService.deleteProfilePic(userId);
    }
}

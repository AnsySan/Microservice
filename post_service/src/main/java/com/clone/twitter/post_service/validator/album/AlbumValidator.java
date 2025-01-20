package com.clone.twitter.post_service.validator.album;

import com.clone.twitter.post_service.client.UserServiceClient;
import com.clone.twitter.post_service.dto.user.UserDto;
import com.clone.twitter.post_service.exception.DataValidationException;
import com.clone.twitter.post_service.exception.NoAccessException;
import com.clone.twitter.post_service.model.Album;
import com.clone.twitter.post_service.model.AlbumVisibility;
import com.clone.twitter.post_service.model.Post;
import com.clone.twitter.post_service.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlbumValidator {

    private final AlbumRepository albumRepository;
    private final UserServiceClient userServiceClient;

    public void validateAlbumExistence(Album album, long userId) {
        boolean albumExists = checkAlbumExistenceInFavorites(album, userId);
        if (albumExists) {
            throw new DataValidationException(String.format("Album with id '%d' already in favorites", album.getId()));
        }
    }

    public void validateAlbumTitleIsUnique(long userId, String title) {
        List<Album> albums = albumRepository.findByAuthorId(userId).toList();
        boolean isTitleNotUnique = albums.stream()
                .anyMatch(existingAlbum -> existingAlbum.getTitle().equals(title));

        if (isTitleNotUnique) {
            throw new DataValidationException(
                    String.format("Album with this title '%s' already exists", title));
        }
    }

    public boolean validateAccess(Album album, long userId) {
        if (album.getVisibility() == AlbumVisibility.ONLY_AUTHOR && userId != album.getAuthorId()) {
            throw new NoAccessException("Only the author has access to the album with albumId = " + album.getId());
        }
        if (album.getVisibility() == AlbumVisibility.SELECTED_USERS && !album.getSelectedUserIds().contains(userId)) {
            throw new NoAccessException("Only author's selected users has access to the album with albumId = " + album.getId());
        }
        if (album.getVisibility() == AlbumVisibility.ONLY_SUBSCRIBERS) {
            List<Long> authorSubscribersIds = userServiceClient.getFollowers(album.getAuthorId()).stream()
                    .map(UserDto::getId)
                    .toList();
            if (!authorSubscribersIds.contains(userId)) {
                throw new NoAccessException("Only subscribers has access to the album with albumId = " + album.getId());
            }
        }
        return true;
    }

    public void checkPostExistenceInAlbum(Album album, long postId) {
        List<Post> posts = album.getPosts();
        boolean isPostAlreadyExistInAlbum = posts.stream()
                .anyMatch(post -> post.getId() == postId);

        if (isPostAlreadyExistInAlbum) {
            throw new DataValidationException(
                    String.format("Post with id '%d' already exists in album", postId));
        }
    }

    public boolean checkAlbumExistenceInFavorites(Album album, long userId) {
        return albumRepository.checkAlbumExistsInFavorites(album.getId(), userId);
    }

    public void validateUserIsAuthor(Album album, long userId) {
        if (album.getAuthorId() != userId) {
            throw new NoAccessException("Only the author can modify album with albumId = " + album.getId());
        }
    }
}

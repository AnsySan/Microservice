package com.clone.twitter.post_service.filter.album;

import com.clone.twitter.post_service.dto.album.AlbumFilterDto;
import com.clone.twitter.post_service.model.Album;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class TitleFilter implements AlbumFilter {

    @Override
    public boolean isAcceptable(AlbumFilterDto albumFilterDto) {
        return albumFilterDto.getTitlePattern() != null && !albumFilterDto.getTitlePattern().isEmpty();
    }

    @Override
    public Stream<Album> applyFilter(Stream<Album> albums, AlbumFilterDto albumFilterDto) {
        return albums.filter(album -> album.getTitle().startsWith(albumFilterDto.getTitlePattern()));
    }
}
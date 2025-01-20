package com.clone.twitter.post_service.filter.album;


import com.clone.twitter.post_service.dto.album.AlbumFilterDto;
import com.clone.twitter.post_service.model.Album;

import java.util.stream.Stream;

public interface AlbumFilter {

    public boolean isAcceptable(AlbumFilterDto albumFilterDto);

    public Stream<Album> applyFilter(Stream<Album> albums, AlbumFilterDto albumFilterDto);

}
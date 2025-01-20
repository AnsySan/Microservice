package com.clone.twitter.post_service.service.album.filter;


import com.clone.twitter.post_service.dto.album.AlbumFilterDto;
import com.clone.twitter.post_service.model.Album;

import java.util.stream.Stream;

public interface AlbumFilterService {
    Stream<Album> applyFilters(Stream<Album> albums, AlbumFilterDto filterDto);
}
package com.clone.twitter.post_service.filter.album;

import com.clone.twitter.post_service.dto.album.AlbumFilterDto;
import com.clone.twitter.post_service.model.Album;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Component
public class FromDateFilter implements AlbumFilter {

    @Override
    public boolean isAcceptable(AlbumFilterDto albumFilterDto) {
        return albumFilterDto.getFromDate() != null;
    }

    @Override
    public Stream<Album> applyFilter(Stream<Album> albums, AlbumFilterDto albumFilterDto) {
        LocalDate fromDate = LocalDate.parse(albumFilterDto.getFromDate(), DateTimeFormatter.ISO_DATE);
        return albums.filter(album -> isAfterOrEqual(album, fromDate));
    }

    private boolean isAfterOrEqual(Album album, LocalDate fromDate) {
        LocalDate albumDate = album.getCreatedAt().toLocalDate();
        return albumDate.isAfter(fromDate) || albumDate.isEqual(fromDate);
    }
}
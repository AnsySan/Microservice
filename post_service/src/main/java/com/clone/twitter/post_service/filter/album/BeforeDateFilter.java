package com.clone.twitter.post_service.filter.album;

import com.clone.twitter.post_service.dto.album.AlbumFilterDto;
import com.clone.twitter.post_service.model.Album;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Component
public class BeforeDateFilter implements AlbumFilter {

    @Override
    public boolean isAcceptable(AlbumFilterDto albumFilterDto) {
        return albumFilterDto.getBeforeDate() != null;
    }

    @Override
    public Stream<Album> applyFilter(Stream<Album> albums, AlbumFilterDto albumFilterDto) {
        LocalDate beforeDate = LocalDate.parse(albumFilterDto.getBeforeDate(), DateTimeFormatter.ISO_DATE);
        return albums.filter(album -> isBeforeOrEqual(album, beforeDate));
    }

    private boolean isBeforeOrEqual(Album album, LocalDate beforeDate) {
        LocalDate albumDate = album.getCreatedAt().toLocalDate();
        return albumDate.isBefore(beforeDate) || albumDate.isEqual(beforeDate);
    }
}
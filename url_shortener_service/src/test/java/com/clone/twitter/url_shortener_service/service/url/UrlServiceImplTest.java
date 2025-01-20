package com.clone.twitter.url_shortener_service.service.url;

import com.clone.twitter.url_shortener_service.dto.Request;
import com.clone.twitter.url_shortener_service.dto.Response;
import com.clone.twitter.url_shortener_service.entity.Url;
import com.clone.twitter.url_shortener_service.exception.NotFoundException;
import com.clone.twitter.url_shortener_service.repository.UrlCacheRepository;
import com.clone.twitter.url_shortener_service.repository.UrlRepository;
import com.clone.twitter.url_shortener_service.service.cache.HashCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlServiceImplTest {

    @Mock
    private UrlCacheRepository urlCacheRepository;

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private HashCache hashCache;

    @InjectMocks
    private UrlServiceImpl urlService;

    @Test
    public void testGetRedirectViewUrlFoundInCache() {
        String hash = "abc";
        String originalUrl = "https://example.com";

        when(urlCacheRepository.getUrlByHash(anyString())).thenReturn(Optional.of(originalUrl));

        RedirectView redirectView = urlService.getRedirectView(hash);

        assertEquals(originalUrl, redirectView.getUrl());
    }

    @Test
    public void testGetRedirectViewUrlFoundInDb() {
        String hash = "abc";
        String originalUrl = "https://example.com";
        Url urlEntity = new Url();
        urlEntity.setUrl(originalUrl);

        when(urlCacheRepository.getUrlByHash(anyString())).thenReturn(Optional.empty());
        when(urlRepository.getUrlByHash(anyString())).thenReturn(Optional.of(urlEntity));

        RedirectView redirectView = urlService.getRedirectView(hash);

        assertEquals(originalUrl, redirectView.getUrl());
    }

    @Test
    public void testGetRedirectViewUrlNotFound() {
        String hash = "abc";

        when(urlCacheRepository.getUrlByHash(anyString())).thenReturn(Optional.empty());
        when(urlRepository.getUrlByHash(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> urlService.getRedirectView(hash));
    }

    @Test
    public void testCreateShortUrl() {
        String hash = "abc";
        String originalUrl = "https://example.com";
        Request dto = new Request(originalUrl);
        Response response = new Response(hash);

        when(hashCache.getHash()).thenReturn(hash);

        Response result = urlService.createShortUrl(dto);
        assertEquals(response.getHash(), result.getHash());
    }
}
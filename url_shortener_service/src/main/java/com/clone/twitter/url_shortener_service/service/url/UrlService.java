package com.clone.twitter.url_shortener_service.service.url;

import com.clone.twitter.url_shortener_service.dto.Request;
import com.clone.twitter.url_shortener_service.dto.Response;
import org.springframework.web.servlet.view.RedirectView;

public interface UrlService {

    RedirectView getRedirectView(String hash);

    Response createShortUrl(Request dto);
}

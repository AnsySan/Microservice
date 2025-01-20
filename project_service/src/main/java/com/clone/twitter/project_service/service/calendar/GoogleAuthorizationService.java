package com.clone.twitter.project_service.service.calendar;

import com.clone.twitter.project_service.model.Project;
import com.clone.twitter.project_service.model.calendar.CalendarToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;

import java.net.URL;

public interface GoogleAuthorizationService {
    Credential generateCredential(CalendarToken calendarToken);

    void refreshToken(CalendarToken calendarToken, Credential credential);

    CalendarToken authorizeProject(Project project, String code);

    TokenResponse requestToken(String code);

    URL getAuthUrl();
}

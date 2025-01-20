package com.clone.twitter.project_service.service.calendar;

import com.clone.twitter.project_service.dto.calendar.AclDto;
import com.clone.twitter.project_service.dto.calendar.CalendarDto;
import com.clone.twitter.project_service.dto.calendar.CalendarEventDto;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.calendar.Calendar;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;

@Component
public interface CalendarService {

    URL getAccessCode();

    Credential auth(long projectId, String code);

    CalendarEventDto createEvent(long projectId, String calendarId, CalendarEventDto eventDto);

    CalendarDto getCalendar(long projectId, String calendarId);

    List<CalendarEventDto> getEvents(long projectId, String calendarId);

    void deleteEvent(long projectId, String calendarId, long eventId);

    CalendarDto createCalendar(long projectId, CalendarDto calendarDto);

    AclDto createAcl(long projectId, String calendarId, AclDto aclDto);

    List<AclDto> listAcl(long projectId, String calendarId);

    void deleteAcl(long projectId, String calendarId, long aclId);

    Calendar buildCalendar(long projectId);
}

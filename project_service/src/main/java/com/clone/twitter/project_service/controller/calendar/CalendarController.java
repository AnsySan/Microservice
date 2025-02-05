package com.clone.twitter.project_service.controller.calendar;

import com.clone.twitter.project_service.dto.calendar.AclDto;
import com.clone.twitter.project_service.dto.calendar.CalendarDto;
import com.clone.twitter.project_service.dto.calendar.CalendarEventDto;
import com.clone.twitter.project_service.service.calendar.CalendarService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Validated
@RestController
@RequestMapping("projects")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;


    @GetMapping("calendars/auth")
    public void getAccessCode(HttpServletResponse response) throws IOException {
        URL url = calendarService.getAccessCode();
        response.sendRedirect(url.toString());
    }

    // Эндпоит getGoogleCalenderToken перекидывает пользователя на страничку для разрешения доступа к Google Calendar,
    // после подтверждения пользователя перенаправляет на страницу
    // http://localhost:8888/Callback?code=КОД_ДОСТУПА&scope=... Нужно достать код и после этого вызвать
    // эндпоит setCredentialsForProject для того, чтобы разрешить проекту доступ

    @PostMapping("{projectId}/calendars/auth")
    public String setProjectCredentials(@PathVariable long projectId, @NotBlank @RequestParam String code) {
        calendarService.auth(projectId, code);
        return "credential.getAccessToken().toString()";
    }

    //На один гугл аккаунт, можно выставить один проект

    @PostMapping("{projectId}/calendars/events")
    public CalendarEventDto createEvent(@PathVariable long projectId,
                                        @RequestParam String calendarId,
                                        @Valid @RequestBody CalendarEventDto eventDto) {
        eventDto.verifyEndIsAfterStartTime();
        return calendarService.createEvent(projectId, calendarId, eventDto);
    }

    @GetMapping("{projectId}/calendars/events")
    public List<CalendarEventDto> getEvents(@PathVariable long projectId, @RequestParam String calendarId) {
        return calendarService.getEvents(projectId, calendarId);
    }

    @DeleteMapping("{projectId}/calendars/events/{eventId}")
    public void deleteEvent(@PathVariable long projectId,
                            @RequestParam String calendarId,
                            @PathVariable long eventId) {
        calendarService.deleteEvent(projectId, calendarId, eventId);
    }

    @PostMapping("{projectId}/calendars")
    public CalendarDto createCalendar(@PathVariable long projectId, @Valid @RequestBody CalendarDto calendarDto) {
        return calendarService.createCalendar(projectId, calendarDto);
    }

    @GetMapping("{projectId}/calendars")
    public CalendarDto getCalendar(@PathVariable long projectId, @RequestParam String calendarId) {
        return calendarService.getCalendar(projectId, calendarId);
    }

    @PostMapping("{projectId}/calendars/acl")
    public AclDto createAclRule(@PathVariable long projectId,
                                @RequestParam String calendarId,
                                @Valid @RequestBody AclDto aclDto) {
        return calendarService.createAcl(projectId, calendarId, aclDto);
    }

    @GetMapping("{projectId}/calendars/acl")
    public List<AclDto> listAclRules(@PathVariable long projectId,
                                     @RequestParam String calendarId) {
        return calendarService.listAcl(projectId, calendarId);
    }

    @DeleteMapping("{projectId}/calendars/acl")
    public void deleteAclRule(@PathVariable long projectId,
                              @RequestParam String calendarId,
                              @RequestParam long aclId) {
        calendarService.deleteAcl(projectId, calendarId, aclId);
    }
}

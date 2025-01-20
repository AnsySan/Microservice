package com.clone.twitter.project_service.mapper.calendar;

import com.clone.twitter.project_service.dto.calendar.CalendarDto;
import com.google.api.services.calendar.model.Calendar;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CalendarMapper {
    Calendar toModel(CalendarDto calendarDto);

    CalendarDto toDto(Calendar calendar);

    List<CalendarDto> toDtos(List<Calendar> calendars);
}

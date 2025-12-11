package com.Eonline.Education.Service;

import com.Eonline.Education.Request.CalendarEventRequest;
import com.Eonline.Education.modals.CalendarEvent;
import org.springframework.http.ResponseEntity;

public interface CalendarEventService {
    ResponseEntity<?> getAllEvents(String email);
    CalendarEvent addEvent(CalendarEventRequest event, String email);
    void deleteEvent(String email);
}
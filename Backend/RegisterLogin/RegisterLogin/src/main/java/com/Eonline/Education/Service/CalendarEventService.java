package com.Eonline.Education.Service;

import com.Eonline.Education.Request.CalendarEventRequest;
import com.Eonline.Education.modals.CalendarEvent;
import org.springframework.http.ResponseEntity;

public interface CalendarEventService {

    public ResponseEntity<?> getAllEvents(String Email);

    public CalendarEvent addEvent(CalendarEventRequest event , String Email);

    public void deleteEvent(String Email);




}
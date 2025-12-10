package com.Eonline.Education.Controller;

import com.Eonline.Education.Request.CalendarEventRequest;
import com.Eonline.Education.Service.CalendarEventService;
import com.Eonline.Education.modals.CalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/CalendarEvent")
public class CalendarEventController {

    @Autowired
    private CalendarEventService calendarEventService;

    //@Secured("ADMIN")

    @GetMapping("/{Email}")
    public ResponseEntity getEvents(@PathVariable String Email){

        return ResponseEntity.ok(calendarEventService.getAllEvents(Email));
    }

    @PostMapping("/{Email}")
    public ResponseEntity  createEvent(@PathVariable String Email, @RequestBody CalendarEventRequest calendarEventRequest){
        CalendarEvent newEvent = calendarEventService.addEvent( calendarEventRequest,Email);
        if(newEvent == null){
           return ResponseEntity.ok("Not created");
        }
        else {
           return ResponseEntity.ok("Event Created");
        }
    }

    @DeleteMapping("/{Email}")
    public void deleteEvent(@PathVariable String Email){
        calendarEventService.deleteEvent(Email);
    }


}

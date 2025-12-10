package com.Eonline.Education.Service;

import com.Eonline.Education.Request.CalendarEventRequest;
import com.Eonline.Education.modals.CalendarEvent;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.CalendarEventRepository;
import com.Eonline.Education.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CalendarEventServiceImplementation implements CalendarEventService {
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @Override
    public ResponseEntity<?> getAllEvents(String Email) {
        User userDetails = userRepository.findByEmail(Email);
        long userAccID = userDetails.getId();

        try{
            if(calendarEventRepository.existsByUserId(userAccID)){
                return ResponseEntity.ok(calendarEventRepository.findByUserId(userAccID));
            }
            else{
                throw new RuntimeException("No events are there for this user");
            }
        }
        catch (Exception e){
            return ResponseEntity.ok(e);
        }

    }

    @Override
    public CalendarEvent addEvent(CalendarEventRequest event , String Email) {
        User userDetails = userRepository.findByEmail(Email);
        long userAccID = userDetails.getId();
        if (!calendarEventRepository.existsByUserId(userAccID)) {
            CalendarEvent calendarEvent = new CalendarEvent();
            calendarEvent.setUser(userDetails);
            calendarEvent.setTitle(event.getTitle());
            calendarEvent.setStartTime(event.getStartTime());
            calendarEvent.setEndTime(event.getEndTime());
            calendarEvent.setDate(event.getDate());
            calendarEventRepository.save(calendarEvent);

        }
        else{
            CalendarEvent calendarEvent =calendarEventRepository.findByUserId(userAccID);
            calendarEvent.setTitle(event.getTitle());
            calendarEvent.setStartTime(event.getStartTime());
            calendarEvent.setEndTime(event.getEndTime());
            calendarEvent.setDate(event.getDate());
            calendarEventRepository.save(calendarEvent);

        }
        return calendarEventRepository.findByUserId(userAccID);
    }

    @Override
    public void deleteEvent(String Email) {
        User userDetails = userRepository.findByEmail(Email);
        long userAccID = userDetails.getId();
        calendarEventRepository.deleteByUserId(userAccID);

    }
}

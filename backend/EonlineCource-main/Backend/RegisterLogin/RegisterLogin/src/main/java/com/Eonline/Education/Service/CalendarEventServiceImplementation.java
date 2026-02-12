package com.Eonline.Education.Service;

import com.Eonline.Education.Request.CalendarEventRequest;
import com.Eonline.Education.modals.CalendarEvent;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.CalendarEventRepository;
import com.Eonline.Education.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CalendarEventServiceImplementation implements CalendarEventService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @Override
    public ResponseEntity<?> getAllEvents(String email) {
        try {
            // Get user from repository - FIXED: Use orElseThrow to get User from Optional
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

            long userId = user.getId();

            // Check if event exists for this user
            if (calendarEventRepository.existsByUserId(userId)) {
                CalendarEvent event = calendarEventRepository.findByUserId(userId);



                return ResponseEntity.ok(event);
            } else {
                return ResponseEntity.ok("No events found for user: " + email);
            }

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    @Override
    public CalendarEvent addEvent(CalendarEventRequest event, String email) {
        // Get user from repository - FIXED: Use orElseThrow to get User from Optional
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        long userId = user.getId();
        CalendarEvent calendarEvent;

        // Check if event already exists for this user
        Optional<CalendarEvent> existingEventOpt = Optional.ofNullable(calendarEventRepository.findByUserId(userId));

        if (existingEventOpt.isPresent()) {
            // Update existing event
            calendarEvent = existingEventOpt.get();
        } else {
            // Create new event
            calendarEvent = new CalendarEvent();
            calendarEvent.setUser(user);
        }

        // Set event details
        calendarEvent.setTitle(event.getTitle());
        calendarEvent.setDate(event.getDate());
        calendarEvent.setStartTime(event.getStartTime());
        calendarEvent.setEndTime(event.getEndTime());

        // Set optional fields
        if (event.getMeetingLink() != null && !event.getMeetingLink().isEmpty()) {
            calendarEvent.setMeetingLink(event.getMeetingLink());
        }

        // Set admin email if provided
        if (event.getUserEmail() != null && !event.getUserEmail().isEmpty()) {
            calendarEvent.setAdminEmail(event.getUserEmail());
        }

        // Save and return the event
        return calendarEventRepository.save(calendarEvent);
    }

    @Override
    public void deleteEvent(String email) {
        // Get user from repository - FIXED: Use orElseThrow to get User from Optional
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        long userId = user.getId();

        // Check if event exists before deleting
        if (calendarEventRepository.existsByUserId(userId)) {
            calendarEventRepository.deleteByUserId(userId);
        } else {
            throw new RuntimeException("No events found for user: " + email);
        }
    }
}
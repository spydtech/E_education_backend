package com.Eonline.Education.Controller;

import com.Eonline.Education.Configuration.JwtTokenProvider;
import com.Eonline.Education.Request.TicketRequest;
import com.Eonline.Education.Service.TicketService;
import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.UserRepository;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.response.TicketResponse;
import com.Eonline.Education.user.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-support")
public class TicketController {

    @Autowired
    TicketService chatSupportService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    // User: Create ticket
    @PostMapping("/create/chatSupport")
    public ApiResponse createChatSupport(@RequestBody TicketRequest chatSupport) {
        return chatSupportService.createChatSupport(chatSupport);
    }

    // User: Get all tickets of user
    @GetMapping("/getAllTickets/by-user")
    public List<TicketResponse> getAllTicketByUser(@RequestHeader("Authorization") String jwt) {
        return chatSupportService.getAllTicketByUser(jwt);
    }

    // User/Admin: View ticket by ticket number
    @GetMapping("/getByTicket/{ticketNo}")
    public TicketResponse getByTicket(@RequestParam String ticketNo) {
        return chatSupportService.getByTicket(ticketNo);
    }

    // Admin: Update ticket
    @PutMapping("/update/{ticketNo}")
    public ResponseEntity<?> ticketUpdate(@RequestHeader("Authorization") String jwt,
                                          @PathVariable String ticketNo,
                                          @RequestBody TicketRequest chatSupport) {
        try {
            String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
            // ✅ FIXED: Handle Optional properly
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

            if (user.getRole().equalsIgnoreCase("ADMIN")) {
                return ResponseEntity.ok(chatSupportService.ticketUpdate(ticketNo, chatSupport));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Access denied: Invalid token or unauthorized access for role: " + user.getRole());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    // Admin: Get all tickets
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String jwt) {
        try {
            String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
            // ✅ FIXED: Handle Optional properly
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

            if (user.getRole().equalsIgnoreCase("ADMIN")) {
                return ResponseEntity.ok(chatSupportService.getAll());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Access denied: Invalid token or unauthorized access for role: " + user.getRole());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    // Admin: Change ticket status
    @PutMapping("/status/change")
    public ResponseEntity<?> statusUpdate(@RequestHeader("Authorization") String jwt,
                                          @RequestParam String ticketNo,
                                          @RequestParam TicketStatus ticketStatus) {
        try {
            String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
            // ✅ FIXED: Handle Optional properly
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

            if (user.getRole().equalsIgnoreCase("ADMIN")) {
                return ResponseEntity.ok(chatSupportService.statusUpdate(ticketNo, ticketStatus));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Access denied: Invalid token or unauthorized access for role: " + user.getRole());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }
}
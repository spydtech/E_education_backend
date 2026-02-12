package com.Eonline.Education.Request;

import com.Eonline.Education.modals.Employee;
import com.Eonline.Education.user.Channel;
import com.Eonline.Education.user.Priority;
import com.Eonline.Education.user.TicketStatus;
import com.Eonline.Education.user.TicketType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {
    private String userName;
    private String emailId;
    private String contactNumber;
    private TicketType requestTicketType;
    private String ticketBody;
    private String ticketNo;
    private LocalDate ticketRaisedOn;
    private LocalDate completionDate;
    private String assignTo;
    private TicketStatus status;
    private Priority priority;
    private Channel channel;

}

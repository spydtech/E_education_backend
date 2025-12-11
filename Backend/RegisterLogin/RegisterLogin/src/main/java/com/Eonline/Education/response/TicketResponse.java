package com.Eonline.Education.response;

import com.Eonline.Education.modals.Employee;
import com.Eonline.Education.user.Channel;
import com.Eonline.Education.user.Priority;
import com.Eonline.Education.user.TicketStatus;
import com.Eonline.Education.user.TicketType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TicketResponse {
    private Long  id;
    private String userName;
    private String emailId;
    private String userId;
    private String contactNumber;
    private TicketType requestTicketType;
    private String ticketBody;
    private String ticketNo;
    private LocalDate ticketRaisedOn;
    private LocalDate completionDate;
    private String employeeName;
    private String employeeEmail;
    private String employeeId;
    private TicketStatus status;
    private Priority priority;
    private Channel channel;
}

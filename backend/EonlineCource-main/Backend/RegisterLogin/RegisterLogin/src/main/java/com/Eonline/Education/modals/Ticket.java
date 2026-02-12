package com.Eonline.Education.modals;

import com.Eonline.Education.user.Channel;
import com.Eonline.Education.user.Priority;
import com.Eonline.Education.user.TicketStatus;
import com.Eonline.Education.user.TicketType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String userName;
        private String emailId;
        private String contactNumber;
        @Enumerated(EnumType.STRING)
        private TicketType requestTicketType;
        private String ticketBody;
        private String ticketNo;
        private LocalDate ticketRaisedOn;
        @Column(name = "completionDate", nullable = true)
        private LocalDate completionDate;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "assigned_to_id", nullable = true)
        private Employee assignTo;
        @Enumerated(EnumType.STRING)
        @Column(nullable = true)
        private TicketStatus status;
        @Enumerated(EnumType.STRING)
        @Column(nullable = true)
        private Priority priority;
        @Enumerated(EnumType.STRING)
        @Column(nullable = true)
        private Channel channel;


}

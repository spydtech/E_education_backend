package com.Eonline.Education.repository;

import com.Eonline.Education.modals.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {



    Ticket findByTicketNo(String ticketNo);

    List<Ticket> findAllByEmailId(String userEmail);
}

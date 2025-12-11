package com.Eonline.Education.Service;

import com.Eonline.Education.Request.TicketRequest;
import com.Eonline.Education.response.ApiResponse;
import com.Eonline.Education.response.TicketResponse;
import com.Eonline.Education.user.TicketStatus;

import java.util.List;

public interface TicketService {
    ApiResponse createChatSupport(TicketRequest chatSupport);
    public List<TicketResponse> getAllTicketByUser(String jwt);
    TicketResponse getByTicket(String ticketNo);



    TicketResponse ticketUpdate(String ticketNo,TicketRequest chatSupport);

    List<TicketResponse> getAll();

    TicketResponse statusUpdate(String ticketNo, TicketStatus ticketStatus);

}

package com.example.tickets;

import java.util.Collections;

/**
 * Service layer that creates tickets in an immutable fashion.
 */
public class TicketService {

    public IncidentTicket createTicket(String id, String reporterEmail, String title) {
        // Use the builder to create the ticket
        // All validation happens in .build()
        return IncidentTicket.builder()
                .id(id)
                .reporterEmail(reporterEmail)
                .title(title)
                .priority("MEDIUM")
                .source("CLI")
                .customerVisible(false)
                .tags(Collections.singletonList("NEW"))
                .build();
    }

    public IncidentTicket escalateToCritical(IncidentTicket t) {
        // Return a NEW instance with updated priority and tags
        return t.toBuilder()
                .priority("CRITICAL")
                .addTag("ESCALATED")
                .build();
    }

    public IncidentTicket assign(IncidentTicket t, String assigneeEmail) {
        // Return a NEW instance with updated assignee
        return t.toBuilder()
                .assigneeEmail(assigneeEmail)
                .build();
    }
}

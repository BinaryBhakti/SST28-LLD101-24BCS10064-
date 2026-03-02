import com.example.tickets.IncidentTicket;
import com.example.tickets.TicketService;

import java.util.List;

/**
 * Updated demo showing immutability and new update pattern.
 */
public class TryIt {

    public static void main(String[] args) {
        TicketService service = new TicketService();

        // 1. Create a ticket
        IncidentTicket t1 = service.createTicket("TCK-1001", "reporter@example.com", "Payment failing on checkout");
        System.out.println("Created (t1): " + t1);

        // 2. Demonstrate "updates" returning new objects
        IncidentTicket t2 = service.assign(t1, "agent@example.com");
        IncidentTicket t3 = service.escalateToCritical(t2);

        System.out.println("\nAfter assignments (t2): " + t2);
        System.out.println("After escalation  (t3): " + t3);
        System.out.println("Original t1 remains: " + t1);

        // 3. Demonstrate immutability of tags
        try {
            t3.getTags().add("HACKED");
        } catch (UnsupportedOperationException e) {
            System.out.println("\nSuccessfully blocked tag mutation: " + e);
        }

        // 4. Demonstrate centralized validation (should throw exception)
        try {
            IncidentTicket.builder()
                    .id("INVALID-ID-X-Y-Z-123-456-789")
                    .build();
        } catch (IllegalArgumentException e) {
            System.out.println("\nBlocked invalid ticket creation: " + e.getMessage());
        }
    }
}

import java.time.Duration;
import java.time.LocalDateTime;

class Bill {
    private ParkingTicket ticket;
    private LocalDateTime exitTime;
    private double amount;

    public Bill(ParkingTicket ticket, LocalDateTime exitTime, double amount) {
        this.ticket = ticket;
        this.exitTime = exitTime;
        this.amount = amount;
    }

    public ParkingTicket getTicket() { return ticket; }
    public LocalDateTime getExitTime() { return exitTime; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        long hours = Duration.between(ticket.getEntryTime(), exitTime).toHours() + 1;
        return "Bill | Ticket: " + ticket.getTicketId()
                + " | " + ticket.getVehicle()
                + " | Slot Type: " + ticket.getSlot().getSlotType()
                + " | Duration: " + hours + "h"
                + " | Amount: Rs." + amount;
    }
}

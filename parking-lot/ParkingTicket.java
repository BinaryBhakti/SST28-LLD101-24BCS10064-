import java.time.LocalDateTime;
import java.util.UUID;

class ParkingTicket {
    private String ticketId;
    private Vehicle vehicle;
    private ParkingSlot slot;
    private LocalDateTime entryTime;
    private String entryGateId;

    public ParkingTicket(Vehicle vehicle, ParkingSlot slot, LocalDateTime entryTime, String entryGateId) {
        this.ticketId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.vehicle = vehicle;
        this.slot = slot;
        this.entryTime = entryTime;
        this.entryGateId = entryGateId;
    }

    public String getTicketId() { return ticketId; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSlot getSlot() { return slot; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public String getEntryGateId() { return entryGateId; }

    @Override
    public String toString() {
        return "Ticket[" + ticketId + "] | " + vehicle
                + " | Slot: " + slot.getSlotId()
                + " (" + slot.getSlotType() + ")"
                + " | Level: " + slot.getLevel()
                + " | Entry: " + entryTime
                + " | Gate: " + entryGateId;
    }
}

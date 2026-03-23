package com.example.parking;

import java.time.LocalDateTime;

public class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSlot slot;
    private final LocalDateTime inTime;
    private final String gateId;

    public ParkingTicket(String ticketId, Vehicle vehicle, ParkingSlot slot,
                         LocalDateTime inTime, String gateId) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.slot = slot;
        this.inTime = inTime;
        this.gateId = gateId;
    }

    public String getTicketId() { return ticketId; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSlot getSlot() { return slot; }
    public LocalDateTime getInTime() { return inTime; }
    public String getGateId() { return gateId; }

    @Override
    public String toString() {
        return "Ticket[" + ticketId + " | " + vehicle + " | " + slot + " | in=" + inTime + "]";
    }
}

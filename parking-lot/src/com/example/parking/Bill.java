package com.example.parking;

import java.time.LocalDateTime;

public class Bill {
    private final String billId;
    private final ParkingTicket ticket;
    private final LocalDateTime outTime;
    private final double amount;

    public Bill(String billId, ParkingTicket ticket, LocalDateTime outTime, double amount) {
        this.billId = billId;
        this.ticket = ticket;
        this.outTime = outTime;
        this.amount = amount;
    }

    public String getBillId() { return billId; }
    public ParkingTicket getTicket() { return ticket; }
    public LocalDateTime getOutTime() { return outTime; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return "Bill[" + billId + " | " + ticket.getTicketId()
                + " | slot=" + ticket.getSlot().getSlotType()
                + " | in=" + ticket.getInTime()
                + " | out=" + outTime
                + " | amount=Rs." + amount + "]";
    }
}

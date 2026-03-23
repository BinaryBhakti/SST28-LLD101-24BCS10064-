package com.example.parking;

import com.example.parking.strategy.PricingStrategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Central class with the 3 APIs:
 *  1. generateParkingTicket()
 *  2. generateBill()
 *  3. showStatus()
 */
public class ParkingLot {
    private final List<ParkingFloor> floors;
    private final List<Gate> gates;
    private final Map<String, Map<String, Integer>> distanceMap; // gateId -> (slotId -> distance)
    private final PricingStrategy pricingStrategy;
    private final AtomicInteger ticketCounter = new AtomicInteger(1);
    private final AtomicInteger billCounter = new AtomicInteger(1);

    public ParkingLot(List<ParkingFloor> floors, List<Gate> gates,
                      Map<String, Map<String, Integer>> distanceMap,
                      PricingStrategy pricingStrategy) {
        this.floors = floors;
        this.gates = gates;
        this.distanceMap = distanceMap;
        this.pricingStrategy = pricingStrategy;
    }

    // ==================== API 1: generateParkingTicket ====================

    public ParkingTicket generateParkingTicket(Vehicle vehicle, LocalDateTime inTime,
                                                SlotType slotType, String gateId) {
        if (!distanceMap.containsKey(gateId)) {
            throw new IllegalArgumentException("Unknown gate: " + gateId);
        }

        ParkingSlot nearest = findNearestAvailableSlot(slotType, gateId);
        if (nearest == null) {
            throw new RuntimeException("No available " + slotType + " slot found!");
        }

        // synchronized occupy() prevents double-assignment
        if (!nearest.occupy(vehicle)) {
            throw new RuntimeException("Slot " + nearest.getSlotId() + " was taken concurrently. Retry.");
        }

        String ticketId = "TKT-" + ticketCounter.getAndIncrement();
        ParkingTicket ticket = new ParkingTicket(ticketId, vehicle, nearest, inTime, gateId);
        System.out.println("Ticket generated: " + ticket);
        return ticket;
    }

    // ==================== API 2: generateBill ====================

    public Bill generateBill(ParkingTicket ticket, LocalDateTime outTime) {
        long durationMinutes = Duration.between(ticket.getInTime(), outTime).toMinutes();
        double amount = pricingStrategy.calculate(ticket.getSlot().getSlotType(), durationMinutes);

        // Release the slot
        ticket.getSlot().release();

        String billId = "BILL-" + billCounter.getAndIncrement();
        Bill bill = new Bill(billId, ticket, outTime, amount);
        System.out.println("Bill generated: " + bill);
        return bill;
    }

    // ==================== API 3: showStatus ====================

    public String showStatus() {
        int smallAvail = 0, mediumAvail = 0, largeAvail = 0;

        for (ParkingFloor floor : floors) {
            for (ParkingSlot slot : floor.getSlots()) {
                if (!slot.isOccupied()) {
                    switch (slot.getSlotType()) {
                        case SMALL:  smallAvail++;  break;
                        case MEDIUM: mediumAvail++; break;
                        case LARGE:  largeAvail++;  break;
                    }
                }
            }
        }

        String status = "Available → S: " + smallAvail + ", M: " + mediumAvail + ", L: " + largeAvail;
        System.out.println(status);
        return status;
    }

    // ==================== Nearest slot logic ====================

    private ParkingSlot findNearestAvailableSlot(SlotType slotType, String gateId) {
        Map<String, Integer> gateDistances = distanceMap.get(gateId);
        ParkingSlot nearest = null;
        int minDist = Integer.MAX_VALUE;

        for (ParkingFloor floor : floors) {
            for (ParkingSlot slot : floor.getSlots()) {
                if (slot.getSlotType() == slotType && !slot.isOccupied()) {
                    int dist = gateDistances.getOrDefault(slot.getSlotId(), Integer.MAX_VALUE);
                    if (dist < minDist) {
                        minDist = dist;
                        nearest = slot;
                    }
                }
            }
        }
        return nearest;
    }

    // ==================== Builder for easy construction ====================

    public static class Builder {
        private final List<ParkingFloor> floors = new ArrayList<>();
        private final List<Gate> gates = new ArrayList<>();
        private final Map<String, Map<String, Integer>> distanceMap = new HashMap<>();
        private PricingStrategy pricingStrategy;

        public Builder addFloor(ParkingFloor floor) {
            floors.add(floor);
            return this;
        }

        public Builder addGate(Gate gate) {
            gates.add(gate);
            distanceMap.put(gate.getGateId(), new HashMap<>());
            return this;
        }

        public Builder setDistance(String gateId, String slotId, int distance) {
            distanceMap.get(gateId).put(slotId, distance);
            return this;
        }

        public Builder pricingStrategy(PricingStrategy strategy) {
            this.pricingStrategy = strategy;
            return this;
        }

        public ParkingLot build() {
            return new ParkingLot(floors, gates, distanceMap, pricingStrategy);
        }
    }
}

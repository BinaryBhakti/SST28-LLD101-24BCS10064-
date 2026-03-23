package com.example.parking;

import com.example.parking.strategy.HourlyPricingStrategy;

import java.time.LocalDateTime;

public class App {

    public static void main(String[] args) {

        // ========== 1. Build Floors with Slots ==========

        // Floor 1: 2 Small, 2 Medium, 1 Large
        ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addSlot(new ParkingSlot("F1-S1", 1, SlotType.SMALL));
        floor1.addSlot(new ParkingSlot("F1-S2", 1, SlotType.SMALL));
        floor1.addSlot(new ParkingSlot("F1-M1", 1, SlotType.MEDIUM));
        floor1.addSlot(new ParkingSlot("F1-M2", 1, SlotType.MEDIUM));
        floor1.addSlot(new ParkingSlot("F1-L1", 1, SlotType.LARGE));

        // Floor 2: 1 Small, 2 Medium, 1 Large
        ParkingFloor floor2 = new ParkingFloor(2);
        floor2.addSlot(new ParkingSlot("F2-S1", 2, SlotType.SMALL));
        floor2.addSlot(new ParkingSlot("F2-M1", 2, SlotType.MEDIUM));
        floor2.addSlot(new ParkingSlot("F2-M2", 2, SlotType.MEDIUM));
        floor2.addSlot(new ParkingSlot("F2-L1", 2, SlotType.LARGE));

        // ========== 2. Build Gates ==========

        Gate gateA = new Gate("GATE-A");
        Gate gateB = new Gate("GATE-B");

        // ========== 3. Build ParkingLot with Distance Map ==========

        ParkingLot lot = new ParkingLot.Builder()
                .addFloor(floor1)
                .addFloor(floor2)
                .addGate(gateA)
                .addGate(gateB)
                // Distances from GATE-A (closer to Floor 1)
                .setDistance("GATE-A", "F1-S1", 10)
                .setDistance("GATE-A", "F1-S2", 20)
                .setDistance("GATE-A", "F1-M1", 15)
                .setDistance("GATE-A", "F1-M2", 25)
                .setDistance("GATE-A", "F1-L1", 30)
                .setDistance("GATE-A", "F2-S1", 50)
                .setDistance("GATE-A", "F2-M1", 55)
                .setDistance("GATE-A", "F2-M2", 60)
                .setDistance("GATE-A", "F2-L1", 65)
                // Distances from GATE-B (closer to Floor 2)
                .setDistance("GATE-B", "F1-S1", 60)
                .setDistance("GATE-B", "F1-S2", 55)
                .setDistance("GATE-B", "F1-M1", 50)
                .setDistance("GATE-B", "F1-M2", 45)
                .setDistance("GATE-B", "F1-L1", 40)
                .setDistance("GATE-B", "F2-S1", 15)
                .setDistance("GATE-B", "F2-M1", 10)
                .setDistance("GATE-B", "F2-M2", 20)
                .setDistance("GATE-B", "F2-L1", 25)
                .pricingStrategy(new HourlyPricingStrategy())
                .build();

        // ========== 4. Show initial status ==========

        System.out.println("=== Initial Status ===");
        lot.showStatus();  // S: 3, M: 4, L: 2

        // ========== 5. Generate Tickets ==========

        System.out.println("\n=== Parking Vehicles ===");

        // Car enters from GATE-A → nearest MEDIUM slot = F1-M1 (dist 15)
        LocalDateTime now = LocalDateTime.of(2026, 3, 23, 10, 0);
        Vehicle car1 = new Vehicle("KA-01-1234", "White", "Swift");
        ParkingTicket t1 = lot.generateParkingTicket(car1, now, SlotType.MEDIUM, "GATE-A");

        // 2-wheeler enters from GATE-A → nearest SMALL slot = F1-S1 (dist 10)
        Vehicle bike1 = new Vehicle("KA-02-5678", "Black", "Activa");
        ParkingTicket t2 = lot.generateParkingTicket(bike1, now, SlotType.SMALL, "GATE-A");

        // Bus enters from GATE-B → nearest LARGE slot = F2-L1 (dist 25)
        Vehicle bus1 = new Vehicle("KA-03-9999", "Yellow", "Volvo");
        ParkingTicket t3 = lot.generateParkingTicket(bus1, now, SlotType.LARGE, "GATE-B");

        // 2-wheeler requesting a MEDIUM slot (allowed — slot type != vehicle type)
        Vehicle bike2 = new Vehicle("KA-04-1111", "Red", "Pulsar");
        ParkingTicket t4 = lot.generateParkingTicket(bike2, now, SlotType.MEDIUM, "GATE-B");

        // ========== 6. Show status after parking ==========

        System.out.println("\n=== Status After Parking ===");
        lot.showStatus();

        // ========== 7. Generate Bills ==========

        System.out.println("\n=== Generating Bills ===");

        // Car leaves after 3 hours → MEDIUM slot → 3 * Rs.20 = Rs.60
        LocalDateTime outTime1 = now.plusHours(3);
        lot.generateBill(t1, outTime1);

        // Bike leaves after 90 min → SMALL slot → ceil(1.5) = 2hrs * Rs.10 = Rs.20
        LocalDateTime outTime2 = now.plusMinutes(90);
        lot.generateBill(t2, outTime2);

        // ========== 8. Show status after exits ==========

        System.out.println("\n=== Status After Exits ===");
        lot.showStatus();
    }
}

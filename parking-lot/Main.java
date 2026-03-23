import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ParkingLot lot = ParkingLot.getInstance();

        // Setup entry gates
        EntryGate gateA = new EntryGate("G1", "Gate A", 0);
        EntryGate gateB = new EntryGate("G2", "Gate B", 50);
        lot.addGate(gateA);
        lot.addGate(gateB);

        // Level 0: Ground floor — 5 small, 5 medium, 3 large
        List<ParkingSlot> level0 = new ArrayList<>();
        for (int i = 0; i < 5; i++) level0.add(new ParkingSlot("L0-S" + i, SlotType.SMALL,  0, i * 10));
        for (int i = 0; i < 5; i++) level0.add(new ParkingSlot("L0-M" + i, SlotType.MEDIUM, 0, 50 + i * 10));
        for (int i = 0; i < 3; i++) level0.add(new ParkingSlot("L0-L" + i, SlotType.LARGE,  0, 100 + i * 10));
        lot.addLevel(new ParkingLevel(0, level0));

        // Level 1: First floor — 5 small, 5 medium, 3 large
        List<ParkingSlot> level1 = new ArrayList<>();
        for (int i = 0; i < 5; i++) level1.add(new ParkingSlot("L1-S" + i, SlotType.SMALL,  1, i * 10));
        for (int i = 0; i < 5; i++) level1.add(new ParkingSlot("L1-M" + i, SlotType.MEDIUM, 1, 50 + i * 10));
        for (int i = 0; i < 3; i++) level1.add(new ParkingSlot("L1-L" + i, SlotType.LARGE,  1, 100 + i * 10));
        lot.addLevel(new ParkingLevel(1, level1));

        System.out.println("=== Initial Slot Availability ===");
        lot.status().forEach((type, count) -> System.out.println("  " + type + ": " + count));

        System.out.println("\n=== Parking Operations ===");

        Vehicle bike = new Vehicle("MH-12-AB-1234", VehicleType.TWO_WHEELER);
        Vehicle car  = new Vehicle("MH-14-CD-5678", VehicleType.CAR);
        Vehicle bus  = new Vehicle("MH-01-EF-9012", VehicleType.BUS);

        // Bike parks in SMALL from Gate A
        ParkingTicket t1 = lot.park(bike, LocalDateTime.of(2025, 1, 1, 10, 0), SlotType.SMALL, "G1");
        System.out.println("Parked: " + t1);

        // Car parks in MEDIUM from Gate B
        ParkingTicket t2 = lot.park(car, LocalDateTime.of(2025, 1, 1, 10, 30), SlotType.MEDIUM, "G2");
        System.out.println("Parked: " + t2);

        // Bus parks in LARGE from Gate A
        ParkingTicket t3 = lot.park(bus, LocalDateTime.of(2025, 1, 1, 11, 0), SlotType.LARGE, "G1");
        System.out.println("Parked: " + t3);

        // Bike with no small slots left should fall back to MEDIUM
        Vehicle bike2 = new Vehicle("MH-12-XY-9999", VehicleType.TWO_WHEELER);
        // Fill all small slots first
        for (int i = 1; i < 5; i++)
            lot.park(new Vehicle("BIKE-" + i, VehicleType.TWO_WHEELER),
                     LocalDateTime.of(2025, 1, 1, 11, 0), SlotType.SMALL, "G1");
        // L1 smalls
        for (int i = 0; i < 5; i++)
            lot.park(new Vehicle("BIKE-L1-" + i, VehicleType.TWO_WHEELER),
                     LocalDateTime.of(2025, 1, 1, 11, 0), SlotType.SMALL, "G1");
        ParkingTicket t4 = lot.park(bike2, LocalDateTime.of(2025, 1, 1, 11, 30), SlotType.SMALL, "G1");
        System.out.println("Bike with no SMALL left: " + t4);

        System.out.println("\n=== Slot Availability After Parking ===");
        lot.status().forEach((type, count) -> System.out.println("  " + type + ": " + count));

        System.out.println("\n=== Exit Operations ===");
        Bill b1 = lot.exit(t1, LocalDateTime.of(2025, 1, 1, 13, 0)); // 3h in SMALL = Rs.60
        System.out.println(b1);

        Bill b2 = lot.exit(t2, LocalDateTime.of(2025, 1, 1, 14, 30)); // 4h in MEDIUM = Rs.160
        System.out.println(b2);

        Bill b3 = lot.exit(t3, LocalDateTime.of(2025, 1, 1, 15, 0)); // 4h in LARGE = Rs.320
        System.out.println(b3);

        // Bike2 parked in MEDIUM — billed at MEDIUM rate
        Bill b4 = lot.exit(t4, LocalDateTime.of(2025, 1, 1, 13, 30)); // 2h in MEDIUM = Rs.80
        System.out.println(b4);

        System.out.println("\n=== Final Slot Availability ===");
        lot.status().forEach((type, count) -> System.out.println("  " + type + ": " + count));
    }
}

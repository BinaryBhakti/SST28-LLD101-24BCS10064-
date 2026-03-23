import java.time.*;
import java.util.*;
import java.util.stream.*;

// Singleton Pattern — one ParkingLot instance across the system
// DIP — depends on SlotAssignmentStrategy and PricingStrategy abstractions, not implementations
class ParkingLot {
    private static ParkingLot instance;

    private final List<ParkingLevel> levels;
    private final Map<String, EntryGate> gates;
    private final Map<String, ParkingTicket> activeTickets;
    private final SlotAssignmentStrategy assignmentStrategy;
    private final PricingStrategy pricingStrategy;

    private ParkingLot() {
        levels = new ArrayList<>();
        gates = new HashMap<>();
        activeTickets = new HashMap<>();
        assignmentStrategy = new NearestSlotAssignment();
        pricingStrategy = new HourlyPricing();
    }

    public static ParkingLot getInstance() {
        if (instance == null) {
            instance = new ParkingLot();
        }
        return instance;
    }

    public void addLevel(ParkingLevel level) { levels.add(level); }
    public void addGate(EntryGate gate) { gates.put(gate.getGateId(), gate); }

    // park(vehicleDetails, entryTime, requestedSlotType, entryGateID)
    public ParkingTicket park(Vehicle vehicle, LocalDateTime entryTime,
                               SlotType requestedSlotType, String entryGateId) {
        EntryGate gate = gates.get(entryGateId);
        if (gate == null) throw new IllegalArgumentException("Invalid gate ID: " + entryGateId);

        List<ParkingSlot> allSlots = levels.stream()
                .flatMap(l -> l.getSlots().stream())
                .collect(Collectors.toList());

        Optional<ParkingSlot> slot = assignmentStrategy.findSlot(
                allSlots, vehicle.getVehicleType(), requestedSlotType, gate);

        if (slot.isEmpty())
            throw new RuntimeException("No available compatible slot for " + vehicle.getVehicleType());

        slot.get().setOccupied(true);
        ParkingTicket ticket = new ParkingTicket(vehicle, slot.get(), entryTime, entryGateId);
        activeTickets.put(ticket.getTicketId(), ticket);
        return ticket;
    }

    // status() — available slots grouped by type
    public Map<SlotType, Long> status() {
        return levels.stream()
                .flatMap(l -> l.getSlots().stream())
                .filter(s -> !s.isOccupied())
                .collect(Collectors.groupingBy(ParkingSlot::getSlotType, Collectors.counting()));
    }

    // exit(parkingTicket, exitTime) — returns bill
    public Bill exit(ParkingTicket ticket, LocalDateTime exitTime) {
        if (!activeTickets.containsKey(ticket.getTicketId()))
            throw new IllegalArgumentException("Ticket not found: " + ticket.getTicketId());

        Duration duration = Duration.between(ticket.getEntryTime(), exitTime);
        double amount = pricingStrategy.calculateCharge(ticket.getSlot().getSlotType(), duration);

        ticket.getSlot().setOccupied(false);
        activeTickets.remove(ticket.getTicketId());

        return new Bill(ticket, exitTime, amount);
    }
}

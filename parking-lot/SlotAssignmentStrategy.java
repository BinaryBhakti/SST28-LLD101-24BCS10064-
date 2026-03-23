import java.util.*;

// Strategy Pattern — ISP: slot finding is separate from pricing
interface SlotAssignmentStrategy {
    Optional<ParkingSlot> findSlot(List<ParkingSlot> slots, VehicleType vehicleType,
                                   SlotType requestedType, EntryGate gate);
}

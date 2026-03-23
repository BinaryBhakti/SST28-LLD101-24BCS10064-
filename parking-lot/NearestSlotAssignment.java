import java.util.*;

class NearestSlotAssignment implements SlotAssignmentStrategy {

    @Override
    public Optional<ParkingSlot> findSlot(List<ParkingSlot> slots, VehicleType vehicleType,
                                           SlotType requestedType, EntryGate gate) {
        List<SlotType> compatible = vehicleType.getCompatibleSlots();

        // Start from requested type; if not in compatible list default to first compatible
        int startIndex = compatible.indexOf(requestedType);
        if (startIndex == -1) startIndex = 0;

        // Try from requested type upward (larger slots) to satisfy "park in larger if needed"
        for (int i = startIndex; i < compatible.size(); i++) {
            SlotType tryType = compatible.get(i);
            Optional<ParkingSlot> nearest = slots.stream()
                    .filter(s -> !s.isOccupied() && s.getSlotType() == tryType)
                    .min(Comparator.comparingInt(s -> s.distanceTo(gate)));
            if (nearest.isPresent()) return nearest;
        }
        return Optional.empty();
    }
}

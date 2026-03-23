import java.util.*;

enum VehicleType {
    TWO_WHEELER, CAR, BUS;

    // SRP: vehicle type knows which slots it is compatible with
    public List<SlotType> getCompatibleSlots() {
        switch (this) {
            case TWO_WHEELER: return Arrays.asList(SlotType.SMALL, SlotType.MEDIUM, SlotType.LARGE);
            case CAR:         return Arrays.asList(SlotType.MEDIUM, SlotType.LARGE);
            case BUS:         return Arrays.asList(SlotType.LARGE);
            default:          return Collections.emptyList();
        }
    }
}

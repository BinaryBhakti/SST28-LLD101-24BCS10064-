import java.util.*;

public class DefaultRoomPricing implements RoomPricing {
    private final Map<Integer, Double> rates = new HashMap<>();

    public DefaultRoomPricing() {
        rates.put(LegacyRoomTypes.SINGLE, 14000.0);
        rates.put(LegacyRoomTypes.DOUBLE, 15000.0);
        rates.put(LegacyRoomTypes.TRIPLE, 12000.0);
        rates.put(LegacyRoomTypes.DELUXE, 16000.0);
    }

    public double monthlyRate(int roomType) {
        return rates.getOrDefault(roomType, 16000.0);
    }
}

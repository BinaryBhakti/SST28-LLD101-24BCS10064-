import java.time.Duration;
import java.util.*;

class HourlyPricing implements PricingStrategy {
    private final Map<SlotType, Double> hourlyRates;

    public HourlyPricing() {
        hourlyRates = new EnumMap<>(SlotType.class);
        hourlyRates.put(SlotType.SMALL,  20.0);
        hourlyRates.put(SlotType.MEDIUM, 40.0);
        hourlyRates.put(SlotType.LARGE,  80.0);
    }

    @Override
    public double calculateCharge(SlotType slotType, Duration duration) {
        long hours = Math.max(1, duration.toHours() + (duration.toMinutesPart() > 0 ? 1 : 0));
        return hourlyRates.get(slotType) * hours;
    }
}

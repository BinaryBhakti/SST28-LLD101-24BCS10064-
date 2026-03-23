import java.time.Duration;

// Strategy Pattern — ISP: billing is separate from slot assignment
interface PricingStrategy {
    double calculateCharge(SlotType slotType, Duration duration);
}

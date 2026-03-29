// Strategy Pattern — different pricing models for different days/shows
interface PricingStrategy {
    int calculatePrice(SeatType seatType);
}

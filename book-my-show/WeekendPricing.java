class WeekendPricing implements PricingStrategy {
    // 20% premium on weekends
    @Override
    public int calculatePrice(SeatType seatType) {
        return (int)(seatType.getBasePrice() * 1.2);
    }
}

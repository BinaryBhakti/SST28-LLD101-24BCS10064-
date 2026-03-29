class WeekdayPricing implements PricingStrategy {
    // 10% discount on weekdays
    @Override
    public int calculatePrice(SeatType seatType) {
        return (int)(seatType.getBasePrice() * 0.9);
    }
}

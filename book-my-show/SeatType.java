enum SeatType {
    STANDARD(100),
    PREMIUM(150),
    RECLINER(250);

    private final int basePrice;

    SeatType(int basePrice) {
        this.basePrice = basePrice;
    }

    public int getBasePrice() { return basePrice; }
}

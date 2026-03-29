import java.time.LocalDateTime;

class ShowTiming {
    private String timingId;
    private Show show;
    private Theater theater;
    private Screen screen;
    private LocalDateTime startTime;
    private PricingStrategy pricingStrategy;

    public ShowTiming(String timingId, Show show, Theater theater, Screen screen, 
                      LocalDateTime startTime, PricingStrategy pricing) {
        this.timingId = timingId;
        this.show = show;
        this.theater = theater;
        this.screen = screen;
        this.startTime = startTime;
        this.pricingStrategy = pricing;
    }

    public String getTimingId() { return timingId; }
    public Show getShow() { return show; }
    public Theater getTheater() { return theater; }
    public Screen getScreen() { return screen; }
    public LocalDateTime getStartTime() { return startTime; }
    public PricingStrategy getPricingStrategy() { return pricingStrategy; }

    @Override
    public String toString() {
        return show.getMovieName() + " at " + theater.getTheaterName() + " on " + startTime;
    }
}

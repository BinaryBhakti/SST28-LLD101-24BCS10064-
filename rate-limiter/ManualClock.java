class ManualClock implements Clock {
    private long currentTimeMillis;

    public ManualClock(long startTimeMillis) {
        this.currentTimeMillis = startTimeMillis;
    }

    @Override
    public long currentTimeMillis() {
        return currentTimeMillis;
    }

    public void advanceMillis(long millis) {
        currentTimeMillis += millis;
    }

    public void advanceSeconds(long seconds) {
        advanceMillis(seconds * 1000L);
    }
}

import java.time.Duration;

class RateLimitRule {
    private final String ruleId;
    private final int maxRequests;
    private final Duration window;

    public RateLimitRule(String ruleId, int maxRequests, Duration window) {
        if (maxRequests <= 0) {
            throw new IllegalArgumentException("Max requests must be positive");
        }
        if (window.isZero() || window.isNegative()) {
            throw new IllegalArgumentException("Window must be positive");
        }

        this.ruleId = ruleId;
        this.maxRequests = maxRequests;
        this.window = window;
    }

    public String getRuleId() {
        return ruleId;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public Duration getWindow() {
        return window;
    }

    public long getWindowMillis() {
        return window.toMillis();
    }

    @Override
    public String toString() {
        return ruleId + ": " + maxRequests + " requests / " + window;
    }
}

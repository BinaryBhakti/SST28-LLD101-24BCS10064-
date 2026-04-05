class RateLimitDecision {
    private final boolean allowed;
    private final String key;
    private final RateLimitRule violatedRule;
    private final long retryAfterMillis;
    private final String message;

    private RateLimitDecision(boolean allowed, String key, RateLimitRule violatedRule,
                              long retryAfterMillis, String message) {
        this.allowed = allowed;
        this.key = key;
        this.violatedRule = violatedRule;
        this.retryAfterMillis = retryAfterMillis;
        this.message = message;
    }

    public static RateLimitDecision allowed(String key) {
        return new RateLimitDecision(true, key, null, 0L, "Allowed");
    }

    public static RateLimitDecision denied(String key, RateLimitRule violatedRule,
                                           long retryAfterMillis, String message) {
        return new RateLimitDecision(false, key, violatedRule, retryAfterMillis, message);
    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getKey() {
        return key;
    }

    public RateLimitRule getViolatedRule() {
        return violatedRule;
    }

    public long getRetryAfterMillis() {
        return retryAfterMillis;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        if (allowed) {
            return "RateLimitDecision(ALLOWED for key=" + key + ")";
        }

        return "RateLimitDecision(DENIED for key=" + key
                + ", rule=" + violatedRule.getRuleId()
                + ", retryAfterMs=" + retryAfterMillis
                + ", message=" + message + ")";
    }
}

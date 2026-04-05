class DefaultRateLimiter implements RateLimiter {
    private final RateLimitPolicy policy;
    private final RateLimitingAlgorithm algorithm;
    private final Clock clock;

    public DefaultRateLimiter(RateLimitPolicy policy, RateLimitingAlgorithm algorithm, Clock clock) {
        this.policy = policy;
        this.algorithm = algorithm;
        this.clock = clock;
    }

    @Override
    public RateLimitDecision allow(String key) {
        return algorithm.allow(key, policy.getRules(), clock.currentTimeMillis());
    }
}

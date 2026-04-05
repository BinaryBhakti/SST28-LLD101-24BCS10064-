interface RateLimiter {
    RateLimitDecision allow(String key);
}

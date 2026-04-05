import java.util.List;

interface RateLimitingAlgorithm {
    RateLimitDecision allow(String key, List<RateLimitRule> rules, long nowMillis);
}

import java.util.*;

class RateLimitPolicy {
    private final String policyId;
    private final List<RateLimitRule> rules;

    public RateLimitPolicy(String policyId, List<RateLimitRule> rules) {
        if (rules == null || rules.isEmpty()) {
            throw new IllegalArgumentException("At least one rate limit rule is required");
        }

        this.policyId = policyId;
        this.rules = Collections.unmodifiableList(new ArrayList<>(rules));
    }

    public String getPolicyId() {
        return policyId;
    }

    public List<RateLimitRule> getRules() {
        return rules;
    }
}

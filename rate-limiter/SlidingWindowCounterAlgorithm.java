import java.util.*;
import java.util.concurrent.*;

class SlidingWindowCounterAlgorithm implements RateLimitingAlgorithm {
    private final ConcurrentHashMap<String, Object> keyLocks;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, SlidingWindowState>> states;

    public SlidingWindowCounterAlgorithm() {
        this.keyLocks = new ConcurrentHashMap<>();
        this.states = new ConcurrentHashMap<>();
    }

    @Override
    public RateLimitDecision allow(String key, List<RateLimitRule> rules, long nowMillis) {
        Object lock = keyLocks.computeIfAbsent(key, ignored -> new Object());

        synchronized (lock) {
            ConcurrentHashMap<String, SlidingWindowState> perKeyStates =
                    states.computeIfAbsent(key, ignored -> new ConcurrentHashMap<>());

            for (RateLimitRule rule : rules) {
                SlidingWindowState state = perKeyStates.computeIfAbsent(rule.getRuleId(), ignored -> new SlidingWindowState());
                advanceWindow(state, rule, nowMillis);

                double estimatedCount = estimateCount(state, rule, nowMillis);
                if (estimatedCount + 1.0 > rule.getMaxRequests()) {
                    long retryAfterMillis = Math.max(1L, (state.currentWindowStartMillis + rule.getWindowMillis()) - nowMillis);
                    return RateLimitDecision.denied(
                            key,
                            rule,
                            retryAfterMillis,
                            "Sliding window limit exceeded"
                    );
                }
            }

            for (RateLimitRule rule : rules) {
                perKeyStates.get(rule.getRuleId()).currentCount++;
            }

            return RateLimitDecision.allowed(key);
        }
    }

    private void advanceWindow(SlidingWindowState state, RateLimitRule rule, long nowMillis) {
        long windowMillis = rule.getWindowMillis();
        long alignedWindowStart = (nowMillis / windowMillis) * windowMillis;

        if (!state.initialized) {
            state.initialized = true;
            state.currentWindowStartMillis = alignedWindowStart;
            state.currentCount = 0;
            state.previousCount = 0;
            return;
        }

        if (alignedWindowStart == state.currentWindowStartMillis) {
            return;
        }

        if (alignedWindowStart == state.currentWindowStartMillis + windowMillis) {
            state.previousCount = state.currentCount;
        } else {
            state.previousCount = 0;
        }

        state.currentCount = 0;
        state.currentWindowStartMillis = alignedWindowStart;
    }

    private double estimateCount(SlidingWindowState state, RateLimitRule rule, long nowMillis) {
        long windowMillis = rule.getWindowMillis();
        long elapsedInCurrentWindow = nowMillis - state.currentWindowStartMillis;
        double previousWindowWeight = (double) (windowMillis - elapsedInCurrentWindow) / windowMillis;
        previousWindowWeight = Math.max(0.0, Math.min(1.0, previousWindowWeight));

        return state.currentCount + (state.previousCount * previousWindowWeight);
    }

    private static class SlidingWindowState {
        private boolean initialized;
        private long currentWindowStartMillis;
        private int currentCount;
        private int previousCount;
    }
}

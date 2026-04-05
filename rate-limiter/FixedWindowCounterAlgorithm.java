import java.util.*;
import java.util.concurrent.*;

class FixedWindowCounterAlgorithm implements RateLimitingAlgorithm {
    private final ConcurrentHashMap<String, Object> keyLocks;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, FixedWindowState>> states;

    public FixedWindowCounterAlgorithm() {
        this.keyLocks = new ConcurrentHashMap<>();
        this.states = new ConcurrentHashMap<>();
    }

    @Override
    public RateLimitDecision allow(String key, List<RateLimitRule> rules, long nowMillis) {
        Object lock = keyLocks.computeIfAbsent(key, ignored -> new Object());

        synchronized (lock) {
            ConcurrentHashMap<String, FixedWindowState> perKeyStates =
                    states.computeIfAbsent(key, ignored -> new ConcurrentHashMap<>());

            for (RateLimitRule rule : rules) {
                FixedWindowState state = perKeyStates.computeIfAbsent(rule.getRuleId(), ignored -> new FixedWindowState());
                refreshWindow(state, rule, nowMillis);

                if (state.count >= rule.getMaxRequests()) {
                    long retryAfterMillis = Math.max(1L, (state.windowStartMillis + rule.getWindowMillis()) - nowMillis);
                    return RateLimitDecision.denied(
                            key,
                            rule,
                            retryAfterMillis,
                            "Fixed window limit exceeded"
                    );
                }
            }

            for (RateLimitRule rule : rules) {
                perKeyStates.get(rule.getRuleId()).count++;
            }

            return RateLimitDecision.allowed(key);
        }
    }

    private void refreshWindow(FixedWindowState state, RateLimitRule rule, long nowMillis) {
        long windowMillis = rule.getWindowMillis();
        long alignedWindowStart = (nowMillis / windowMillis) * windowMillis;

        if (state.windowStartMillis != alignedWindowStart) {
            state.windowStartMillis = alignedWindowStart;
            state.count = 0;
        }
    }

    private static class FixedWindowState {
        private long windowStartMillis;
        private int count;
    }
}

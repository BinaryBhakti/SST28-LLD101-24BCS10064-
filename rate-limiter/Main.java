import java.time.Duration;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        RateLimitPolicy tenantPolicy = new RateLimitPolicy(
                "tenant-external-usage",
                Arrays.asList(
                        new RateLimitRule("5-per-minute", 5, Duration.ofMinutes(1)),
                        new RateLimitRule("12-per-hour", 12, Duration.ofHours(1))
                )
        );

        System.out.println("========== Rate Limiter Demo ==========\n");
        System.out.println("Policy:");
        for (RateLimitRule rule : tenantPolicy.getRules()) {
            System.out.println("  • " + rule);
        }
        System.out.println();

        runScenario("Fixed Window Counter", new FixedWindowCounterAlgorithm(), tenantPolicy);
        System.out.println();
        runScenario("Sliding Window Counter", new SlidingWindowCounterAlgorithm(), tenantPolicy);
    }

    private static void runScenario(String algorithmName, RateLimitingAlgorithm algorithm, RateLimitPolicy policy) {
        ManualClock clock = new ManualClock(0L);
        RateLimiter rateLimiter = new DefaultRateLimiter(policy, algorithm, clock);
        InternalService service = new InternalService(rateLimiter, new PaidExternalResourceClient());

        System.out.println("=== " + algorithmName + " ===");
        System.out.println("Caller uses the same InternalService API. Only the algorithm object changes.\n");

        System.out.println("--- Step 1: request that does not need external usage ---");
        System.out.println("  " + service.handleRequest("T1", false));

        System.out.println("\n--- Step 2: 5 external calls in the first minute ---");
        for (int i = 1; i <= 5; i++) {
            System.out.println("  Call " + i + ": " + service.handleRequest("T1", true));
            clock.advanceSeconds(10);
        }

        System.out.println("\n--- Step 3: 6th external call in the same minute ---");
        System.out.println("  " + service.handleRequest("T1", true));

        System.out.println("\n--- Step 4: advance time and try again ---");
        clock.advanceSeconds(15);
        System.out.println("  Current time: " + clock.currentTimeMillis() / 1000 + "s");
        System.out.println("  " + service.handleRequest("T1", true));

        System.out.println("\n--- Step 5: different tenant has an independent quota ---");
        System.out.println("  " + service.handleRequest("T2", true));
    }
}

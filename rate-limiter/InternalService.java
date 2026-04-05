class InternalService {
    private final RateLimiter rateLimiter;
    private final ExternalResourceClient externalResourceClient;

    public InternalService(RateLimiter rateLimiter, ExternalResourceClient externalResourceClient) {
        this.rateLimiter = rateLimiter;
        this.externalResourceClient = externalResourceClient;
    }

    public String handleRequest(String tenantId, boolean requiresExternalCall) {
        System.out.println("Business logic running for tenant " + tenantId
                + " | requiresExternalCall=" + requiresExternalCall);

        if (!requiresExternalCall) {
            return "Handled locally without external resource";
        }

        String rateLimitKey = "tenant:" + tenantId;
        RateLimitDecision decision = rateLimiter.allow(rateLimitKey);

        if (!decision.isAllowed()) {
            return "External call denied by rate limiter -> " + decision;
        }

        return externalResourceClient.call(rateLimitKey);
    }
}

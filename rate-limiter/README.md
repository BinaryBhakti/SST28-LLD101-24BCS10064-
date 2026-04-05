# Rate Limiter - Low Level Design

## Overview
This module designs a pluggable rate limiting system for **external resource usage**, not for incoming client API requests.

Flow:

1. Client calls our API
2. Internal service runs business logic
3. Only if the business logic decides to call the paid external resource:
   - the rate limiter is consulted
   - if allowed, the external resource is called
   - otherwise the call is denied or handled gracefully

For the current implementation:

- **Implemented Algorithms**:
  - Fixed Window Counter
  - Sliding Window Counter
- **Extensible For Future Algorithms**:
  - Token Bucket
  - Leaky Bucket
  - Sliding Log
- **Key Examples**:
  - per tenant
  - per customer
  - per API key
  - per provider

## Design Patterns Used

### 1. Strategy Pattern
- `RateLimitingAlgorithm` is the strategy interface
- `FixedWindowCounterAlgorithm` and `SlidingWindowCounterAlgorithm` are pluggable implementations

### 2. Dependency Inversion
- `InternalService` depends on `RateLimiter`, not on a specific algorithm
- `DefaultRateLimiter` depends on `RateLimitingAlgorithm`, `Clock`, and `RateLimitPolicy` abstractions

### 3. Composition
- `DefaultRateLimiter` composes:
  - a policy
  - an algorithm
  - a clock

### 4. Testability by Abstraction
- `Clock` is abstracted so we can use `ManualClock` in tests and demos

## Class Diagram

```text
                 +----------------------+
                 |      RateLimiter     |
                 +----------------------+
                 | +allow(key)          |
                 +----------+-----------+
                            |
                 +----------v-----------+
                 |  DefaultRateLimiter  |
                 +----------------------+
                 | -policy              |
                 | -algorithm           |
                 | -clock               |
                 +----------+-----------+
                            |
                            |
          +-----------------+------------------+
          |                                    |
 +--------v--------+                  +--------v--------+
 | RateLimitPolicy |                  |      Clock      |
 +-----------------+                  +-----------------+
 | -rules          |                  | +currentTimeMs()|
 +--------+--------+                  +--------+--------+
          |                                    |
          | has many                           |
 +--------v--------+                  +--------v--------+
 | RateLimitRule   |                  |   ManualClock   |
 +-----------------+                  +-----------------+


                 +---------------------------+
                 |   RateLimitingAlgorithm   |
                 +---------------------------+
                 | +allow(key, rules, now)   |
                 +-------------+-------------+
                               |
             +-----------------+------------------+
             |                                    |
 +-----------v------------+        +--------------v--------------+
 | FixedWindowCounterAlgo |        | SlidingWindowCounterAlgo    |
 +------------------------+        +-----------------------------+


                 +----------------------+
                 |    InternalService   |
                 +----------------------+
                 | -rateLimiter         |
                 | -externalClient      |
                 | +handleRequest()     |
                 +----------+-----------+
                            |
                 +----------v-----------+
                 | ExternalResourceClient|
                 +----------------------+
```

## Core Classes

### `RateLimitRule`
Represents one limit such as:

- `5 requests per minute`
- `1000 requests per hour`

Each rule contains:

- rule id
- max requests
- window duration

### `RateLimitPolicy`
Groups multiple rules together for one usage type.

Example:

- `5 per minute`
- `12 per hour`

for the same tenant key.

### `RateLimiter`
Simple interface used by internal services:

```java
RateLimitDecision decision = rateLimiter.allow("tenant:T1");
```

### `RateLimitingAlgorithm`
Strategy abstraction for the actual limiting logic.

Current implementations:

- `FixedWindowCounterAlgorithm`
- `SlidingWindowCounterAlgorithm`

## How the Design Works

### 1. Where Rate Limiting Happens
Rate limiting is applied only when the system is about to call the external paid resource.

That means:

- if business logic does not need the external resource, no rate limit check happens
- only paid external usage consumes quota

This matches the requirement exactly.

### 2. How a Key Is Chosen
The `allow(key)` interface accepts a flexible string key.

Examples:

- `tenant:T1`
- `customer:C123`
- `apiKey:K-7788`
- `provider:OpenAI`

This keeps the module generic and reusable.

### 3. How Fixed Window Counter Works
For each key and each rule:

- maintain the current fixed time window
- maintain a simple counter in that window
- if the counter has already reached the limit, reject
- otherwise increment and allow

Example:

- limit = 5 per minute
- all requests between `00:00` and `00:59` belong to the same window

Pros:

- simple
- memory efficient
- fast

Trade-off:

- allows burstiness at window boundaries

### 4. How Sliding Window Counter Works
For each key and each rule:

- keep count for the current window
- keep count for the previous window
- estimate effective usage by weighting the previous window based on overlap

This gives smoother limiting behavior than fixed window.

Pros:

- smoother than fixed window
- still compact in memory

Trade-off:

- slightly more complex
- uses an approximation, not exact per-request history

### 5. Thread Safety
The implementation is thread-safe:

- state is stored in `ConcurrentHashMap`
- updates are synchronized per rate-limit key
- checking multiple rules for the same key is done atomically under the same lock

This prevents partial consumption when one rule passes and another fails.

### 6. Efficiency

#### Fixed Window Counter
- Time per request: `O(number of rules)`
- Space: `O(number of active keys × number of rules)`

#### Sliding Window Counter
- Time per request: `O(number of rules)`
- Space: `O(number of active keys × number of rules)`

The sliding counter uses slightly more state per rule than fixed window.

## Switching Algorithms Without Changing Business Logic

Business logic depends only on `RateLimiter`:

```java
RateLimiter limiter = new DefaultRateLimiter(policy, new FixedWindowCounterAlgorithm(), clock);
InternalService service = new InternalService(limiter, new PaidExternalResourceClient());
```

Switching to sliding window only changes object creation:

```java
RateLimiter limiter = new DefaultRateLimiter(policy, new SlidingWindowCounterAlgorithm(), clock);
InternalService service = new InternalService(limiter, new PaidExternalResourceClient());
```

`InternalService` stays unchanged.

## Features Implemented

✓ Pluggable rate limiting algorithms  
✓ Fixed Window Counter  
✓ Sliding Window Counter  
✓ Multiple rules per policy  
✓ Flexible key model  
✓ Thread-safe implementation  
✓ Manual clock for deterministic testing  
✓ Business flow demo showing limiter only before external call

## Future Extensions

- Add `TokenBucketAlgorithm`
- Add `LeakyBucketAlgorithm`
- Add `SlidingLogAlgorithm`
- Add distributed shared storage like Redis
- Add automatic cleanup for inactive keys
- Add metrics and observability
- Add different policies per provider or per endpoint

## Running the Code

```bash
javac *.java
java Main
```

The demo shows:

- request without external call skips rate limiting
- fixed window behavior
- sliding window behavior
- same business logic with different algorithms
- independent quotas for different tenants

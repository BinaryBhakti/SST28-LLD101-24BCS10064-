package com.example.metrics;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * INTENTION: Global metrics registry (should be a Singleton).
 *
 * CURRENT STATE (BROKEN ON PURPOSE):
 * - Constructor is public -> anyone can create instances.
 * - getInstance() is lazy but NOT thread-safe -> can create multiple instances.
 * - Reflection can call the constructor to create more instances.
 * - Serialization can create a new instance when deserialized.
 *
 * TODO (student):
 * 1) Make it a proper lazy, thread-safe singleton (private ctor)
 * 2) Block reflection-based multiple construction
 * 3) Preserve singleton on serialization (readResolve)
 */
public class MetricsRegistry implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<String, Long> counters = new HashMap<>();

    // Reflection protection flag
    private static boolean created = false;

    // Private constructor with reflection protection
    private MetricsRegistry() {
        if (created) {
            throw new IllegalStateException("Multiple instances of MetricsRegistry cannot be created.");
        }
        created = true;
    }

    // Static Inner Class (Holder) for thread-safe lazy initialization
    private static class Holder {
        private static final MetricsRegistry INSTANCE = new MetricsRegistry();
    }

    // Public method to return the singleton instance
    public static MetricsRegistry getInstance() {
        return Holder.INSTANCE;
    }

    public synchronized void setCount(String key, long value) {
        counters.put(key, value);
    }

    public synchronized void increment(String key) {
        counters.put(key, getCount(key) + 1);
    }

    public synchronized long getCount(String key) {
        return counters.getOrDefault(key, 0L);
    }

    public synchronized Map<String, Long> getAll() {
        return Collections.unmodifiableMap(new HashMap<>(counters));
    }

    // Implement readResolve() to preserve singleton on deserialization
    @Serial
    private Object readResolve() {
        return getInstance();
    }
}

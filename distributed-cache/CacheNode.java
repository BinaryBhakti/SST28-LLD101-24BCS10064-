import java.util.*;

class CacheNode<K, V> {
    private final String nodeId;
    private final int capacity;
    private final Map<K, V> storage;
    private final EvictionPolicy<K> evictionPolicy;

    public CacheNode(String nodeId, int capacity, EvictionPolicy<K> evictionPolicy) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Node capacity must be positive");
        }

        this.nodeId = nodeId;
        this.capacity = capacity;
        this.storage = new LinkedHashMap<>();
        this.evictionPolicy = evictionPolicy;
    }

    public String getNodeId() {
        return nodeId;
    }

    public boolean containsKey(K key) {
        return storage.containsKey(key);
    }

    public V get(K key) {
        if (!storage.containsKey(key)) {
            return null;
        }

        evictionPolicy.onKeyAccess(key);
        return storage.get(key);
    }

    public void put(K key, V value) {
        if (storage.containsKey(key)) {
            storage.put(key, value);
            evictionPolicy.onKeyAccess(key);
            return;
        }

        if (storage.size() >= capacity) {
            K evictionCandidate = evictionPolicy.getEvictionCandidate();
            if (evictionCandidate != null) {
                storage.remove(evictionCandidate);
                evictionPolicy.onKeyRemove(evictionCandidate);
            }
        }

        storage.put(key, value);
        evictionPolicy.onKeyInsert(key);
    }

    public Map<K, V> snapshot() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(storage));
    }

    @Override
    public String toString() {
        return "CacheNode(" + nodeId + ") size=" + storage.size() + "/" + capacity + " " + storage;
    }
}

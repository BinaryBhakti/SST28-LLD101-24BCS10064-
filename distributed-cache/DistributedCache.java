import java.util.*;
import java.util.function.Supplier;

class DistributedCache<K, V> {
    private final List<CacheNode<K, V>> nodes;
    private final DistributionStrategy<K> distributionStrategy;
    private final Database<K, V> database;

    public DistributedCache(int numberOfNodes, int nodeCapacity,
                            DistributionStrategy<K> distributionStrategy,
                            Supplier<EvictionPolicy<K>> evictionPolicyFactory,
                            Database<K, V> database) {
        if (numberOfNodes <= 0) {
            throw new IllegalArgumentException("Number of cache nodes must be positive");
        }

        this.nodes = new ArrayList<>();
        this.distributionStrategy = distributionStrategy;
        this.database = database;

        for (int i = 0; i < numberOfNodes; i++) {
            nodes.add(new CacheNode<>("NODE-" + i, nodeCapacity, evictionPolicyFactory.get()));
        }
    }

    public V get(K key) {
        CacheNode<K, V> node = resolveNode(key);

        if (node.containsKey(key)) {
            return node.get(key);
        }

        V value = database.get(key);
        if (value != null) {
            node.put(key, value);
        }
        return value;
    }

    // Assumption: write-through cache. put updates DB first, then the routed cache node.
    public void put(K key, V value) {
        database.put(key, value);
        resolveNode(key).put(key, value);
    }

    public boolean isCached(K key) {
        return resolveNode(key).containsKey(key);
    }

    public int getNodeIndexForKey(K key) {
        int index = distributionStrategy.selectNodeIndex(key, nodes.size());
        if (index < 0 || index >= nodes.size()) {
            throw new IllegalStateException("Distribution strategy returned invalid node index: " + index);
        }
        return index;
    }

    public List<CacheNode<K, V>> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public void displayClusterState() {
        for (int i = 0; i < nodes.size(); i++) {
            System.out.println("  [" + i + "] " + nodes.get(i));
        }
    }

    private CacheNode<K, V> resolveNode(K key) {
        return nodes.get(getNodeIndexForKey(key));
    }
}

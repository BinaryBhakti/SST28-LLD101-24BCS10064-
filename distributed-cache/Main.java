public class Main {
    public static void main(String[] args) {
        FakeDatabase<Integer, String> database = new FakeDatabase<>();
        database.seed(2, "Profile-Priya");
        database.seed(5, "Profile-Rahul");
        database.seed(8, "Profile-Neha");

        DistributedCache<Integer, String> cache = new DistributedCache<>(
                3,
                2,
                new ModuloDistributionStrategy<>(),
                LRUEvictionPolicy::new,
                database
        );

        System.out.println("========== Distributed Cache Demo ==========\n");
        System.out.println("Cluster Setup:");
        System.out.println("  Nodes: 3");
        System.out.println("  Capacity per node: 2");
        System.out.println("  Distribution: hash(key) % numberOfNodes");
        System.out.println("  Eviction: LRU");
        System.out.println("  Assumption: write-through put() updates DB and cache\n");

        System.out.println("--- Routing Preview ---");
        printRoute(cache, 1);
        printRoute(cache, 4);
        printRoute(cache, 7);
        printRoute(cache, 2);
        printRoute(cache, 5);

        System.out.println("\n--- Step 1: put(1), put(4) ---");
        cache.put(1, "Profile-Amit");
        cache.put(4, "Profile-Sara");
        cache.displayClusterState();

        System.out.println("\n--- Step 2: get(1) -> cache hit ---");
        System.out.println("  Value: " + cache.get(1));
        cache.displayClusterState();

        System.out.println("\n--- Step 3: put(7) on same node as 1 and 4 ---");
        System.out.println("  Since node capacity is 2, LRU eviction should remove key 4.");
        cache.put(7, "Profile-Karan");
        cache.displayClusterState();

        System.out.println("\n--- Step 4: get(2) -> cache miss, fetch from DB, then cache it ---");
        traceGet(cache, 2);
        cache.displayClusterState();

        System.out.println("\n--- Step 5: get(2) again -> cache hit ---");
        traceGet(cache, 2);
        cache.displayClusterState();

        System.out.println("\n--- Step 6: get(5) -> another DB-backed cache miss ---");
        traceGet(cache, 5);
        cache.displayClusterState();

        System.out.println("\n========== Demo Complete ==========");
    }

    private static void printRoute(DistributedCache<Integer, String> cache, int key) {
        System.out.println("  Key " + key + " -> NODE-" + cache.getNodeIndexForKey(key));
    }

    private static void traceGet(DistributedCache<Integer, String> cache, int key) {
        boolean wasCached = cache.isCached(key);
        String value = cache.get(key);
        System.out.println("  get(" + key + ") = " + value +
                (wasCached ? " [CACHE HIT]" : " [CACHE MISS -> DB LOAD]"));
    }
}

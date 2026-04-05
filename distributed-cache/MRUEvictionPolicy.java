import java.util.*;

class MRUEvictionPolicy<K> implements EvictionPolicy<K> {
    private final LinkedHashSet<K> accessOrder;

    public MRUEvictionPolicy() {
        this.accessOrder = new LinkedHashSet<>();
    }

    @Override
    public void onKeyAccess(K key) {
        accessOrder.remove(key);
        accessOrder.add(key);
    }

    @Override
    public void onKeyInsert(K key) {
        accessOrder.remove(key);
        accessOrder.add(key);
    }

    @Override
    public void onKeyRemove(K key) {
        accessOrder.remove(key);
    }

    @Override
    public K getEvictionCandidate() {
        K candidate = null;
        for (K key : accessOrder) {
            candidate = key;
        }
        return candidate;
    }
}

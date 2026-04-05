import java.util.*;

class LRUEvictionPolicy<K> implements EvictionPolicy<K> {
    private final LinkedHashSet<K> accessOrder;

    public LRUEvictionPolicy() {
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
        Iterator<K> iterator = accessOrder.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }
}

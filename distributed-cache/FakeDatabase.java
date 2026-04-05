import java.util.*;

class FakeDatabase<K, V> implements Database<K, V> {
    private final Map<K, V> storage;

    public FakeDatabase() {
        this.storage = new HashMap<>();
    }

    public void seed(K key, V value) {
        storage.put(key, value);
    }

    @Override
    public V get(K key) {
        return storage.get(key);
    }

    @Override
    public void put(K key, V value) {
        storage.put(key, value);
    }
}

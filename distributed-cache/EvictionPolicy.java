interface EvictionPolicy<K> {
    void onKeyAccess(K key);
    void onKeyInsert(K key);
    void onKeyRemove(K key);
    K getEvictionCandidate();
}

interface DistributionStrategy<K> {
    int selectNodeIndex(K key, int totalNodes);
}

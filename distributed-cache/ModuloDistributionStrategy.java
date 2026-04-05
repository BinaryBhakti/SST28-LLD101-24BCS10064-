class ModuloDistributionStrategy<K> implements DistributionStrategy<K> {
    @Override
    public int selectNodeIndex(K key, int totalNodes) {
        if (totalNodes <= 0) {
            throw new IllegalArgumentException("Total nodes must be positive");
        }

        int hash = (key == null) ? 0 : key.hashCode();
        return Math.floorMod(hash, totalNodes);
    }
}

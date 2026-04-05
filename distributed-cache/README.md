# Distributed Cache - Low Level Design

## Overview
This is a distributed cache design where data is stored across multiple cache nodes.
The system supports:

- `get(key)`
- `put(key, value)`

The design is built for extensibility:

- **Pluggable distribution strategy** for choosing a cache node
- **Pluggable eviction policy** for per-node cache management

For the current implementation:

- **Distribution Strategy**: Modulo-based routing
- **Eviction Policy**: LRU (Least Recently Used)
- **Database Write Model**: Write-through on `put(key, value)`

## Design Patterns Used

### 1. Strategy Pattern
- `DistributionStrategy` decides which cache node a key belongs to
- `EvictionPolicy` decides which key should be evicted inside a node

### 2. Dependency Inversion
- `DistributedCache` depends on `DistributionStrategy`, `EvictionPolicy`, and `Database` abstractions
- This keeps the core cache coordinator open for future routing and eviction changes

### 3. Composition
- `DistributedCache` is composed of multiple `CacheNode` objects
- Each `CacheNode` owns its own storage and eviction policy instance

## Class Diagram

```text
                 +----------------------+
                 |   DistributionStrategy|
                 +----------------------+
                 | +selectNodeIndex()   |
                 +----------+-----------+
                            |
                            |
                 +----------v-----------+
                 | ModuloDistribution   |
                 +----------------------+


                 +----------------------+
                 |    EvictionPolicy    |
                 +----------------------+
                 | +onKeyAccess()       |
                 | +onKeyInsert()       |
                 | +onKeyRemove()       |
                 | +getEvictionCandidate|
                 +----------+-----------+
                            |
          +-----------------+------------------+
          |                                    |
 +--------v--------+                  +--------v--------+
 | LRU Eviction    |                  | MRU Eviction    |
 +-----------------+                  +-----------------+


                 +----------------------+
                 |      Database        |
                 +----------------------+
                 | +get()               |
                 | +put()               |
                 +----------+-----------+
                            |
                 +----------v-----------+
                 |    FakeDatabase      |
                 +----------------------+


                 +----------------------+
                 |    DistributedCache  |
                 +----------------------+
                 | -nodes               |
                 | -distributionStrategy|
                 | -database            |
                 | +get()               |
                 | +put()               |
                 +----------+-----------+
                            |
                            | contains many
                            |
                 +----------v-----------+
                 |      CacheNode       |
                 +----------------------+
                 | -capacity            |
                 | -storage             |
                 | -evictionPolicy      |
                 | +get()               |
                 | +put()               |
                 +----------------------+
```

## How the Design Works

### 1. Data Distribution Across Nodes
The cache is split into multiple nodes.

For the current implementation:

```java
nodeIndex = hash(key) % numberOfNodes
```

Example with 3 nodes:

- `1 -> NODE-1`
- `4 -> NODE-1`
- `7 -> NODE-1`
- `2 -> NODE-2`

Because distribution is behind the `DistributionStrategy` interface, this can later be replaced by:

- consistent hashing
- routing-table-based mapping
- range-based partitioning

without changing `DistributedCache` logic.

### 2. Cache Miss Handling
When `get(key)` is called:

1. The system first routes the key to the correct node.
2. If the key is present in that node, it is returned directly.
3. If the key is missing:
   - the value is fetched from the database
   - the value is inserted into the same routed cache node
   - the value is returned to the caller

This makes the cache a **read-through** cache on misses.

### 3. put(key, value)
Assumption for this implementation:

- `put(key, value)` is **write-through**
- the database is updated first
- then the routed cache node is updated

This keeps cache and DB aligned for the demo.

### 4. Eviction Inside a Node
Each `CacheNode` has limited capacity.

When a new key needs to be inserted and the node is already full:

1. The node asks its configured `EvictionPolicy` for the eviction candidate
2. That key is removed from the node
3. The new key is inserted

For LRU:

- every `get()` or update marks the key as most recently used
- the least recently used key is evicted first

## Features Implemented

✓ Configurable number of cache nodes  
✓ Configurable capacity per node  
✓ Pluggable distribution strategy  
✓ Pluggable eviction policy  
✓ Read-through cache miss handling  
✓ Write-through `put()` behavior  
✓ In-memory fake database for demonstration  
✓ Runnable demo

## Extensibility

The design is ready for future changes:

- Add `ConsistentHashingStrategy` without changing `DistributedCache`
- Add `LFUEvictionPolicy` without changing `CacheNode`
- Replace `FakeDatabase` with a real repository or DAO implementation
- Add TTL support by extending `CacheNode`
- Add cache metrics and observability without changing the external API

## Running the Code

```bash
javac *.java
java Main
```

The demo shows:

- how keys are routed to nodes
- cache hit vs cache miss behavior
- database fetch on miss
- LRU eviction when a node reaches capacity

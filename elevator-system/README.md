# Elevator System - Low Level Design

## Overview
A complete elevator system design for a multi-floor building with multiple elevators. The system handles efficient elevator scheduling, load distribution, and maintenance modes.

## Requirements Addressed

✓ **750kg capacity** per elevator with weight tracking
✓ **External buttons** (UP/DOWN) on each floor to call elevators
✓ **Internal panel** buttons to select destination floors
✓ **Optimal scheduling** using SCAN algorithm
✓ **Elevator maintenance mode** — no requests assigned during maintenance
✓ **Floor maintenance mode** — buttons disabled for that floor
✓ **Load distribution strategy** — nearest elevator or least loaded
✓ **Request queuing** — system manages pending requests

## Design Patterns Used

### 1. **Singleton Pattern**
- `Building` class — single instance manages all shafts

### 2. **Strategy Pattern**
- `FloorSchedulingStrategy` — SCAN algorithm for optimal stops
- `LoadBalancingStrategy` — elevator selection (NearestElevator, LeastLoaded)

### 3. **State Pattern** (via Enums)
- `ElevatorStatus` — IDLE, MOVING_UP, MOVING_DOWN, MAINTENANCE
- `FloorStatus` — ACTIVE, MAINTENANCE
- `RequestType` — UP, DOWN

### 4. **Template Method Pattern**
- `Button` abstract class with up/down behavior

## Architecture

```
Building (Singleton)
├── ElevatorShaft
│   ├── Elevator (up to N elevators)
│   │   ├── destinationFloors
│   │   ├── passengers
│   │   └── FloorSchedulingStrategy
│   ├── Floor (0 to N floors)
│   │   ├── ExternalButton (UP/DOWN)
│   │   └── usersWaiting
│   └── LoadBalancingStrategy
├── Request (pending requests queue)
└── User (with weight tracking)
```

## Key Classes

### User
- `userId, name, weight`
- Weight tracked for capacity management

### Elevator
- **Capacity**: 750kg
- **Status**: IDLE, MOVING_UP, MOVING_DOWN, MAINTENANCE
- **Methods**: addUser(), removeUser(), moveToFloor(), getOptimalSequence()
- **Scheduling**: Uses SCAN algorithm for efficient floor ordering

### ElevatorShaft
- Manages all elevators and floors
- Distributes requests using LoadBalancingStrategy
- Enforces maintenance rules

### Button Hierarchy
- **Button** (abstract) — base with press/release
- **ExternalButton** — on floors (UP/DOWN)
- **InternalButton** — inside elevator

### Strategies

#### FloorSchedulingStrategy
- **SCANAlgorithm**: Moves in one direction, services all stops, then reverses
- Optimal for reducing wait times

#### LoadBalancingStrategy
- **NearestElevatorStrategy**: Selects closest elevator to source floor
- **LeastLoadedStrategy**: Selects least loaded elevator

## SOLID Principles

- **S**ingle Responsibility: Each class has one purpose
- **O**pen/Closed: New strategies can be added without modifying existing code
- **L**iskov Substitution: Any strategy implementation is interchangeable
- **I**nterface Segregation: Separate interfaces for scheduling and load balancing
- **D**ependency Inversion: Elevator depends on strategy abstraction

## Usage Flow

### Basic Request
```java
ElevatorShaft shaft = new ElevatorShaft("SHAFT-1", 10);
shaft.addElevator(new Elevator("E1", 0));

User user = new User("U1", "John", 70);
shaft.requestElevator(user, 0, 5); // Request elevator from floor 0 to 5
```

### Maintenance
```java
shaft.setElevatorMaintenance("E1", true);  // Prevent new requests
shaft.setFloorMaintenance(5, true);        // Disable floor 5 access
```

### Optimal Scheduling
```java
elevator.addDestinationFloor(3);
elevator.addDestinationFloor(7);
List<Integer> sequence = elevator.getOptimalSequence(); // SCAN algorithm
```

## Features Implemented

✓ Multi-elevator system with independent control
✓ Weight-based capacity management
✓ Request queuing and assignment
✓ Optimal floor scheduling (SCAN algorithm)
✓ Elevator maintenance mode
✓ Floor maintenance mode
✓ Two load balancing strategies
✓ Visual status display
✓ Exception handling

## Demo Scenarios

1. **Normal Requests** — Multiple users requesting elevators
2. **Capacity Test** — Filling elevator to max capacity
3. **Elevator Maintenance** — Taking elevator out of service
4. **Floor Maintenance** — Disabling floor access
5. **SCAN Algorithm** — Optimal floor sequencing
6. **Load Balancing** — Smart elevator selection

## Future Enhancements

- Round-robin scheduling across multiple shafts
- User preferences (express elevators, etc.)
- Energy optimization
- Real-time ETA calculation
- Emergency mode
- Occupancy-based pricing
- Integration with access control

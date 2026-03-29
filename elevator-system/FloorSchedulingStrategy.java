import java.util.*;

// Strategy Pattern — different algorithms for scheduling elevator stops
interface FloorSchedulingStrategy {
    List<Integer> getOptimalFloorSequence(int currentFloor, List<Integer> requestedFloors, ElevatorStatus direction);
}

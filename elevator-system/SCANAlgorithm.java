import java.util.*;

// SCAN Algorithm — Elevator moves in one direction, services all stops, then reverses
class SCANAlgorithm implements FloorSchedulingStrategy {
    @Override
    public List<Integer> getOptimalFloorSequence(int currentFloor, List<Integer> requestedFloors, 
                                                   ElevatorStatus direction) {
        List<Integer> sequence = new ArrayList<>();
        
        List<Integer> upFloors = new ArrayList<>();
        List<Integer> downFloors = new ArrayList<>();
        
        for (int floor : requestedFloors) {
            if (floor > currentFloor) {
                upFloors.add(floor);
            } else if (floor < currentFloor) {
                downFloors.add(floor);
            } else {
                sequence.add(floor);
            }
        }
        
        Collections.sort(upFloors);
        Collections.sort(downFloors, Collections.reverseOrder());
        
        if (direction == ElevatorStatus.MOVING_UP) {
            sequence.addAll(upFloors);
            sequence.addAll(downFloors);
        } else {
            sequence.addAll(downFloors);
            sequence.addAll(upFloors);
        }
        
        return sequence;
    }
}

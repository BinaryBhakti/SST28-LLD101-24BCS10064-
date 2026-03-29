import java.util.*;

class Elevator {
    private String elevatorId;
    private int currentFloor;
    private int currentLoad; // in kg
    private final int maxCapacity = 750; // kg
    private ElevatorStatus status;
    private List<Integer> destinationFloors;
    private List<User> passengers;
    private FloorSchedulingStrategy schedulingStrategy;

    public Elevator(String elevatorId, int startFloor) {
        this.elevatorId = elevatorId;
        this.currentFloor = startFloor;
        this.currentLoad = 0;
        this.status = ElevatorStatus.IDLE;
        this.destinationFloors = new ArrayList<>();
        this.passengers = new ArrayList<>();
        this.schedulingStrategy = new SCANAlgorithm();
    }

    public String getElevatorId() { return elevatorId; }
    public int getCurrentFloor() { return currentFloor; }
    public int getCurrentLoad() { return currentLoad; }
    public int getMaxCapacity() { return maxCapacity; }
    public ElevatorStatus getStatus() { return status; }
    public List<Integer> getDestinationFloors() { return new ArrayList<>(destinationFloors); }
    public List<User> getPassengers() { return passengers; }

    public boolean canAddUser(User user) {
        return (currentLoad + user.getWeight()) <= maxCapacity;
    }

    public void addUser(User user) throws Exception {
        if (!canAddUser(user)) {
            throw new Exception("Elevator at capacity! Cannot add " + user.getName());
        }
        passengers.add(user);
        currentLoad += user.getWeight();
    }

    public void removeUser(User user) throws Exception {
        if (!passengers.contains(user)) {
            throw new Exception("User not in elevator!");
        }
        passengers.remove(user);
        currentLoad -= user.getWeight();
    }

    public void addDestinationFloor(int floor) {
        if (!destinationFloors.contains(floor)) {
            destinationFloors.add(floor);
        }
    }

    public void moveToFloor(int floor) {
        if (floor > currentFloor) {
            status = ElevatorStatus.MOVING_UP;
        } else if (floor < currentFloor) {
            status = ElevatorStatus.MOVING_DOWN;
        }
        currentFloor = floor;
        destinationFloors.remove(Integer.valueOf(floor));
        
        if (destinationFloors.isEmpty()) {
            status = ElevatorStatus.IDLE;
        }
    }

    public void setMaintenance(boolean inMaintenance) {
        status = inMaintenance ? ElevatorStatus.MAINTENANCE : ElevatorStatus.IDLE;
    }

    public List<Integer> getOptimalSequence() {
        ElevatorStatus direction = status == ElevatorStatus.MOVING_DOWN ? 
                                   ElevatorStatus.MOVING_DOWN : ElevatorStatus.MOVING_UP;
        return schedulingStrategy.getOptimalFloorSequence(currentFloor, destinationFloors, direction);
    }

    @Override
    public String toString() {
        return "Elevator(" + elevatorId + ") Floor:" + currentFloor + " Load:" + currentLoad + 
               "kg/" + maxCapacity + "kg [" + status + "]";
    }
}

import java.util.*;

// Shaft controls all elevators and request distribution
class ElevatorShaft {
    private String shaftId;
    private List<Elevator> elevators;
    private List<Floor> floors;
    private Queue<Request> pendingRequests;
    private LoadBalancingStrategy loadBalancingStrategy;

    public ElevatorShaft(String shaftId, int totalFloors) {
        this.shaftId = shaftId;
        this.elevators = new ArrayList<>();
        this.floors = new ArrayList<>();
        this.pendingRequests = new LinkedList<>();
        this.loadBalancingStrategy = new NearestElevatorStrategy();

        // Initialize floors
        for (int i = 0; i < totalFloors; i++) {
            floors.add(new Floor(i));
        }
    }

    public void addElevator(Elevator elevator) {
        elevators.add(elevator);
    }

    public String getShaftId() { return shaftId; }
    public List<Elevator> getElevators() { return elevators; }
    public List<Floor> getFloors() { return floors; }

    // User calls elevator from a floor
    public void requestElevator(User user, int currentFloor, int destinationFloor) throws Exception {
        Floor floor = floors.get(currentFloor);
        
        if (floor.getStatus() == FloorStatus.MAINTENANCE) {
            throw new Exception("Floor " + currentFloor + " is under maintenance!");
        }

        RequestType type = destinationFloor > currentFloor ? RequestType.UP : RequestType.DOWN;
        Request request = new Request("R-" + System.currentTimeMillis(), currentFloor, 
                                     destinationFloor, type, user);
        
        pendingRequests.add(request);
        floor.addUserWaiting(user);
        
        processRequest(request);
    }

    // Assign request to optimal elevator
    private void processRequest(Request request) throws Exception {
        String selectedElevatorId = loadBalancingStrategy.selectElevator(elevators, request);
        
        if (selectedElevatorId == null) {
            throw new Exception("No available elevator to service request!");
        }

        Elevator elevator = elevators.stream()
                .filter(e -> e.getElevatorId().equals(selectedElevatorId))
                .findFirst()
                .orElse(null);

        if (elevator == null) return;

        if (!elevator.canAddUser(request.getUser())) {
            return;
        }

        moveElevatorToFloor(elevator, request.getSourceFloor());
        elevator.addDestinationFloor(request.getDestinationFloor());
        request.setAssigned(true);
    }

    public void moveElevatorToFloor(Elevator elevator, int floor) {
        System.out.println("  → " + elevator.getElevatorId() + " moving from floor " + 
                         elevator.getCurrentFloor() + " to floor " + floor);
        elevator.moveToFloor(floor);
    }

    public void pickupUser(Elevator elevator, User user) throws Exception {
        Floor floor = floors.get(elevator.getCurrentFloor());
        floor.removeUserWaiting(user);
        elevator.addUser(user);
    }

    public void dropoffUser(Elevator elevator, User user) throws Exception {
        elevator.removeUser(user);
    }

    public void setElevatorMaintenance(String elevatorId, boolean maintenance) {
        Elevator e = elevators.stream()
                .filter(el -> el.getElevatorId().equals(elevatorId))
                .findFirst()
                .orElse(null);
        
        if (e != null) {
            e.setMaintenance(maintenance);
            System.out.println("  → " + elevatorId + " is now " + (maintenance ? "UNDER MAINTENANCE" : "ACTIVE"));
        }
    }

    public void setFloorMaintenance(int floor, boolean maintenance) {
        floors.get(floor).setStatus(maintenance ? FloorStatus.MAINTENANCE : FloorStatus.ACTIVE);
        System.out.println("  → Floor " + floor + " is now " + (maintenance ? "UNDER MAINTENANCE" : "ACTIVE"));
    }

    @Override
    public String toString() {
        return "Shaft(" + shaftId + ") - " + elevators.size() + " elevators, " + floors.size() + " floors";
    }
}

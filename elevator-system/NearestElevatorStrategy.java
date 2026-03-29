import java.util.*;

// Assign request to elevator nearest to source floor
class NearestElevatorStrategy implements LoadBalancingStrategy {
    @Override
    public String selectElevator(List<Elevator> elevators, Request request) {
        String selectedId = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Elevator e : elevators) {
            if (e.getStatus() == ElevatorStatus.MAINTENANCE) continue;
            
            int distance = Math.abs(e.getCurrentFloor() - request.getSourceFloor());
            
            if (distance < minDistance) {
                minDistance = distance;
                selectedId = e.getElevatorId();
            }
        }
        
        return selectedId;
    }
}

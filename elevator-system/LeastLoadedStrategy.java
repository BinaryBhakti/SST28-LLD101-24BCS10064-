import java.util.*;

// Assign request to least loaded elevator
class LeastLoadedStrategy implements LoadBalancingStrategy {
    @Override
    public String selectElevator(List<Elevator> elevators, Request request) {
        String selectedId = null;
        int minLoad = Integer.MAX_VALUE;
        
        for (Elevator e : elevators) {
            if (e.getStatus() == ElevatorStatus.MAINTENANCE) continue;
            
            int currentLoad = e.getCurrentLoad();
            
            if (currentLoad < minLoad) {
                minLoad = currentLoad;
                selectedId = e.getElevatorId();
            }
        }
        
        return selectedId;
    }
}

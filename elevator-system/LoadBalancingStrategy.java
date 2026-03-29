import java.util.*;

// Strategy Pattern — different load balancing algorithms
interface LoadBalancingStrategy {
    String selectElevator(List<Elevator> elevators, Request request);
}

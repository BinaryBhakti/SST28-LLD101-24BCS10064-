import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("========== Elevator System Demo ==========\n");

        // Initialize building
        Building building = Building.initializeBuilding("Tech Tower", 10);
        System.out.println("Building: " + building);

        // Create elevator shaft
        ElevatorShaft shaft = new ElevatorShaft("SHAFT-1", 10);
        building.addShaft(shaft);

        // Add elevators to shaft (750kg capacity each)
        Elevator e1 = new Elevator("E1", 0);
        Elevator e2 = new Elevator("E2", 0);
        Elevator e3 = new Elevator("E3", 5);
        
        shaft.addElevator(e1);
        shaft.addElevator(e2);
        shaft.addElevator(e3);

        System.out.println("\n--- Elevator Setup ---");
        for (Elevator e : shaft.getElevators()) {
            System.out.println("  • " + e);
        }

        // Create users
        User user1 = new User("U1", "Amit", 70);
        User user2 = new User("U2", "Priya", 60);
        User user3 = new User("U3", "Rahul", 75);
        User user4 = new User("U4", "Sara", 55);

        System.out.println("\n--- Users ---");
        System.out.println("  • " + user1);
        System.out.println("  • " + user2);
        System.out.println("  • " + user3);
        System.out.println("  • " + user4);

        // Scenario 1: Normal elevator requests
        System.out.println("\n--- Scenario 1: Normal Requests ---");
        shaft.requestElevator(user1, 0, 5);
        shaft.requestElevator(user2, 0, 7);
        shaft.requestElevator(user3, 2, 9);

        System.out.println("\nElevator Status:");
        for (Elevator e : shaft.getElevators()) {
            System.out.println("  " + e);
        }

        // Scenario 2: Elevator at capacity
        System.out.println("\n--- Scenario 2: Capacity Test ---");
        System.out.println("Elevator E1 current load: " + e1.getCurrentLoad() + "kg (max: " + e1.getMaxCapacity() + "kg)");
        System.out.println("Adding users to fill capacity...");
        
        try {
            shaft.pickupUser(e1, user1);
            System.out.println("  ✓ Added: " + user1 + " (Current load: " + e1.getCurrentLoad() + "kg)");
            
            shaft.pickupUser(e1, user2);
            System.out.println("  ✓ Added: " + user2 + " (Current load: " + e1.getCurrentLoad() + "kg)");
            
            shaft.pickupUser(e1, user3);
            System.out.println("  ✓ Added: " + user3 + " (Current load: " + e1.getCurrentLoad() + "kg)");
            
            shaft.pickupUser(e1, user4);
            System.out.println("  ✓ Added: " + user4 + " (Current load: " + e1.getCurrentLoad() + "kg)");
            
            // Try to add another heavy user (should fail)
            User heavyUser = new User("HU1", "Heavy-User", 500);
            shaft.pickupUser(e1, heavyUser);
        } catch (Exception ex) {
            System.out.println("  ✗ " + ex.getMessage());
        }

        // Scenario 3: Elevator maintenance
        System.out.println("\n--- Scenario 3: Elevator Maintenance ---");
        shaft.setElevatorMaintenance("E1", true);
        System.out.println("E1 Status: " + e1.getStatus());
        
        shaft.setElevatorMaintenance("E1", false);
        System.out.println("E1 restored: " + e1.getStatus());

        // Scenario 4: Floor maintenance
        System.out.println("\n--- Scenario 4: Floor Maintenance ---");
        shaft.setFloorMaintenance(5, true);
        
        System.out.println("Trying to request from floor 5 (under maintenance)...");
        try {
            User testUser = new User("T1", "Test", 70);
            shaft.requestElevator(testUser, 5, 8);
        } catch (Exception ex) {
            System.out.println("  ✓ " + ex.getMessage());
        }

        shaft.setFloorMaintenance(5, false);
        System.out.println("Floor 5 restored");

        // Scenario 5: Optimal floor scheduling
        System.out.println("\n--- Scenario 5: SCAN Algorithm (Optimal Scheduling) ---");
        Elevator demoElevator = new Elevator("E-DEMO", 2);
        demoElevator.addDestinationFloor(3);
        demoElevator.addDestinationFloor(7);
        demoElevator.addDestinationFloor(1);
        demoElevator.addDestinationFloor(9);
        
        System.out.println("Elevator at floor: " + demoElevator.getCurrentFloor());
        System.out.println("Destination floors (unordered): " + demoElevator.getDestinationFloors());
        
        List<Integer> optimalSequence = demoElevator.getOptimalSequence();
        System.out.println("Optimal stop sequence (SCAN): " + optimalSequence);

        // Scenario 6: Load balancing strategy
        System.out.println("\n--- Scenario 6: Load Balancing ---");
        System.out.println("Elevator Status Before New Requests:");
        for (Elevator e : shaft.getElevators()) {
            System.out.println("  " + e);
        }
        
        System.out.println("\nUser from Floor 1 requesting to go to Floor 8...");
        User newUser = new User("NU1", "NewUser", 80);
        shaft.requestElevator(newUser, 1, 8);
        
        System.out.println("\nElevator Status After Request:");
        for (Elevator e : shaft.getElevators()) {
            System.out.println("  " + e);
        }

        // Final status
        System.out.println("\n--- Final Status ---");
        System.out.println("All Floors:");
        for (Floor floor : shaft.getFloors()) {
            System.out.println("  " + floor);
        }
        
        System.out.println("\nAll Elevators:");
        for (Elevator e : shaft.getElevators()) {
            System.out.println("  " + e);
        }

        System.out.println("\n========== Demo Complete ==========");
    }
}

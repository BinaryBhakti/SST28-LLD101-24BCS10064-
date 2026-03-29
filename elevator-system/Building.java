import java.util.*;

// Singleton — manages all elevator shafts in the building
class Building {
    private static Building instance;
    private String buildingName;
    private int totalFloors;
    private Map<String, ElevatorShaft> shafts;

    private Building(String name, int floors) {
        this.buildingName = name;
        this.totalFloors = floors;
        this.shafts = new HashMap<>();
    }

    public static Building initializeBuilding(String name, int floors) {
        if (instance == null) {
            instance = new Building(name, floors);
        }
        return instance;
    }

    public static Building getInstance() {
        return instance;
    }

    public void addShaft(ElevatorShaft shaft) {
        shafts.put(shaft.getShaftId(), shaft);
    }

    public ElevatorShaft getShaft(String shaftId) {
        return shafts.get(shaftId);
    }

    public String getBuildingName() { return buildingName; }
    public int getTotalFloors() { return totalFloors; }
    public Collection<ElevatorShaft> getAllShafts() { return shafts.values(); }

    @Override
    public String toString() {
        return buildingName + " (" + totalFloors + " floors, " + shafts.size() + " shafts)";
    }
}

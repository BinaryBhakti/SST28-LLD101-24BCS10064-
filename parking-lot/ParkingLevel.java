import java.util.List;

class ParkingLevel {
    private int levelNumber;
    private List<ParkingSlot> slots;

    public ParkingLevel(int levelNumber, List<ParkingSlot> slots) {
        this.levelNumber = levelNumber;
        this.slots = slots;
    }

    public int getLevelNumber() { return levelNumber; }
    public List<ParkingSlot> getSlots() { return slots; }
}

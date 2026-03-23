class ParkingSlot {
    private String slotId;
    private SlotType slotType;
    private boolean occupied;
    private int level;
    private int position; // simulated linear position within the level

    public ParkingSlot(String slotId, SlotType slotType, int level, int position) {
        this.slotId = slotId;
        this.slotType = slotType;
        this.level = level;
        this.position = position;
        this.occupied = false;
    }

    // Distance from a gate: slot position offset + penalty per level above ground
    public int distanceTo(EntryGate gate) {
        return Math.abs(position - gate.getPosition()) + level * 10;
    }

    public String getSlotId() { return slotId; }
    public SlotType getSlotType() { return slotType; }
    public int getLevel() { return level; }
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
}

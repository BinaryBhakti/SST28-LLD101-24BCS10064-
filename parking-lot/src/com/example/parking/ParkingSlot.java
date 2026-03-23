package com.example.parking;

/**
 * Represents a single parking slot.
 * occupy() is synchronized to prevent the same slot being assigned to two vehicles concurrently.
 */
public class ParkingSlot {
    private final String slotId;
    private final int floorNo;
    private final SlotType slotType;
    private Vehicle parkedVehicle;
    private boolean occupied;

    public ParkingSlot(String slotId, int floorNo, SlotType slotType) {
        this.slotId = slotId;
        this.floorNo = floorNo;
        this.slotType = slotType;
        this.occupied = false;
    }

    /**
     * Synchronized to handle concurrency — two threads cannot occupy the same slot.
     */
    public synchronized boolean occupy(Vehicle vehicle) {
        if (occupied) {
            return false;
        }
        this.parkedVehicle = vehicle;
        this.occupied = true;
        return true;
    }

    public synchronized void release() {
        this.parkedVehicle = null;
        this.occupied = false;
    }

    public synchronized boolean isOccupied() {
        return occupied;
    }

    public String getSlotId() { return slotId; }
    public int getFloorNo() { return floorNo; }
    public SlotType getSlotType() { return slotType; }
    public Vehicle getParkedVehicle() { return parkedVehicle; }

    @Override
    public String toString() {
        return "Slot[" + slotId + ", Floor=" + floorNo + ", Type=" + slotType + "]";
    }
}

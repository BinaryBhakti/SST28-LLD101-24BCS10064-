package com.example.parking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParkingFloor {
    private final int floorNo;
    private final List<ParkingSlot> slots;

    public ParkingFloor(int floorNo) {
        this.floorNo = floorNo;
        this.slots = new ArrayList<>();
    }

    public void addSlot(ParkingSlot slot) {
        slots.add(slot);
    }

    public List<ParkingSlot> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    public int getFloorNo() { return floorNo; }
}

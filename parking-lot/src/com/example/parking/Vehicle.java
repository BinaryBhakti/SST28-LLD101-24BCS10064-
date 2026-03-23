package com.example.parking;

public class Vehicle {
    private final String vehicleNo;
    private final String color;
    private final String model;

    public Vehicle(String vehicleNo, String color, String model) {
        this.vehicleNo = vehicleNo;
        this.color = color;
        this.model = model;
    }

    public String getVehicleNo() { return vehicleNo; }
    public String getColor() { return color; }
    public String getModel() { return model; }

    @Override
    public String toString() {
        return vehicleNo + " (" + color + " " + model + ")";
    }
}

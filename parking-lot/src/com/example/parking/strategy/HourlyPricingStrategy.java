package com.example.parking.strategy;

import com.example.parking.SlotType;

/**
 * Charges per hour based on slot type.
 * SMALL = Rs.10/hr, MEDIUM = Rs.20/hr, LARGE = Rs.40/hr
 */
public class HourlyPricingStrategy implements PricingStrategy {

    @Override
    public double calculate(SlotType slotType, long durationMinutes) {
        double hours = Math.ceil(durationMinutes / 60.0);
        if (hours == 0) hours = 1; // minimum 1 hour

        switch (slotType) {
            case SMALL:  return hours * 10;
            case MEDIUM: return hours * 20;
            case LARGE:  return hours * 40;
            default:     throw new IllegalArgumentException("Unknown slot type: " + slotType);
        }
    }
}

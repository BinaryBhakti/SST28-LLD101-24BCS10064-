package com.example.parking.strategy;

import com.example.parking.SlotType;

/**
 * Strategy for calculating parking bill.
 * Amount = f(slotType, durationMinutes).
 * NOT based on vehicle type.
 */
public interface PricingStrategy {
    double calculate(SlotType slotType, long durationMinutes);
}

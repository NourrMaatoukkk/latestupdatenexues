package com.nexus.pricing.strategies;

public class NormalPricing implements PricingStrategy {

    @Override
    public double applyDiscount(double price) {
        return price;
    }
}
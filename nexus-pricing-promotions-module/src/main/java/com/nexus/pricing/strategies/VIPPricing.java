package com.nexus.pricing.strategies;

public class VIPPricing implements PricingStrategy {

    @Override
    public double applyDiscount(double price) {
        return price * 0.85;
    }
}
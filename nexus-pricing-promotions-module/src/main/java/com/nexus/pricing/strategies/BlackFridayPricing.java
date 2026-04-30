package com.nexus.pricing.strategies;

public class BlackFridayPricing implements PricingStrategy {

    @Override
    public double applyDiscount(double price) {
        return price * 0.80;
    }
}
package com.nexus.pricing.manager;

import com.nexus.pricing.models.Order;
import com.nexus.pricing.singleton.GlobalConfigManager;
import com.nexus.pricing.strategies.PricingStrategy;

public class PricingManager {

    public double calculateFinalPrice(Order order, PricingStrategy strategy) {

        double basePrice = order.getTotalBasePrice();

        double discountedPrice = strategy.applyDiscount(basePrice);

        double tax = discountedPrice *
                GlobalConfigManager.getInstance().getTaxRate();

        return discountedPrice + tax;
    }
}
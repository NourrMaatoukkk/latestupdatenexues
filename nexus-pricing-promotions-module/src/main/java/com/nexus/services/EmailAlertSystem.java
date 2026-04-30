package com.nexus.services;

import com.nexus.models.Product;

public class EmailAlertSystem implements StockObserver {
    @Override
    public void onStockChange(Product product) {
        if (product.getQuantity() <= product.getThreshold()) {
            System.out.println(">>> [EMAIL SYSTEM]: Sending critical alert for " + product.getName());
        }
    }
}
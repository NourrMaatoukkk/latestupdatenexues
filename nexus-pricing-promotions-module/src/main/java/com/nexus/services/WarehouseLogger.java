package com.nexus.services;

import com.nexus.models.Product;

public class WarehouseLogger implements StockObserver {
    @Override
    public void onStockChange(Product product) {
        System.out.println(">>> [LOG SYSTEM]: SKU " + product.getId() + " updated. Current Stock: " + product.getQuantity());
    }
}
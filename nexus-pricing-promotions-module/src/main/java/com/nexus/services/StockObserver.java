package com.nexus.services;

import com.nexus.models.Product;

public interface StockObserver {
    void onStockChange(Product product);
}
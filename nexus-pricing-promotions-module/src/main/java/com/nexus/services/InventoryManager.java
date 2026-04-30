package com.nexus.services;

import com.nexus.models.Product;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<StockObserver> observers = new ArrayList<>();

    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(StockObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Product product) {
        for (StockObserver observer : observers) {
            observer.onStockChange(product);
        }
    }
}
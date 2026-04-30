package com.nexus.services;

import com.google.cloud.firestore.Firestore;
import com.nexus.models.Product;

public class InventoryService {
    
    private Firestore db = FirebaseService.getInstance().getDb();
    private InventoryManager eventManager;

    public InventoryService() {
    }

    public InventoryService(InventoryManager manager) {
        this.eventManager = manager;
    }

    // 1. Method to ADD a product (Logical and Clean)
    public void addProductToCloud(Product p) {
        try {
            // This line sends the object directly to Firebase
            db.collection("inventory").document(p.getId()).set(p);
            System.out.println("Cloud Sync: Added " + p.getName());
        } catch (Exception e) {
            System.err.println("Error adding product: " + e.getMessage());
        }
    }

    // 2. Method to UPDATE quantity (Prevents unsafe operations)
    public void updateStockLevel(String productId, int newQuantity) {
        if (newQuantity < 0) {
            System.out.println("BLOCKED: Cannot have negative stock.");
            return;
        }
        db.collection("inventory").document(productId).update("quantity", newQuantity);
    }

    // 3. Method to DELETE (Flexible feature)
    public void removeProductFromCloud(String productId) {
        db.collection("inventory").document(productId).delete();
        System.out.println("Cloud Sync: Removed item " + productId);
    }

    public void updateStockInCloud(Product p) {
        try {
            db.collection("inventory").document(p.getId()).set(p);
            if (eventManager != null) {
                eventManager.notifyObservers(p);
            }
        } catch (Exception e) {
            System.err.println("Cloud Sync Failed: " + e.getMessage());
        }
    }
}
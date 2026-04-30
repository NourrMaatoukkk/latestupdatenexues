package com.nexus.models;

public class Product {
    private String id, name, category, warehouse, storage_type;
    private String supplierId, supplierName;
    private double price;
    private int quantity, threshold;

    public Product() {} // For Firebase

    public Product(String id, String name, String category, double price, int quantity, int threshold, String warehouse, String storage_type, String supplierId, String supplierName) {
        this.id = id; this.name = name; this.category = category; this.price = price;
        this.quantity = quantity; this.threshold = threshold; 
        this.warehouse = warehouse; this.storage_type = storage_type;
        this.supplierId = supplierId; this.supplierName = supplierName;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public int getThreshold() { return threshold; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public String getWarehouse() { return warehouse; }
    public String getStorage_type() { return storage_type; }
    public String getSupplierId() { return supplierId; }
    public String getSupplierName() { return supplierName; }
}
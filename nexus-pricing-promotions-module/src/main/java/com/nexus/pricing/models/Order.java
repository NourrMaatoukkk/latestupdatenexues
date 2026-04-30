package com.nexus.pricing.models;

import com.nexus.models.Product;

public class Order {

    private int orderId;
    private Product product;
    private int quantity;

    public Order(int orderId, Product product, int quantity) {
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalBasePrice() {
        return product.getPrice() * quantity;
    }
}
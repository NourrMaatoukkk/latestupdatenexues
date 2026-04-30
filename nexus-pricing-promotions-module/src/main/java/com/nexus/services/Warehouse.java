package com.nexus.services;

import com.nexus.models.Product;
import java.util.Enumeration;
import java.util.Vector;

public class Warehouse {
    private String name;
    private Vector<Product> stock = new Vector<>(); // Vector allows easy Enumeration

    public Warehouse(String name) { this.name = name; }

    // PATTERN: Iterator (Returning Enumeration as required)
    public Enumeration<Product> getProducts() {
        return stock.elements();
    }

    public void addProduct(Product p) { stock.add(p); }
}
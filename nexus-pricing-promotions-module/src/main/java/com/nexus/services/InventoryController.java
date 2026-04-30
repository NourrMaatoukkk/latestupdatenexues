package com.nexus.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.models.Manager;
import com.nexus.models.Product;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @GetMapping("/list")
    public List<Product> getLiveInventory() {
        List<Product> list = new ArrayList<>();
        try {
            var db = FirebaseService.getInstance().getDb();
            var future = db.collection("inventory").get();
            var documents = future.get().getDocuments();

            Manager current = SessionManager.getInstance().getCurrentManager();
            String section = (current == null) ? "" : current.getSection();

            for (var doc : documents) {
                Product p = doc.toObject(Product.class);
                if (section == null || section.isBlank()) continue;
                if (section.equalsIgnoreCase("All") || section.equalsIgnoreCase(p.getWarehouse())) {
                    list.add(p);
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching from Firebase: " + e.getMessage());
        }
        return list;
    }
}
package com.nexus.pricing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.models.Product;
import com.nexus.pricing.manager.PricingManager;
import com.nexus.pricing.models.Order;
import com.nexus.pricing.strategies.BlackFridayPricing;
import com.nexus.services.FirebaseService;

@RestController
@RequestMapping("/api/pricing")
public class PricingController {

    @GetMapping("/calculate/{id}")
    public double calculate(@PathVariable String id) {

        try {

            var db = FirebaseService.getInstance().getDb();

            var doc = db.collection("inventory")
                        .document(id)
                        .get()
                        .get();

            Product product = doc.toObject(Product.class);

            Order order = new Order(1, product, 1);

            PricingManager pricingManager = new PricingManager();

            return pricingManager.calculateFinalPrice(
                    order,
                    new BlackFridayPricing()
            );

        } catch (Exception e) {
            return -1;
        }
    }
}
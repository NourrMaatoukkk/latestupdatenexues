package com.nexus.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private final RestockSystem restockSystem;

    public NotificationController(RestockSystem restockSystem) {
        this.restockSystem = restockSystem;
    }

    @GetMapping("/notifications")
    public List<String> getNotifications() {
        return restockSystem.getNotificationLogs();
    }

    @PostMapping("/request-restock")
    public ResponseEntity<String> requestRestock(
            @RequestParam(value = "branch", required = false) String branch,
            @RequestParam(value = "item", required = false) String item,
            @RequestBody(required = false) RestockRequest request) {

        String resolvedBranch = branch;
        String resolvedItem = item;

        if ((resolvedBranch == null || resolvedBranch.isBlank()) && request != null) {
            resolvedBranch = request.branch;
        }
        if ((resolvedItem == null || resolvedItem.isBlank()) && request != null) {
            resolvedItem = request.item;
        }

        if (resolvedBranch == null || resolvedBranch.isBlank() || resolvedItem == null || resolvedItem.isBlank()) {
            return ResponseEntity.badRequest().body("branch and item are required");
        }

        restockSystem.requestRestock(resolvedBranch, resolvedItem);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/contact-supplier")
    public ResponseEntity<String> contactSupplier(
            @RequestParam(value = "item", required = false) String item,
            @RequestBody(required = false) SupplierContactRequest request) {

        String resolvedItem = item;

        if ((resolvedItem == null || resolvedItem.isBlank()) && request != null) {
            resolvedItem = request.item;
        }

        if (resolvedItem == null || resolvedItem.isBlank()) {
            return ResponseEntity.badRequest().body("item is required");
        }

        // Resolve supplier from item name
        String supplier = resolveSupplierFromItem(resolvedItem);

        // Log M00 contacting the supplier
        restockSystem.logEvent("M00", resolvedItem, "SUPPLIER_CONTACTED", supplier, 
            "M00 CONTACTING SUPPLIER: " + supplier + " for " + resolvedItem);
        restockSystem.logEvent("M00", resolvedItem, "SUPPLIER_CONFIRMED", supplier, 
            "SUPPLIER CONFIRMED: " + supplier + " will begin restocking " + resolvedItem);

        return ResponseEntity.ok("Supplier contacted and confirmed for: " + resolvedItem);
    }

    private String resolveSupplierFromItem(String item) {
        String normalized = (item == null) ? "" : item.toLowerCase();
        if (normalized.contains("macbook") || normalized.contains("iphone")) {
            return "Apple Supplier";
        } else if (normalized.contains("rtx") || normalized.contains("nvidia")) {
            return "Nvidia Supplier";
        } else if (normalized.contains("samsung")) {
            return "Samsung Supplier";
        }
        return "General Supplier";
    }

    public static class RestockRequest {
        public String branch;
        public String item;
    }

    public static class SupplierContactRequest {
        public String item;
    }
}

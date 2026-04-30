package com.nexus.services;

import java.util.HashMap;
import java.util.Map;

public class GlobalManagerM00 implements ManagerObserver {

    private final RestockSystem restockSystem;
    private final Map<String, String> supplierMap = new HashMap<>();

    public GlobalManagerM00(RestockSystem restockSystem) {
        this.restockSystem = restockSystem;
        supplierMap.put("macbook", "Apple Supplier");
        supplierMap.put("rtx 4090", "Nvidia Supplier");
        supplierMap.put("iphone", "Apple Supplier");
        supplierMap.put("samsung", "Samsung Supplier");
    }

    @Override
    public void update(String branch, String item) {
        String supplier = resolveSupplier(item);

        restockSystem.logEvent(branch, item, "APPROVED", supplier, "M00 APPROVED: " + item + " for " + branch);
        restockSystem.logEvent(branch, item, "SUPPLIER_CONTACTED", supplier, "M00 CONTACTED: " + supplier + " for " + item);
        restockSystem.logEvent(branch, item, "SUPPLIER_CONFIRMED", supplier, "SUPPLIER CONFIRMED: " + supplier + " approved new stock for " + item);
        restockSystem.logEvent(branch, item, "BRANCH_UPDATED", supplier, "M00 UPDATED BRANCH: " + branch + " confirmed incoming stock for " + item);
    }

    private String resolveSupplier(String item) {
        String normalized = (item == null) ? "" : item.toLowerCase();
        for (Map.Entry<String, String> entry : supplierMap.entrySet()) {
            if (normalized.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "General Supplier";
    }
}

package com.nexus.pricing.singleton;

public class GlobalConfigManager {

    private static GlobalConfigManager instance;

    private double taxRate = 0.14;

    private GlobalConfigManager() {
    }

    public static synchronized GlobalConfigManager getInstance() {
        if (instance == null) {
            instance = new GlobalConfigManager();
        }
        return instance;
    }

    public double getTaxRate() {
        return taxRate;
    }
}
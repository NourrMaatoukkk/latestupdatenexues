package com.nexus;

import org.springframework.boot.CommandLineRunner; // NEW IMPORT
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.nexus.models.Product;
import com.nexus.services.DatabaseSeeder;
import com.nexus.services.EmailAlertSystem;
import com.nexus.services.GlobalManagerM00;
import com.nexus.services.InventoryManager;
import com.nexus.services.InventoryService;
import com.nexus.services.RestockSystem;
import com.nexus.services.WarehouseLogger;

@SpringBootApplication
public class MainApp {
    public static void main(String[] args) {
        DatabaseSeeder.uploadManagers();
        DatabaseSeeder.uploadSuppliers();
        // 1. RUN THIS ONCE to fill your Firebase database with the CSV data
        // After you see "Cloud Sync SUCCESS" in the terminal, you can add // back to this line.
        DatabaseSeeder.uploadInventory(); 

        // 2. Starts the Web Server
        SpringApplication.run(MainApp.class, args);

        InventoryManager dispatcher = new InventoryManager();
        dispatcher.addObserver(new EmailAlertSystem());
        dispatcher.addObserver(new WarehouseLogger());

        InventoryService service = new InventoryService(dispatcher);
        Product p = new Product("D001", "Elite Laptop", "Electronics", 2000, 2, 5, "Cairo", "Cold", "S01", "Nex Supply Cairo");
        service.updateStockInCloud(p);
        
        System.out.println("====================================================");
        System.out.println("NEXUS HUB IS ONLINE: http://localhost:8080/index.html");
        System.out.println("====================================================");
    }

    @Bean
    @SuppressWarnings("unused")
    CommandLineRunner observerBootstrap(RestockSystem system) {
        return args -> system.attach(new GlobalManagerM00(system));
    }
}
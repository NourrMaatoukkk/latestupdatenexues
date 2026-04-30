package com.nexus.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.models.Manager;
import com.nexus.models.Supplier;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @GetMapping("/list")
    public List<Supplier> listSuppliers() {
        List<Supplier> out = new ArrayList<>();
        try {
            var db = FirebaseService.getInstance().getDb();
            var docs = db.collection("suppliers").get().get().getDocuments();

            Manager current = SessionManager.getInstance().getCurrentManager();
            String section = (current == null) ? "" : current.getSection();

            for (var d : docs) {
                Supplier s = d.toObject(Supplier.class);
                if (section == null || section.isBlank()) continue;
                if (section.equalsIgnoreCase("All") || section.equalsIgnoreCase(s.getBranch())) {
                    out.add(s);
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching suppliers: " + e.getMessage());
        }
        return out;
    }

    @PostMapping("/create")
    public String createSupplier(@RequestBody Supplier s) {
        try {
            var db = FirebaseService.getInstance().getDb();
            if (s.getId() == null || s.getId().isBlank()) return "BAD_REQUEST";
            db.collection("suppliers").document(s.getId()).set(s).get();
            return "OK";
        } catch (Exception e) {
            System.err.println("Error creating supplier: " + e.getMessage());
            return "ERROR";
        }
    }
}

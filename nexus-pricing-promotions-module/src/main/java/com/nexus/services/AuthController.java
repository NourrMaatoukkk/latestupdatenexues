package com.nexus.services;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.models.Manager;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public String login(@RequestBody Manager loginRequest) throws Exception {
        var db = FirebaseService.getInstance().getDb();
        String managerId = loginRequest.getId() == null ? "" : loginRequest.getId().trim().toUpperCase();
        if (managerId.isEmpty()) {
            return "UNAUTHORIZED";
        }
        
        // 1. Check Firebase if Manager ID exists
        var docRef = db.collection("managers").document(managerId).get();
        var doc = docRef.get();

        if (doc.exists()) {
            Manager m = new Manager();
            m.setId(managerId);

            String name = doc.getString("name");
            if (name == null || name.isBlank()) {
                name = doc.getString("Name");
            }
            m.setName(name);

            String section = doc.getString("section");
            if (section == null || section.isBlank()) {
                section = doc.getString("Section");
            }
            m.setSection(section);

            // 2. Set the Singleton Session
            SessionManager.getInstance().login(m);
            return "OK";
        } else {
            return "UNAUTHORIZED";
        }
    }

    @GetMapping({"/session", "/current"})
    public Manager getSession() {
        return SessionManager.getInstance().getCurrentManager();
    }
}
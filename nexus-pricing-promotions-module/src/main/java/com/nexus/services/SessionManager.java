package com.nexus.services;

import com.nexus.models.Manager;

public class SessionManager {
    private static volatile SessionManager instance;
    private Manager currentManager;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) instance = new SessionManager();
            }
        }
        return instance;
    }

    public void login(Manager m) { this.currentManager = m; }
    public Manager getCurrentManager() { return currentManager; }
    public boolean isLoggedIn() { return currentManager != null; }
}
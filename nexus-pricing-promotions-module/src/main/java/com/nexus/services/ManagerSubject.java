package com.nexus.services;

public interface ManagerSubject {
    void attach(ManagerObserver observer);
    void detach(ManagerObserver observer);
    void notifyObservers(String branch, String item);
}

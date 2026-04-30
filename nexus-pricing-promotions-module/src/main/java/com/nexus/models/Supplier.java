package com.nexus.models;

public class Supplier {
    private String id, name, contact, email, branch;

    public Supplier() {}

    public Supplier(String id, String name, String contact, String email, String branch) {
        this.id = id; this.name = name; this.contact = contact; this.email = email; this.branch = branch;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getEmail() { return email; }
    public String getBranch() { return branch; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setContact(String contact) { this.contact = contact; }
    public void setEmail(String email) { this.email = email; }
    public void setBranch(String branch) { this.branch = branch; }
}

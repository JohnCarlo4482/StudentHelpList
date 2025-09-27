package com.example.studentlist2025.models;

public class Contact {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String category;
    private boolean isFavorite;
    private long lastContact;

    public Contact(String name, String phone, String email, String category) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.category = category;
        this.isFavorite = false;
        this.lastContact = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getCategory() { return category; }
    public boolean isFavorite() { return isFavorite; }
    public long getLastContact() { return lastContact; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setCategory(String category) { this.category = category; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public void setLastContact(long lastContact) { this.lastContact = lastContact; }
}
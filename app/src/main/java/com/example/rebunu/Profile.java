package com.example.rebunu;

public class Profile {
    private String phone;
    private String email;
    private String username;
    private Double balance;
    private String role;
    private Rating rating;

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public Double getBalance() {
        return balance;
    }

    public String getRole() {
        return role;
    }

    public Rating getRating() {
        return rating;
    }

    void setPhone(String phone) {
        this.phone = phone;
    }

    void setEmail(String email) {
        this.email = email;
    }

    void setUsername(String username) {
        this.username = username;
    }

    void setBalance(Double balance) {
        this.balance = balance;
    }

    void setRole(String role) {
        this.role = role;
    }

    void setRating(Rating rating) {
        this.rating = rating;
    }
}

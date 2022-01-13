package com.happylearn.dao;

public class SimpleUserData {
    String username;

    public SimpleUserData(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    String role;

    @Override
    public String toString() {
        return "UserData{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}

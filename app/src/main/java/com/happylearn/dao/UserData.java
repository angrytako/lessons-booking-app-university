package com.happylearn.dao;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

public class UserData {
    ObservableField<String> username;

    public UserData(String username, String role) {
        this.username = new ObservableField<>(username);
        this.role = new ObservableField<>(role);
    }

    public ObservableField<String> getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public ObservableField<String> getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    ObservableField<String> role;

    @Override
    public String toString() {
        return "UserData{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}

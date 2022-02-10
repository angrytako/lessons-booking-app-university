package com.happylearn.dao;

public class Utente {
    private String username;
    private String password;
    private String ruolo;

    public Utente(String username, String password, String ruolo) {
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;                             // solo 'amministratore' o 'cliente'
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRuolo() {
        return ruolo;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", ruolo='" + ruolo + '\'' +
                '}';
    }
}

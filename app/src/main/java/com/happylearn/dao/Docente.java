package com.happylearn.dao;

public class Docente {
    private int id;
    private String nome;
    private String cognome;
    private boolean rimosso;

    public Docente(int id, String nome, String cognome, boolean rimosso) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.rimosso = rimosso;     //volendo già a false
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public boolean isRimosso() {
        return rimosso;
    }

    @Override
    public String toString() {
        return "Docente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", rimosso=" + rimosso +
                '}';
    }
}

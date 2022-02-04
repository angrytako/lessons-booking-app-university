package com.happylearn.dao;

import androidx.databinding.ObservableField;

public class BindablePrenotazione {
    ObservableField<String> corso;
    ObservableField<String> ruolo, nomeDocente, cognomeDocente;
    ObservableField<Integer> idDocente;
    ObservableField<String> utente;
    ObservableField<String> stato;
    ObservableField<Integer> giorno;			// 0: lunedì, 1: martedì, 2: mercoledì, 3: giovedì, 4: venerdì
    ObservableField<Integer> orario;			// 0: 15-16, 1: 16-17,  2: 17-18,    3: 18-19

    public BindablePrenotazione(Prenotazione booking){
        this.corso = new ObservableField<>(booking.getCorso());
        this.ruolo = new ObservableField<>(booking.getRuolo());
        this.nomeDocente = new ObservableField<>(booking.getNomeDocente());
        this.cognomeDocente = new ObservableField<>(booking.getCognomeDocente());
        this.idDocente = new ObservableField<>(booking.getIdDocente());
        this.utente = new ObservableField<>(booking.getUtente());
        this.stato = new ObservableField<>(booking.getStato());
        this.giorno = new ObservableField<>(booking.getGiorno());
        this.orario = new ObservableField<>(booking.getOrario());
    }


    public BindablePrenotazione(String corso, String ruolo, String nomeDocente, String cognomeDocente, int idDocente, String utente, String stato, int giorno, int orario) {
        this.corso = new ObservableField<>(corso);
        this.ruolo = new ObservableField<>(ruolo);
        this.nomeDocente = new ObservableField<>(nomeDocente);
        this.cognomeDocente = new ObservableField<>(cognomeDocente);
        this.idDocente = new ObservableField<>(idDocente);
        this.utente = new ObservableField<>(utente);
        this.stato = new ObservableField<>(stato);
        this.giorno = new ObservableField<>(giorno);
        this.orario = new ObservableField<>(orario);
    }

    public Prenotazione getSerializable(){
        return new Prenotazione(corso.get(),idDocente.get(),nomeDocente.get(),cognomeDocente.get(),ruolo.get(),utente.get(), stato.get(), giorno.get(), orario.get());
    }

    public ObservableField<String> getCorso() {
        return corso;
    }

    public void setCorso(ObservableField<String> corso) {
        this.corso = corso;
    }

    public ObservableField<String> getRuolo() {
        return ruolo;
    }

    public void setRuolo(ObservableField<String> ruolo) {
        this.ruolo = ruolo;
    }

    public ObservableField<String> getNomeDocente() {
        return nomeDocente;
    }

    public void setNomeDocente(ObservableField<String> nomeDocente) {
        this.nomeDocente = nomeDocente;
    }

    public ObservableField<String> getCognomeDocente() {
        return cognomeDocente;
    }

    public void setCognomeDocente(ObservableField<String> cognomeDocente) {
        this.cognomeDocente = cognomeDocente;
    }

    public ObservableField<Integer> getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(ObservableField<Integer> idDocente) {
        this.idDocente = idDocente;
    }

    public ObservableField<String> getUtente() {
        return utente;
    }

    public void setUtente(ObservableField<String> utente) {
        this.utente = utente;
    }

    public ObservableField<String> getStato() {
        return stato;
    }

    public void setStato(ObservableField<String> stato) {
        this.stato = stato;
    }

    public ObservableField<Integer> getGiorno() {
        return giorno;
    }

    public void setGiorno(ObservableField<Integer> giorno) {
        this.giorno = giorno;
    }

    public ObservableField<Integer> getOrario() {
        return orario;
    }

    public void setOrario(ObservableField<Integer> orario) {
        this.orario = orario;
    }



}

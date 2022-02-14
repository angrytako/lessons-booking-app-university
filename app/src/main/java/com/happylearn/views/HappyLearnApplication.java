package com.happylearn.views;

import android.app.Application;

import com.happylearn.adapters.PrenotazioniAdapter;
import com.happylearn.dao.BindablePrenotazione;
import com.happylearn.dao.Slot;
import com.happylearn.dao.UserData;

import java.util.List;

public class HappyLearnApplication extends Application {
    private UserData userData;
    private List<BindablePrenotazione> myBookings = null;
    private List<BindablePrenotazione> bookings = null;
    private PrenotazioniAdapter bookingsAdapter, myBookingsAdapter;
    private Slot slot;


    public PrenotazioniAdapter getBookingsAdapter() {
        return bookingsAdapter;
    }

    public void setBookingsAdapter(PrenotazioniAdapter bookingsAdapter) {
        this.bookingsAdapter = bookingsAdapter;
    }

    public PrenotazioniAdapter getMyBookingsAdapter() {
        return myBookingsAdapter;
    }

    public void setMyBookingsAdapter(PrenotazioniAdapter myBookingsAdapter) {
        this.myBookingsAdapter = myBookingsAdapter;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public UserData getUserData(){
        return userData;
    }
    private String sessionCookie = null;
    public void setUserData(UserData userData){
        this.userData = userData;
    }

    public List<BindablePrenotazione> getBookings() {
        return bookings;
    }

    public void setBookings(List<BindablePrenotazione> bookings) {
        this.bookings = bookings;
    }

    public List<BindablePrenotazione> getMyBookings() {
        return myBookings;
    }



    public void setMyBookings(List<BindablePrenotazione> myBookings) {
        this.myBookings = myBookings;
    }

    public String getSessionCookie() {
        return sessionCookie;
    }

    public void setSessionCookie(String sessionCookie) {
        this.sessionCookie = sessionCookie;
    }
}

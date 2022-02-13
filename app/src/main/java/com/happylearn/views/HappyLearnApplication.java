package com.happylearn.views;

import android.app.Application;

import com.happylearn.dao.BindablePrenotazione;
import com.happylearn.dao.BindableSlots;
import com.happylearn.dao.UserData;

import java.util.List;

public class HappyLearnApplication extends Application {
    private UserData userData;
    private List<BindablePrenotazione> myBookings = null;
    private List<BindableSlots> availableSlots = null;
    private List<BindablePrenotazione> bookings = null;
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

    public List<BindableSlots> getSlots(){
        return availableSlots;
    }

    public void setSlots(List<BindableSlots> availableSlots){
        this.availableSlots = availableSlots;
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

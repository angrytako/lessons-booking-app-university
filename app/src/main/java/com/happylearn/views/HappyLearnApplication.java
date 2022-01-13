package com.happylearn.views;

import android.app.Application;

import androidx.databinding.Bindable;

import com.happylearn.dao.UserData;

public class HappyLearnApplication extends Application {
    private UserData userData;
    public UserData getUserData(){
        return userData;
    }
    public void setUserData(UserData userData){
        this.userData = userData;
    }




}

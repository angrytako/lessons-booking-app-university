package com.happylearn.bindings;

import android.view.View;

import androidx.databinding.ObservableField;

import com.happylearn.R;
import com.happylearn.dao.UserData;

public class BindingUtils {
    private static String[] days = {"Lunedì","Martedì","Mercoledì","Giovedì","Venerdì"};
    private static String[] hours = {"15-16","16-17","17-18","18-19"};
    public static boolean isUserLogged(UserData user){
        return user.getRole().get().equals("amministratore") ||  user.getRole().get().equals("cliente");
    }
    public static String getDay(int dayNum){
        return  days[dayNum];
    }
    public static String getHours(int hourNum){
        return  hours[hourNum];
    }

    public static int getVisibility(String status){
        if(status.equals("attiva")) return View.VISIBLE;
        else return View.INVISIBLE;
    }
    public static boolean isActive(String status){
        if(status.equals("attiva")) return true;
        return false;
    }
    public static boolean isCompleted(String status){
        if(status.equals("effettuata"))  return true;
        return false;
    }
    public static boolean isUserAdmin(UserData user){
        return user.getRole().get().equals("amministratore");
    }
    public static int getColour(String status){
        if(status.equals("attiva")) return R.color.blue;
        else if(status.equals("effettuata")) return R.color.green;
        else return R.color.red;
    }
    public static String getFullName(ObservableField<String> name, ObservableField<String> surname){
      return  name.get() + " " + surname.get();
    };
}

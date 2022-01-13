package com.happylearn.bindings;

import com.happylearn.dao.UserData;

public class BindingUtils {
    public static boolean isUserLogged(UserData user){
        return user.getRole().get().equals("amministratore") ||  user.getRole().get().equals("cliente");
    }
    public static boolean isUserAdmin(UserData user){
        return user.getRole().get().equals("amministratore");
    }
}

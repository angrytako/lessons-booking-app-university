package com.happylearn.bindings;

import android.view.Menu;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import com.happylearn.R;
public class MenuController {
    private Menu menu;
    private ObservableField<String>  role;

    public MenuController(Menu menu, ObservableField<String> role) {
        this.menu = menu;
        this.role = role;
        setToRightLayout();
        role.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setToRightLayout();
            }
        });
    }
    private void setToRightLayout(){
        if(isUserClient())
            setMenuToClient();
        else if (isUserAdmin())
            setMenuToAdmin();
        else
            setMenuToGuest();
    }
    private boolean isUserClient(){
        return role.get().equals("cliente");
    }
    private  boolean isUserAdmin(){
        return role.get().equals("amministratore");
    }
    private void setAllToInvisible(){
        for(int i = 0; i<menu.size(); i++){
            menu.getItem(i).setVisible(false);
        }
    }
    private void setVisible(int[] ids){
        for(int id : ids){
            menu.findItem(id).setVisible(true);
        }
    }
    private void setMenuToGuest(){
        setAllToInvisible();
        int[] ids = {R.id.home_itm,R.id.login_itm};
        setVisible(ids);
    }
    private void setMenuToClient(){
        setAllToInvisible();
        int[] ids = {R.id.home_itm,R.id.logout_itm,R.id.mie_prenotazioni_itm};
        setVisible(ids);
    }
    private void setMenuToAdmin(){
        setAllToInvisible();
        int[] ids = {R.id.home_itm,R.id.logout_itm,R.id.mie_prenotazioni_itm,R.id.prenotazioni_itm};
        setVisible(ids);
    }
}

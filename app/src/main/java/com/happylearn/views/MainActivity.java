package com.happylearn.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.happylearn.R;
import com.happylearn.bindings.MenuController;
import com.happylearn.dao.UserData;
import com.happylearn.databinding.ActivityMainBinding;
import com.happylearn.databinding.NavHeaderBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mBinding;
    Fragment currentLoadedFragment = null;
    ActionBarDrawerToggle toggle = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        DrawerLayout navbar = findViewById(R.id.drawer_layout);
        NavigationView navbarItems = findViewById(R.id.navbar_items_nv);

        toggle = new ActionBarDrawerToggle(this, navbar, R.string.menu_open, R.string.menu_close);
        navbar.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        HomeFragment homeFrag = new HomeFragment();
        LoginFragment loginFrag = new LoginFragment();
        MiePrenotazioniFragment miePrenFrag = new MiePrenotazioniFragment();
        //setting fragment on view
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container, HomeFragment.class, null)
                .commit();
        //set initial userdata
        //TODO network request for this one
        UserData startingData = new UserData("Guest","guest");
        ((HappyLearnApplication) this.getApplication()).setUserData(startingData);

        //bind this classe's view
        mBinding.setUserData(startingData);

        //Bind to header
        View hv = mBinding.navbarItemsNv.getHeaderView(0);
        NavHeaderBinding.bind(hv).setUserData(startingData);
        //Bind to menu
        Menu mv = mBinding.navbarItemsNv.getMenu();
        MenuController mc = new MenuController(mv, startingData.getRole());
        Activity main = this;
        navbarItems.setNavigationItemSelectedListener((menuItem)->{
            switch (menuItem.getItemId()){
                case R.id.home_itm:
                    navbar.closeDrawers();
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .remove(getSupportFragmentManager().getFragments().get(0))
                            .add(R.id.fragment_container, HomeFragment.class, null)
                            .commit();
                    break;
                case R.id.login_itm:
                    navbar.closeDrawers();
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .remove(getSupportFragmentManager().getFragments().get(0))
                            .add(R.id.fragment_container, LoginFragment.class, null)
                            .commit();
                    break;
                case R.id.mie_prenotazioni_itm:
                    navbar.closeDrawers();
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .remove(getSupportFragmentManager().getFragments().get(0))
                            .add(R.id.fragment_container, MiePrenotazioniFragment.class, null)
                            .commit();
                    break;
                case R.id.prenotazioni_itm:
                    navbar.closeDrawers();
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .remove(getSupportFragmentManager().getFragments().get(0))
                            .add(R.id.fragment_container, PrenotazioniFragment.class, null)
                            .commit();
                    break;
            }
            return true;
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
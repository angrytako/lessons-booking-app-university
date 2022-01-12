package com.happylearn.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.happylearn.R;

public class MainActivity extends AppCompatActivity {
    Fragment currentLoadedFragment = null;
    ActionBarDrawerToggle toggle = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawerLayout navbar = findViewById(R.id.drawer_layout);
        NavigationView navbarItems = findViewById(R.id.navbar_items_nv);
        toggle = new ActionBarDrawerToggle(this, navbar, R.string.menu_open, R.string.menu_close);
        navbar.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        HomeFragment homeFrag = new HomeFragment();
        LoginFragment loginFrag = new LoginFragment();

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container, HomeFragment.class, null)
                .commit();
        String username =  ((HappyLearnApplication) this.getApplication()).getUsername();
        Activity main = this;
        navbarItems.setNavigationItemSelectedListener((menuItem)->{
            switch (menuItem.getItemId()){
                case R.id.home_itm:
                    navbar.closeDrawers();
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .remove(getSupportFragmentManager().getFragments().get(0))
                            .add(R.id.fragment_container, HomeFragment.class, null)
                            .commit();
                    break;
                case R.id.login_itm:
                    navbar.closeDrawers();
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .remove(getSupportFragmentManager().getFragments().get(0))
                            .add(R.id.fragment_container, LoginFragment.class, null)
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
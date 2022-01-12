package com.happylearn.views;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.happylearn.R;
import com.happylearn.dao.UserLogin;
import com.happylearn.routes.LoginController;

import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button loginBtn  = view.findViewById(R.id.login_btn);
        EditText usernameEt = view.findViewById(R.id.username_et);
        EditText passwordEt = view.findViewById(R.id.password_et);
        loginBtn.setOnClickListener((btnView)->{
            String username = usernameEt.getText().toString();
            String password = passwordEt.getText().toString();
            //TODO controllo username e pw inseriti
            UserLogin userLogin = new UserLogin(username,password);
            Log.d("NOODLE","PRESSED");
            LoginController controller = new LoginController(userLogin, getContext());
            controller.start();
        });

    }
}
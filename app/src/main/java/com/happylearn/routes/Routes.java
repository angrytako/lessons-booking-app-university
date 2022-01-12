package com.happylearn.routes;

import com.happylearn.R;
import com.happylearn.dao.SimpleMessage;
import com.happylearn.dao.UserData;
import com.happylearn.dao.UserLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Routes {
    @POST("Noodle_war/loginServlet")
    Call<UserData> login(@Body UserLogin userLogin);
}

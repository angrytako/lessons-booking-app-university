package com.happylearn.routes;

import com.happylearn.dao.SimpleUserData;
import com.happylearn.dao.UserLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Routes {
    @POST("Noodle_war/loginServlet")
    Call<SimpleUserData> login(@Body UserLogin userLogin);
}

package com.happylearn.routes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happylearn.dao.SimpleMessage;
import com.happylearn.dao.UserData;
import com.happylearn.dao.UserLogin;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginController implements Callback<UserData> {
    static final String BASE_URL =  "http://192.168.1.207:8080/";
    private  Context context;
    private UserLogin userLogin;
    public LoginController(UserLogin userLogin, Context context) {
        this.userLogin = userLogin;
        this.context = context;
    }

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d("NOODLE","I M HERE2");
        Routes gerritAPI = retrofit.create(Routes.class);
        Call<UserData> call = gerritAPI.login(this.userLogin);
        call.enqueue(this);


    }

    @Override
    public void onResponse(Call<UserData> call, Response<UserData> response) {
            if(response.isSuccessful()) {
                UserData userData = response.body();
                Log.d("NOODLE",response.body().toString());
                System.out.println(response.body().toString());
            } else{
                try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(context, jObjError.getString("error"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    public void onFailure(Call<UserData> call, Throwable t) {
        Log.d("NOODLE",  t.toString());
        t.printStackTrace();
    }
}

package com.happylearn.routes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happylearn.R;
import com.happylearn.dao.SimpleMessage;
import com.happylearn.dao.SimpleUserData;
import com.happylearn.dao.UserData;
import com.happylearn.dao.UserLogin;
import com.happylearn.views.HappyLearnApplication;
import com.happylearn.views.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginController implements Callback<SimpleUserData> {
    static final String BASE_URL =  "http://192.168.1.207:8080/";
    private  Context context;
    private UserLogin userLogin;
    private Activity activity;
    public LoginController(UserLogin userLogin, Context context,  Activity activity) {
        this.userLogin = userLogin;
        this.context = context;
        this.activity = activity;
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
        Call<SimpleUserData> call = gerritAPI.login(this.userLogin);
        call.enqueue(this);


    }

    @Override
    public void onResponse(Call<SimpleUserData> call, Response<SimpleUserData> response) {
            if(response.isSuccessful()) {
                SimpleUserData userData = response.body();
                UserData globalUserData = ((HappyLearnApplication)activity.getApplication()).getUserData();
                globalUserData.setUsername(userData.getUsername());
                globalUserData.setRole(userData.getRole());
                ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .remove(((AppCompatActivity)activity).getSupportFragmentManager().getFragments().get(0))
                        .add(R.id.fragment_container, HomeFragment.class, null)
                        .commit();
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
    public void onFailure(Call<SimpleUserData> call, Throwable t) {
        Log.d("NOODLE",  t.toString());
        t.printStackTrace();
    }
}

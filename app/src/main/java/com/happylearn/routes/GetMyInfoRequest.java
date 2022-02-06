package com.happylearn.routes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happylearn.R;
import com.happylearn.dao.Prenotazione;
import com.happylearn.dao.SimpleUserData;
import com.happylearn.dao.UserData;
import com.happylearn.dao.UserLogin;
import com.happylearn.routes.interceptors.GetAndSetSessionInterceptor;
import com.happylearn.routes.interceptors.SetSessionOnRequestInterceptor;
import com.happylearn.views.HappyLearnApplication;
import com.happylearn.views.HomeFragment;

import org.json.JSONObject;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetMyInfoRequest implements Callback<SimpleUserData> {
    private String BASE_URL;
    private Context context;
    private Activity activity;

    public GetMyInfoRequest( Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        BASE_URL = context.getString(R.string.BASE_URL);
    }

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //used to set the cookie on the request, if there is one
        SetSessionOnRequestInterceptor setSessionInterceptor = new SetSessionOnRequestInterceptor((HappyLearnApplication) activity.getApplication());

        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                //adding interceptor for session cookie
                .addInterceptor(setSessionInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Routes gerritAPI = retrofit.create(Routes.class);
        Call<SimpleUserData> call = gerritAPI.myInfo();
        call.enqueue(this);


    }

    @Override
    public void onResponse(Call<SimpleUserData> call, Response<SimpleUserData> response) {
        Log.d("HAPPY_RESP","quiiiiiiiiiiiiiiiiii");
        UserData globalUserData = ((HappyLearnApplication)activity.getApplication()).getUserData();
        if(response.isSuccessful()) {
            SimpleUserData userData = response.body();

            globalUserData.setUsername(userData.getUsername());
            globalUserData.setRole(userData.getRole());
        } else{
            globalUserData.setUsername("Guest");
            globalUserData.setRole("Guest");
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
        UserData globalUserData = ((HappyLearnApplication)activity.getApplication()).getUserData();
        globalUserData.setUsername("Guest");
        globalUserData.setRole("Guest");
        t.printStackTrace();
    }
}

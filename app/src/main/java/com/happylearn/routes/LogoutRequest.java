package com.happylearn.routes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happylearn.R;
import com.happylearn.dao.SimpleMessage;
import com.happylearn.routes.interceptors.SetSessionOnRequestInterceptor;
import com.happylearn.views.HappyLearnApplication;
import com.happylearn.views.HomeFragment;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogoutRequest  implements Callback<SimpleMessage> {
    private String BASE_URL;
    private Context context;
    private int bookingIndex;
    public LogoutRequest(Context context) {
        this.context = context;
        BASE_URL = context.getString(R.string.BASE_URL);

    }

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //used to set the cookie on the request, if there is one
        SetSessionOnRequestInterceptor setSessionInterceptor = new SetSessionOnRequestInterceptor((HappyLearnApplication)context.getApplicationContext());

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
        Call<SimpleMessage> call = gerritAPI.logout();
        call.enqueue(this);


    }

    @Override
    public void onResponse(Call<SimpleMessage> call, Response<SimpleMessage> response) {
        if(response.isSuccessful()) {
            SimpleMessage message = response.body();
            Activity activity = (Activity)context;
            SharedPreferences sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
            sharedPreferences.edit().remove("SESSION");
            ((HappyLearnApplication)context.getApplicationContext()).getUserData().setRole("guest");
            ((HappyLearnApplication)context.getApplicationContext()).getUserData().setUsername("guest");
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
    public void onFailure(Call<SimpleMessage> call, Throwable t) {
        Log.d("NOODLE",  t.toString());
        Toast.makeText(context, "errore inatteso", Toast.LENGTH_LONG).show();
        t.printStackTrace();
    }
}

package com.happylearn.routes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happylearn.R;
import com.happylearn.dao.Prenotazione;
import com.happylearn.dao.Slot;
import com.happylearn.dao.Utente;
import com.happylearn.routes.interceptors.SetSessionOnRequestInterceptor;
import com.happylearn.views.HappyLearnApplication;

import org.json.JSONObject;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class allUtentiHomeRequest implements Callback<List<Utente>>{

private String BASE_URL;
private Context context;
private Activity activity;
private String username;
private String role;
private Slot slot;
private RecyclerView recyclerViewUtenti;
private RecyclerView recyclerViewCheckBox;
private Button prenota;
private List<Prenotazione> miePrenotazioni;
    public allUtentiHomeRequest(Context context, Activity activity, Slot slot, RecyclerView recyclerViewCheckBox,
                                String role, String username, RecyclerView recyclerViewUtenti, Button prenota, List<Prenotazione> miePrenotazioni) {
        this.context = context;
        this.activity = activity;
        this.slot = slot;
        this.recyclerViewCheckBox = recyclerViewCheckBox;
        this.role = role;
        this.username = username;
        this.recyclerViewUtenti = recyclerViewUtenti;
        BASE_URL = context.getString(R.string.BASE_URL);
        this.prenota=prenota;
        this.miePrenotazioni=miePrenotazioni;
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
        Call<List<Utente>> call = gerritAPI.allUsers();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Utente>> call, Response<List<Utente>> response) {
        if(response.isSuccessful()) {
            List<Utente> utenti = response.body();

            allPrenotazioniHomeRequest allPrenotazioniHome =
                    new allPrenotazioniHomeRequest(context, activity, slot, recyclerViewCheckBox, role,
                            username, recyclerViewUtenti ,utenti,prenota,miePrenotazioni);
            allPrenotazioniHome.start();
        }


        else{
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(context, jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<List<Utente>> call, Throwable t) {
        Log.d("NOODLE",  t.toString());
        Toast.makeText(context, "errore inatteso", Toast.LENGTH_LONG).show();
        t.printStackTrace();
    }


}


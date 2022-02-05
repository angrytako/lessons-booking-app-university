package com.happylearn.routes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happylearn.R;
import com.happylearn.adapters.PrenotazioniAdapter;
import com.happylearn.dao.BindablePrenotazione;
import com.happylearn.dao.Prenotazione;
import com.happylearn.dao.UserLogin;
import com.happylearn.routes.interceptors.SetSessionOnRequestInterceptor;
import com.happylearn.views.HappyLearnApplication;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrenotazioniRequest implements Callback<List<Prenotazione>> {
    private String BASE_URL;
    private Context context;
    private UserLogin userLogin;
    private Activity activity;
    private RecyclerView prenotazioni;
    public PrenotazioniRequest(Context context, Activity activity,  RecyclerView miePrenotazioni) {
        this.userLogin = userLogin;
        this.context = context;
        this.activity = activity;
        BASE_URL = context.getString(R.string.BASE_URL);
        this.prenotazioni = miePrenotazioni;
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
        Call<List<Prenotazione>> call = gerritAPI.prenotazioni();
        call.enqueue(this);


    }

    @Override
    public void onResponse(Call<List<Prenotazione>> call, Response<List<Prenotazione>> response) {
        if(response.isSuccessful()) {
            List<Prenotazione> prenotazioni = response.body();
            ArrayList<BindablePrenotazione> bindablePrenotazioni = new ArrayList<>();
            for (Prenotazione p : prenotazioni)
                bindablePrenotazioni.add(new BindablePrenotazione(p));
            ((HappyLearnApplication)activity.getApplication()).setBookings(bindablePrenotazioni);
            // Create adapter passing in the sample user data
            PrenotazioniAdapter adapter = new PrenotazioniAdapter( ((HappyLearnApplication)activity.getApplication()).getBookings(), "bookings");

            // Attach the adapter to the recyclerview to populate items
            this.prenotazioni.setAdapter(adapter);
            // Set layout manager to position the items
            this.prenotazioni.setLayoutManager(new LinearLayoutManager(context));
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
    public void onFailure(Call<List<Prenotazione>> call, Throwable t) {
        Log.d("NOODLE",  t.toString());
        Toast.makeText(context, "errore inatteso", Toast.LENGTH_LONG).show();
        t.printStackTrace();
    }
}

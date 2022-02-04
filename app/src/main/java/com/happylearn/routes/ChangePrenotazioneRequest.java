package com.happylearn.routes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happylearn.R;
import com.happylearn.adapters.PrenotazioniAdapter;
import com.happylearn.dao.BindablePrenotazione;
import com.happylearn.dao.Prenotazione;
import com.happylearn.dao.SimpleMessage;
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

public class ChangePrenotazioneRequest implements Callback<SimpleMessage> {
    private String BASE_URL;
    private  Context context;
    private List<BindablePrenotazione> bookings;
    private Prenotazione newBooking;
    RecyclerView.Adapter<PrenotazioniAdapter.ViewHolder> adapter;
    private int bookingIndex;
    public ChangePrenotazioneRequest(Context context, List<BindablePrenotazione> bookings, int bookingIndex, Prenotazione newBooking,RecyclerView.Adapter<PrenotazioniAdapter.ViewHolder> adapter) {
        this.context = context;
        this.bookings = bookings;
        this.bookingIndex = bookingIndex;
        BASE_URL = context.getString(R.string.BASE_URL);
        this.newBooking = newBooking;
        this.adapter = adapter;
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
        Call<SimpleMessage> call = gerritAPI.changePrenotazione(newBooking);
        call.enqueue(this);


    }

    @Override
    public void onResponse(Call<SimpleMessage> call, Response<SimpleMessage> response) {
        if(response.isSuccessful()) {
            SimpleMessage message = response.body();
            bookings.get(bookingIndex).setStato(newBooking.getStato());
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

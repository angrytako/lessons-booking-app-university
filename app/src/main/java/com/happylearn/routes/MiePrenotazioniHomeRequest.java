package com.happylearn.routes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happylearn.R;
import com.happylearn.adapters.HomePageAdapter;
import com.happylearn.dao.BindableSlots;
import com.happylearn.dao.Docente;
import com.happylearn.dao.Prenotazione;
import com.happylearn.dao.Slot;
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

public class MiePrenotazioniHomeRequest implements Callback<List<Prenotazione>> {
    private String BASE_URL;
    private Context context;
    private Activity activity;
    private String username;
    private List<List<List<Slot>>> availableSlotsForDayandTime;
    private RecyclerView viewSlots;
    private TabLayout tabHome;


    public MiePrenotazioniHomeRequest(Context context, Activity activity, String username, List<List<List<Slot>>>
            availableSlotsForDayandTime, RecyclerView viewSlots, TabLayout tabHome) {
        this.context = context;
        this.activity = activity;
        this.username = username;
        this.availableSlotsForDayandTime = availableSlotsForDayandTime;
        this.viewSlots = viewSlots;
        BASE_URL = context.getString(R.string.BASE_URL);
        this.tabHome=tabHome;
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
        Call<List<Prenotazione>> call = gerritAPI.myPrenotazioni(username);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Prenotazione>> call, Response<List<Prenotazione>> response) {
        if (response.isSuccessful()) {
            List<Prenotazione> prenotazioni = response.body();

            viewBooking(availableSlotsForDayandTime.get(0),0,prenotazioni);

            tabHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewBooking(availableSlotsForDayandTime.get(tab.getPosition()),tab.getPosition(),prenotazioni);
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) { }
                @Override
                public void onTabReselected(TabLayout.Tab tab) { }
            });

        } else {
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
        Log.d("NOODLE", t.toString());
        Toast.makeText(context, "errore inatteso", Toast.LENGTH_LONG).show();
        t.printStackTrace();
    }



    private void viewBooking(List<List<Slot>> slotForTime,int day , List<Prenotazione> prenotazioni) {
        List<BindableSlots> bindableSlots = new ArrayList<>();

        for(int i = 0; i<slotForTime.size(); i++){
            for (Slot slot : slotForTime.get(i)) {
                Boolean flagJustExistReservation = false;
                Prenotazione prenotazione = null;
                for (Prenotazione p : prenotazioni) {
                    if (p.getGiorno() == day && p.getOrario() == i && !p.getStato().equals("cancellata")) {
                        flagJustExistReservation = true;
                        prenotazione = p;
                    }
                }
                if (flagJustExistReservation == false) {
                    bindableSlots.add(new BindableSlots(slot));
                }
            }
        }

        ((HappyLearnApplication)activity.getApplication()).setSlots(bindableSlots);

        // Create adapter passing in the sample user data
        HomePageAdapter adapter = new HomePageAdapter(((HappyLearnApplication)activity.getApplication()).getSlots(), "mySlots");

        // Attach the adapter to the recyclerview to populate items
        this.viewSlots.setAdapter(adapter);
        // Set layout manager to position the items
        this.viewSlots.setLayoutManager(new LinearLayoutManager(context));
    }
}

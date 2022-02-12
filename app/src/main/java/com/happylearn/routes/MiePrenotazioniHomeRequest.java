package com.happylearn.routes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happylearn.R;
import com.happylearn.dao.Docente;
import com.happylearn.dao.Prenotazione;
import com.happylearn.dao.Slot;
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

public class MiePrenotazioniHomeRequest implements Callback<List<Prenotazione>> {
    private String BASE_URL;
    private Context context;
    private Activity activity;
    private String username;
    private List<List<List<Slot>>> availableSlotsForDayandTime;
    private TextView ripetizioniHome;
    private TabLayout tabHome;


    public MiePrenotazioniHomeRequest(Context context, Activity activity, String username, List<List<List<Slot>>>
            availableSlotsForDayandTime, TextView ripetizioniHome,TabLayout tabHome) {
        this.context = context;
        this.activity = activity;
        this.username = username;
        this.availableSlotsForDayandTime = availableSlotsForDayandTime;
        this.ripetizioniHome = ripetizioniHome;
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


            int day;
            int time;
            if (availableSlotsForDayandTime.size() != 0) {
                /*
                day = availableSlot.get(0).getDay();
                time = availableSlot.get(0).getTime();
                ripetizioniHome.append("Giorno:" + day + "\n");
                ripetizioniHome.append("ora:" + time + "\n");

                for (int i = 0; i < availableSlot.size(); i++) {
                    if (day != availableSlot.get(i).getDay()) {
                        day = availableSlot.get(i).getDay();
                        ripetizioniHome.append("Giorno:" + day + "\n");
                    }
                    if (time != availableSlot.get(i).getTime()) {
                        time = availableSlot.get(i).getTime();
                        ripetizioniHome.append("ora:" + time + "\n");
                    }


                    Boolean flagJustExistReservation = false;
                    for (Prenotazione p : prenotazioni){
                        if (p.getGiorno()==day && p.getOrario()==time && !p.getStato().equals("cancellata")) flagJustExistReservation=true;
                    }

                    if (flagJustExistReservation == false){
                        /*
                        ripetizioniHome.append("corso:" + availableSlot.get(i).getCourse() + "\n");

                        ripetizioniHome.append("Docenti:" + "\n");
                        if (availableSlot.get(i).getTeacherList() != null) {
                            for (int j = 0; j < availableSlot.get(i).getTeacherList().size(); j++) {
                                ripetizioniHome.append(availableSlot.get(i).getTeacherList().get(j) + " ");
                            }
                            ripetizioniHome.append("\n");
                        }


                    }else
                    {
                        ripetizioniHome.append("Ho gi una prenotazione attiva su questo slot\n");
                    }
                }
            */
            }


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
        ripetizioniHome.setText("Ripetizioni disponibili di:"+HomeRequest.dayToString(day) +"\n");
        for(int i=0;i<slotForTime.size();i++){
            ripetizioniHome.append(HomeRequest.timeToString(i) +" -------------------------------------------------\n");
            for (Slot slot : slotForTime.get(i)){
                Boolean flagJustExistReservation = false;
                Prenotazione prenotazione =null;
                for (Prenotazione p : prenotazioni){
                    if (p.getGiorno()==day && p.getOrario()==i && !p.getStato().equals("cancellata")) {
                        flagJustExistReservation=true;
                        prenotazione = p;
                    }
                }
                if (flagJustExistReservation == false){
                    ripetizioniHome.append(slot.getCourse() +"\n");
                    for (Docente docente : slot.getTeacherList()){
                        ripetizioniHome.append("("+ docente.getId()+") " + docente.getNome() + " " + docente.getCognome() + "\n");
                    }
                }
                else{
                    ripetizioniHome.append("Prenotazione già attiva su: \nCorso: "+ prenotazione.getCorso()  +"\nDocente: ("
                            +prenotazione.getIdDocente()+") "+prenotazione.getNomeDocente() +" " + prenotazione.getCognomeDocente() +"\n" );
                    break;
                }
            }

        }

    }
}

package com.happylearn.routes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happylearn.R;
import com.happylearn.adapters.StringListAdapter;
import com.happylearn.dao.Docente;
import com.happylearn.dao.Prenotazione;
import com.happylearn.dao.Slot;
import com.happylearn.dao.Utente;
import com.happylearn.routes.interceptors.SetSessionOnRequestInterceptor;
import com.happylearn.views.EffettuaPrenotazioneFragment;
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

public class allPrenotazioniHomeRequest implements Callback<List<Prenotazione>> {
    private StringListAdapter adapter;
    private String BASE_URL;
    private Context context;
    private Activity activity;
    private String username;
    private String role;
    private Slot slot;
    private RecyclerView recyclerViewUtenti;
    private RecyclerView recyclerViewCheckBox;
    private List<Utente> utenti;
    private Button prenota;
    List<Prenotazione> miePrenotazioni;

    public allPrenotazioniHomeRequest(Context context, Activity activity, Slot slot, RecyclerView recyclerViewCheckBox,
                                      String role, String username, RecyclerView recyclerViewUtenti, List<Utente> utenti,
                                      Button prenota,List<Prenotazione> miePrenotazioni) {
        this.context = context;
        this.activity = activity;
        this.slot = slot;
        this.recyclerViewCheckBox = recyclerViewCheckBox;
        this.role = role;
        this.username = username;
        this.recyclerViewUtenti = recyclerViewUtenti;
        BASE_URL = context.getString(R.string.BASE_URL);
        this.utenti = utenti;
        this.prenota = prenota;
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
        Call<List<Prenotazione>> call = gerritAPI.prenotazioni();
        call.enqueue(this);


    }

    @Override
    public void onResponse(Call<List<Prenotazione>> call, Response<List<Prenotazione>> response) {
        if (response.isSuccessful()) {
            List<Prenotazione> allPrenotazioni = response.body();
            for(Prenotazione p : miePrenotazioni){
                allPrenotazioni.add(p);
            }
            //this.availableSlotsForDayandTime    this.utenti
            //testati ed arrivano i dati competi

            List<String> utentiSelected = new ArrayList<>();

            for (Utente u : utenti) {
                Boolean flagUtenteJustHaveReservation = false;
                for (Prenotazione p : allPrenotazioni) {
                    if (p.getUtente().equals(u.getUsername())) {
                        if (p.getGiorno() == slot.getDay() && p.getOrario() == slot.getTime() && !"cancelata".equals(p.getStato())) {
                            flagUtenteJustHaveReservation = true;
                        }
                    }
                }
                if (flagUtenteJustHaveReservation == false){
                    utentiSelected.add(u.getUsername());
                    Toast.makeText(context, u.getUsername(), Toast.LENGTH_LONG).show();
                }
            }

            recyclerViewUtenti.setLayoutManager(new LinearLayoutManager(context));
            adapter = new StringListAdapter(context, utentiSelected);
            // adapter.setClickListener(this.getContext());
            recyclerViewUtenti.setAdapter(adapter);

            prenota.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int choiseCheckBox = ((StringListAdapter) recyclerViewCheckBox.getAdapter()).getChois();
                    int choiseUtenti = ((StringListAdapter) recyclerViewUtenti.getAdapter()).getChois();

                    if (-1 == choiseCheckBox)
                        Toast.makeText(context, "Per effettuare la prenotazione devi selezionare un Docente", Toast.LENGTH_LONG).show();
                    else if (-1 == choiseUtenti) {
                        Toast.makeText(context, "Per effettuare la prenotazione devi selezionare un Utente", Toast.LENGTH_LONG).show();

                    } else {
                        CheckBox checkBoxSelected = (CheckBox) recyclerViewCheckBox.getLayoutManager().findViewByPosition(choiseCheckBox).findViewById(R.id.docenti_List_Item);
                        CheckBox checkBoxUtenti = (CheckBox) recyclerViewUtenti.getLayoutManager().findViewByPosition(choiseCheckBox).findViewById(R.id.docenti_List_Item);
                        String utenteSelected = checkBoxUtenti.getText().toString();
                        Docente docenteSelected = EffettuaPrenotazioneFragment.parserDocente(checkBoxSelected.getText().toString());
                        DoPrenotazioneRequest availableSlot = new DoPrenotazioneRequest(context, activity,
                                new Prenotazione(slot.getCourse(), docenteSelected.getId(), docenteSelected.getNome(), docenteSelected.getCognome(),
                                        role, utenteSelected, "attiva", slot.getDay(), slot.getTime()));
                        availableSlot.start();
                    }
                }
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


}

package com.happylearn.routes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happylearn.R;
import com.happylearn.adapters.PrenotazioniAdapter;
import com.happylearn.dao.Prenotazione;
import com.happylearn.dao.SimpleUserData;
import com.happylearn.dao.UserData;
import com.happylearn.dao.UserLogin;
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

public class MiePrenotazioniController implements Callback<List<Prenotazione>> {
    private String BASE_URL;
    private  Context context;
    private UserLogin userLogin;
    private Activity activity;
    private String username;
    private RecyclerView miePrenotazioni;
    public MiePrenotazioniController(Context context, Activity activity, String username,  RecyclerView miePrenotazioni) {
        this.userLogin = userLogin;
        this.context = context;
        this.activity = activity;
        this.username = username;
        BASE_URL = context.getString(R.string.BASE_URL);
        this.miePrenotazioni = miePrenotazioni;
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
        if(response.isSuccessful()) {
            List<Prenotazione> prenotazioni = response.body();
            Toast.makeText(context, "SUCCESS", Toast.LENGTH_LONG).show();
            ((HappyLearnApplication)activity.getApplication()).setBookings(prenotazioni);
            // Create adapter passing in the sample user data
            PrenotazioniAdapter adapter = new PrenotazioniAdapter( ((HappyLearnApplication)activity.getApplication()).getBookings());

            // Attach the adapter to the recyclerview to populate items
            miePrenotazioni.setAdapter(adapter);
            // Set layout manager to position the items
            miePrenotazioni.setLayoutManager(new LinearLayoutManager(context));
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

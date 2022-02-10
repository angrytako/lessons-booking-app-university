package com.happylearn.routes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happylearn.R;
import com.happylearn.dao.Slot;
import com.happylearn.dao.UserLogin;
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

public class HomeRequest  implements Callback<List<Slot>> {
    private String BASE_URL;
    private Context context;
    private UserLogin userLogin;
    private Activity activity;
    private String username;
    private TextView ripetizioniHome;

    public HomeRequest(Context context, Activity activity, String username, TextView ripetizioniHome) {
        this.userLogin = userLogin;
        this.context = context;
        this.activity = activity;
        this.username = username;
        BASE_URL = context.getString(R.string.BASE_URL);
        this.ripetizioniHome = ripetizioniHome;
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
        //TODO avaibleSlot richiede un argomento *forse*
        Call<List<Slot>> call = gerritAPI.availableSlots();
        call.enqueue(this);

    }



    @Override
    public void onResponse(Call<List<Slot>> call, Response<List<Slot>> response) {
        if(response.isSuccessful()) {
            List<Slot> availableSlot = response.body();
            System.out.println("*****************************************************************************************************************");
            System.out.println(availableSlot);

            /*
            ArrayList<BindablePrenotazione> bindablePrenotazioni = new ArrayList<>();
            for (Prenotazione p : prenotazioni)
                bindablePrenotazioni.add(new BindablePrenotazione(p));
            ((HappyLearnApplication)activity.getApplication()).setMyBookings(bindablePrenotazioni);
            // Create adapter passing in the sample user data
            PrenotazioniAdapter adapter = new PrenotazioniAdapter( ((HappyLearnApplication)activity.getApplication()).getMyBookings(), "myBookings");

            // Attach the adapter to the recyclerview to populate items
            miePrenotazioni.setAdapter(adapter);
            // Set layout manager to position the items
            miePrenotazioni.setLayoutManager(new LinearLayoutManager(context));
            */


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
    public void onFailure(Call<List<Slot>> call, Throwable t) {
        Log.d("NOODLE",  t.toString());
        Toast.makeText(context, "errore inatteso", Toast.LENGTH_LONG).show();
        t.printStackTrace();
    }




}

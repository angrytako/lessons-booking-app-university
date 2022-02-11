package com.happylearn.routes;

import android.app.Activity;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
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

public class HomeRequest implements Callback<List<Slot>> {
    private String BASE_URL;
    private Context context;
    private UserLogin userLogin;
    private Activity activity;
    private String username;
    private TextView textViewHome;
    private TabLayout tabHome;
    private String role;
    private List<Slot> availableSlot;

    public HomeRequest(Context context, Activity activity, String username, String role, TextView textViewHome, TabLayout tabLayout) {
        this.userLogin = userLogin;
        this.context = context;
        this.activity = activity;
        this.username = username;
        BASE_URL = context.getString(R.string.BASE_URL);
        this.textViewHome = textViewHome;
        this.tabHome = tabLayout;
        this.role = role;
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
        if (response.isSuccessful()) {
            textViewHome.setMovementMethod(new ScrollingMovementMethod());

            saveAvailableSlots(response);

            viewBookingsOfDay(0);

            tabHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewBookingsOfDay(tab.getPosition());
//                    Toast.makeText(context, "hai cliccato: " + tab.getPosition(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) { }

                @Override
                public void onTabReselected(TabLayout.Tab tab) { }
            });

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


        } else {
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(context, jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveAvailableSlots(Response<List<Slot>> response) {
        availableSlot = response.body();
        if (availableSlot != null) {
            if (role.equals("cliente")) {
                //servlet prenotazioni attive
                MiePrenotazioniHomeRequest prenotazioniController = new MiePrenotazioniHomeRequest(context, activity, username,availableSlot, textViewHome);
                prenotazioniController.start();
            }else if (role.equals("amministratore")){
                allUtentiHomeRequest allUtentiHome = new allUtentiHomeRequest(context, activity, username,availableSlot, textViewHome);
                allUtentiHome.start();
            }
        }
    }

    @Override
    public void onFailure(Call<List<Slot>> call, Throwable t) {
        Log.d("NOODLE", t.toString());
        Toast.makeText(context, "errore inatteso", Toast.LENGTH_LONG).show();
        t.printStackTrace();
    }

    private void viewBookingsOfDay(int day) {
        Toast.makeText(context, "dovrei visualizzare le prenotazioni del giorno: " + day, Toast.LENGTH_SHORT).show();
        int time ;

        if (availableSlot.size() != 0) {
            //day = availableSlot.get(0).getDay();
            time = availableSlot.get(0).getTime();
            textViewHome.append("Giorno:" + day + "\n");
            textViewHome.append("Ora:" + time + "\n");

            for (int i = 0; i < availableSlot.size(); i++) {
                if (day == availableSlot.get(i).getDay()) {
                    if (time != availableSlot.get(i).getTime()) {
                        time = availableSlot.get(i).getTime();
                        textViewHome.append("Ora:" + time + "\n");
                    }
                    textViewHome.append("Corso:" + availableSlot.get(i).getCourse() + "\n");

                    textViewHome.append("con Docenti:" + "\n");
                    if (availableSlot.get(i).getTeacherList() != null) {
                        for (int j = 0; j < availableSlot.get(i).getTeacherList().size(); j++) {
                            textViewHome.append(availableSlot.get(i).getTeacherList().get(j) + " ");
                        }
                        textViewHome.append("\n");
                    }
                }
            }
        }
    }
}

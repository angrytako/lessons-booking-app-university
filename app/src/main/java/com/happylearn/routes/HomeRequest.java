package com.happylearn.routes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happylearn.R;
import com.happylearn.adapters.HomePageAdapter;
import com.happylearn.dao.Slot;
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

public class HomeRequest implements Callback<List<Slot>> {
    private String BASE_URL;
    private Context context;
    private UserLogin userLogin;
    private Activity activity;
    private String username;
    private RecyclerView viewSlots;
    private TabLayout tabHome;
    private String role;

    public HomeRequest(Context context, Activity activity, String username, String role, RecyclerView viewSlots, TabLayout tabLayout) {
        this.userLogin = userLogin;
        this.context = context;
        this.activity = activity;
        this.username = username;
        BASE_URL = context.getString(R.string.BASE_URL);
        this.viewSlots = viewSlots;
        this.tabHome = tabLayout;
        this.role = role;
    }

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //used to set the cookie on the request, if there is one
        SetSessionOnRequestInterceptor setSessionInterceptor =
                new SetSessionOnRequestInterceptor((HappyLearnApplication) activity.getApplication());

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
            List<Slot> availableSlot = response.body();

            if (availableSlot != null) {

                List<List<List<Slot>>> availableSlotsForDayandTime = trasformSlotToSlotForDayAndTime(availableSlot);

                if (role.equals("cliente")) {
                    //servlet prenotazioni attive
                    MiePrenotazioniHomeRequest prenotazioniController =
                            new MiePrenotazioniHomeRequest(context, activity, username, availableSlotsForDayandTime, viewSlots, tabHome);
                    prenotazioniController.start();
                } else {

                    viewBooking(availableSlotsForDayandTime.get(0), 0);

                    tabHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            viewBooking(availableSlotsForDayandTime.get(tab.getPosition()), tab.getPosition());
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                        }
                    });

                }
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
    public void onFailure(Call<List<Slot>> call, Throwable t) {
        Log.d("NOODLE", t.toString());
        Toast.makeText(context, "errore inatteso", Toast.LENGTH_LONG).show();
        t.printStackTrace();
    }


    private void viewBooking(List<List<Slot>> slotForTime, int day) {
        List<Slot> listone = new ArrayList<>();
        for (List<Slot> slotList:slotForTime){
            for (Slot s:slotList){
                listone.add(s);
            }
        }

        // Create adapter passing in the sample user data
        HomePageAdapter adapter = new HomePageAdapter(listone, context, (AppCompatActivity) activity);

        // Attach the adapter to the recyclerview to populate items
        this.viewSlots.setAdapter(adapter);
        // Set layout manager to position the items
        this.viewSlots.setLayoutManager(new LinearLayoutManager(context));

    }

    /**
     * get a List of Slot and return the same Slot divided in to List of Day -> time -> slot
     *
     * @param availableSlot
     * @return list of List-Day -> List-time -> List-slot
     */
    public static List<List<List<Slot>>> trasformSlotToSlotForDayAndTime(List<Slot> availableSlot) {
        List<List<List<Slot>>> availableSlotsForDayandTime = new ArrayList(5);
        for (int i = 0; i < 5; i++) {
            List<List<Slot>> ora = new ArrayList<>(4);
            for (int j = 0; j < 4; j++) {
                ora.add(j, new ArrayList<Slot>());
            }
            availableSlotsForDayandTime.add(i, ora);
        }

        int day = 0;
        int time = 0;
        if (availableSlot.size() != 0) {
            for (Slot slot : availableSlot) {
                if (day != slot.getDay()) {
                    day = slot.getDay();
                }
                if (time != slot.getTime()) {
                    time = slot.getTime();
                }
                availableSlotsForDayandTime.get(day).get(time).add(slot);
            }
        }


        return availableSlotsForDayandTime;
    }

    /**
     * Association the number DAY to it String DAY
     *
     * @param day
     * @return
     */
    public static String dayToString(int day) {
        switch (day) {
            case 0:
                return "Lundedì";
            case 1:
                return "Martedì";
            case 2:
                return "Mercoledì";
            case 3:
                return "Giovedì";
            case 4:
                return "Venerdì";
            default:
                return "Error Day";
        }
    }

    /**
     * Association the number TIME to it String Time
     *
     * @param time
     * @return
     */
    public static String timeToString(int time) {
        switch (time) {
            case 0:
                return "15:00 -> 16:00";
            case 1:
                return "16:00 -> 17:00";
            case 2:
                return "17:00 -> 18:00";
            case 3:
                return "18:00 -> 19:00";
            default:
                return "Error Time";
        }
    }
}

























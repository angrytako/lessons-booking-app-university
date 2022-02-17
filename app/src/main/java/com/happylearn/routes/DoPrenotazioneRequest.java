package com.happylearn.routes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happylearn.R;
import com.happylearn.adapters.PrenotazioniAdapter;
import com.happylearn.dao.BindablePrenotazione;
import com.happylearn.dao.Prenotazione;
import com.happylearn.dao.SimpleMessage;
import com.happylearn.routes.interceptors.SetSessionOnRequestInterceptor;
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

public class DoPrenotazioneRequest implements Callback<SimpleMessage> {
    private String BASE_URL;
    private Context context;
    private Activity activity;
    private Prenotazione prenotazione;
    public DoPrenotazioneRequest(Context context, Activity activity, Prenotazione prenotazione ) {
        this.context = context;
        this.activity = activity;
        BASE_URL = context.getString(R.string.BASE_URL);
        this.prenotazione=prenotazione;
        System.out.println(prenotazione);
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


        Call<SimpleMessage> call = gerritAPI.doPrenotazione(prenotazione);
        call.enqueue(this);


    }



    @Override
    public void onResponse(Call<SimpleMessage> call, Response<SimpleMessage> response) {
        if(response.isSuccessful()) {
            SimpleMessage message = response.body();
            if (message!=null){
                Toast.makeText(context, message.getMessage(), Toast.LENGTH_LONG).show();
                updateBookings(prenotazione);
            }
            else Toast.makeText(context, "message.getMessage()==null", Toast.LENGTH_LONG).show();


            ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .remove(((AppCompatActivity)activity).getSupportFragmentManager().getFragments().get(0))
                    .add(R.id.fragment_container, HomeFragment.class, null)
                    .commit();


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

    /**
     * Given a booking that has been added to the user
     * it updates the bookings or myBookings views, according
     * to this new addition
     * */
    private void updateBookings(Prenotazione booking){
        HappyLearnApplication application = (HappyLearnApplication)activity.getApplication();
        String role = application.getUserData().getRole().get();
        String username = application.getUserData().getUsername().get();
        //set the adapter as global
        PrenotazioniAdapter adapter;
        List<BindablePrenotazione> bookings;

        //picking right view to change
        //admin not choosing for himself
        if(role.equals("amministratore") && !username.equals(booking.getUtente()) ){
            adapter = application.getBookingsAdapter();
            bookings = application.getBookings();
        }
        else { //admin choosing for himself or normal user
            adapter = application.getMyBookingsAdapter();
            bookings = application.getMyBookings();
        }
        if(bookings == null || adapter == null)
            return;
        int bookingIndex = findBookingInBookings(booking,bookings);
        //if booking already exist, just update the status, else add the booking to the array
        if(bookingIndex != -1){
            bookings.get(bookingIndex).getIdDocente().set(booking.getIdDocente());
            bookings.get(bookingIndex).getNomeDocente().set(booking.getNomeDocente());
            bookings.get(bookingIndex).getCognomeDocente().set(booking.getCognomeDocente());
            bookings.get(bookingIndex).getStato().set("attiva");
        }
        else {
            bookings.add(0,new BindablePrenotazione(booking));
            adapter.notifyItemChanged(0);
        }

    }
    private int findBookingInBookings(Prenotazione booking, List<BindablePrenotazione> bookings){
        int i = 0;
        for(BindablePrenotazione supBooking : bookings){
            if(supBooking.getCorso().get().equals(booking.getCorso())
                && supBooking.getUtente().get().equals(booking.getUtente())
                && supBooking.getGiorno().get().equals(booking.getGiorno())
                && supBooking.getOrario().get().equals(booking.getOrario()))
                return i;
            else i++;
        }
        return -1;
    }

}



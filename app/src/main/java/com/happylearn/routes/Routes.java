package com.happylearn.routes;

import com.happylearn.dao.Prenotazione;
import com.happylearn.dao.SimpleMessage;
import com.happylearn.dao.SimpleUserData;
import com.happylearn.dao.UserLogin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Routes {
    @POST("Noodle_war/loginServlet")
    Call<SimpleUserData> login(@Body UserLogin userLogin);

    @GET("Noodle_war/MyInfoServlet")
    Call<SimpleUserData> myInfo();

    @PUT("Noodle_war/PrenotazioniServlet")
    Call<SimpleMessage> changePrenotazione(@Body Prenotazione updatedPrenotazione);

    @GET("Noodle_war/PrenotazioniServlet")
    Call<List<Prenotazione>> myPrenotazioni(@Query("username") String username);

    @GET("Noodle_war/PrenotazioniServlet")
    Call<List<Prenotazione>> prenotazioni();
}

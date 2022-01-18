package com.happylearn.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happylearn.R;
import com.happylearn.adapters.PrenotazioniAdapter;
import com.happylearn.dao.Prenotazione;
import com.happylearn.routes.MiePrenotazioniController;

import java.util.ArrayList;

public class MiePrenotazioniFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mie_prenotazioni, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Lookup the recyclerview in activity layout
        RecyclerView miePrenotazioni = (RecyclerView) view.findViewById(R.id.mie_prenotazioni_rv);

        // Initialize contacts
        ArrayList<Prenotazione> bookings = new ArrayList<>();
        bookings.add(new Prenotazione("corso1",21,"Pippo","Baudo","amministratore","ToneTuga","attiva",0, 0));
        bookings.add(new Prenotazione("corso2",11,"Pippo","Baudo","cliente","Anna","cancellata",1, 1));
        String username = ((HappyLearnApplication)this.getActivity().getApplication()).getUserData().getUsername().get();

        MiePrenotazioniController prenotazioniController = new MiePrenotazioniController(this.getContext(),this.getActivity(),username, miePrenotazioni);
        prenotazioniController.start();


    }
}

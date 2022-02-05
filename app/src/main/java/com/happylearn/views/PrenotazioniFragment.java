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
import com.happylearn.routes.MiePrenotazioniRequest;
import com.happylearn.routes.PrenotazioniRequest;

public class PrenotazioniFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prenotazioni, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Lookup the recyclerview in activity layout
        RecyclerView miePrenotazioni = (RecyclerView) view.findViewById(R.id.prenotazioni_rv);
        //choice to not make a heavy network request if i already have a booking
        //this only fails if I make a booking myself from another client or if
        //an admin does it
        //this whole think might have to be moved at the start of the main activity,
        //for performance gain
        if(((HappyLearnApplication)this.getActivity().getApplication()).getBookings() == null) {
            String username = ((HappyLearnApplication)this.getActivity().getApplication()).getUserData().getUsername().get();
            PrenotazioniRequest prenotazioniController = new PrenotazioniRequest(this.getContext(), this.getActivity(), miePrenotazioni);
            prenotazioniController.start();
        }
        //need this else since removing the fragment takes it away
        else {
            PrenotazioniAdapter adapter = new PrenotazioniAdapter(((HappyLearnApplication)this.getActivity().getApplication()).getBookings(), "bookings");
            // Attach the adapter to the recyclerview to populate items
            miePrenotazioni.setAdapter(adapter);
            // Set layout manager to position the items
            miePrenotazioni.setLayoutManager(new LinearLayoutManager(this.getContext()));

        }
    }

}
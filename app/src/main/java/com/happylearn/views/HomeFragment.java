package com.happylearn.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.happylearn.R;
import com.happylearn.routes.HomeRequest;

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Lookup the recyclerview in activity layout
        TextView ripetizioniHome = (TextView) view.findViewById(R.id.ripetizioniHome);
        //choice to not make a heavy network request if i already have a booking
        //this only fails if I make a booking myself from another client or if
        //an admin does it
        //this whole think might have to be moved at the start of the main activity,
        //for performance gain

        String username = ((HappyLearnApplication)this.getActivity().getApplication()).getUserData().getUsername().get();
        HomeRequest availableSlot = new HomeRequest(this.getContext(), this.getActivity(), username, ripetizioniHome);
        availableSlot.start();

        //need this else since removing the fragment takes it away

    }
}
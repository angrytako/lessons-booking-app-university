package com.happylearn.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.happylearn.R;
import com.happylearn.routes.HomeRequest;

import java.util.ArrayList;
import java.util.List;

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

//        TextView ripetizioniHome = (TextView) view.findViewById(R.id.ripetizioniHome);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabName);


        RecyclerView availableSlots =  (RecyclerView) view.findViewById(R.id.available_slots);



        //choice to not make a heavy network request if i already have a booking
        //this only fails if I make a booking myself from another client or if
        //an admin does it
        //this whole think might have to be moved at the start of the main activity,
        //for performance gain

        String username = ((HappyLearnApplication)this.getActivity().getApplication()).getUserData().getUsername().get();
        String role = ((HappyLearnApplication)this.getActivity().getApplication()).getUserData().getRole().get();
        

        HomeRequest availableSlot = new HomeRequest(this.getContext(), this.getActivity(), username,role, availableSlots, tabLayout);
        availableSlot.start();


        //need this else since removing the fragment takes it away

    }
}


























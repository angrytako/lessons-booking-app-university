package com.happylearn.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.happylearn.R;
import com.happylearn.adapters.DocentiListAdapter;
import com.happylearn.dao.Docente;
import com.happylearn.dao.Slot;
import com.happylearn.routes.HomeRequest;

import java.util.ArrayList;
import java.util.List;

public class EffettuaPrenotazioneFragment extends Fragment {
    DocentiListAdapter adapter;

    public EffettuaPrenotazioneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mie_prenotazioni_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lookup the recyclerview in activity layout
        TextView corso = (TextView) view.findViewById(R.id.corso);
        TextView giorno = (TextView) view.findViewById(R.id.giorno);
        TextView ora = (TextView) view.findViewById(R.id.ora);

        /*di prova*/
        List teacherList = new ArrayList<Docente>();
        teacherList.add(new Docente(5,"Roberta","Oliboni",false));
        teacherList.add(new Docente(2,"Gino","Bono",false));
        ((HappyLearnApplication)this.getActivity().getApplication()).setSlot(new Slot("Logica", teacherList,0,0));

        Slot slot = ((HappyLearnApplication)this.getActivity().getApplication()).getSlot();


        corso.append(slot.getCourse());
        giorno.append(HomeRequest.dayToString(slot.getDay()));
        ora.append(HomeRequest.timeToString(slot.getTime()));

        ArrayList<String> docentiString = new ArrayList<>();
        for(Docente d : slot.getTeacherList()){
            docentiString.add(d.getNome());
            docentiString.add(d.getCognome());
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.docentiList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new DocentiListAdapter(this.getContext(), docentiString);
       // adapter.setClickListener(this.getContext());
        recyclerView.setAdapter(adapter);

        //choice to not make a heavy network request if i already have a booking
        //this only fails if I make a booking myself from another client or if
        //an admin does it
        //this whole think might have to be moved at the start of the main activity,
        //for performance gain
/*
        String username = ((HappyLearnApplication)this.getActivity().getApplication()).getUserData().getUsername().get();
        String role = ((HappyLearnApplication)this.getActivity().getApplication()).getUserData().getRole().get();
*/


        //need this else since removing the fragment takes it away

    }
}

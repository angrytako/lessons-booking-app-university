package com.happylearn.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.happylearn.R;
import com.happylearn.adapters.DocentiListAdapter;
import com.happylearn.dao.Docente;
import com.happylearn.dao.Prenotazione;
import com.happylearn.dao.Slot;
import com.happylearn.routes.DoPrenotazioneRequest;
import com.happylearn.routes.HomeRequest;

import java.util.ArrayList;

public class EffettuaPrenotazioneFragment extends Fragment {
    private DocentiListAdapter adapter;
    private Context context = null;
    private Activity activity=null;
    public EffettuaPrenotazioneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.effettua_prenotazione_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lookup the recyclerview in activity layout
        TextView utente = (TextView) view.findViewById(R.id.utente);
        TextView corso = (TextView) view.findViewById(R.id.corso);
        TextView giorno = (TextView) view.findViewById(R.id.giorno);
        TextView ora = (TextView) view.findViewById(R.id.ora);
        Button prenota = (Button) view.findViewById(R.id.prenota);
        Button annulla = (Button) view.findViewById(R.id.annulla);


        Slot slot = ((HappyLearnApplication)this.getActivity().getApplication()).getSlot();
        context=this.getContext();
        activity=this.getActivity();
        String role = ((HappyLearnApplication)this.getActivity().getApplication()).getUserData().getRole().get();
        String username = ((HappyLearnApplication)this.getActivity().getApplication()).getUserData().getUsername().get();


        corso.append(slot.getCourse());
        giorno.append(HomeRequest.dayToString(slot.getDay()));
        ora.append(HomeRequest.timeToString(slot.getTime()));
        if (username==null || username.isEmpty()) utente.append("guest");
        else if (username.isEmpty()) utente.append("guest");
        else utente.append(username);

        ArrayList<String> docentiString = new ArrayList<>();
        for(Docente d : slot.getTeacherList()){
            docentiString.add("("+d.getId()+") "+ d.getNome()+ " " +d.getCognome());
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.docentiList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new DocentiListAdapter(this.getContext(), docentiString);
       // adapter.setClickListener(this.getContext());
        recyclerView.setAdapter(adapter);




        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .remove(((AppCompatActivity)activity).getSupportFragmentManager().getFragments().get(0))
                        .add(R.id.fragment_container, HomeFragment.class, null)
                        .commit();
            }
        });


        if ("guest".equals(role)){
            prenota.setText("Login");
            prenota.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .remove(((AppCompatActivity)activity).getSupportFragmentManager().getFragments().get(0))
                            .add(R.id.fragment_container, LoginFragment.class, null)
                            .commit();
                }
            });
        }else{
            prenota.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int choise = ((DocentiListAdapter)recyclerView.getAdapter()).getChois();
                    if( -1 == choise)
                        Toast.makeText( context, "Per effettuare la prenotazione devi selezionare un Docente", Toast.LENGTH_LONG).show();
                    else{
                        CheckBox checkBoxSelected = (CheckBox) recyclerView.getLayoutManager().findViewByPosition(choise).findViewById(R.id.docenti_List_Item);

                        Docente docenteSelected = parserDocente(checkBoxSelected.getText().toString());
                        DoPrenotazioneRequest availableSlot = new DoPrenotazioneRequest(context,activity ,
                                new Prenotazione(slot.getCourse(),docenteSelected.getId(),docenteSelected.getNome(),docenteSelected.getCognome(),
                                        role,username,"attiva",slot.getDay(),slot.getTime()));
                        availableSlot.start();
                    }
                }
            });
        }







    }


    private Docente parserDocente (String text){
        String[] singoleComponenti = text.split(" ");

        return new Docente(  Integer.parseInt(String.valueOf(singoleComponenti[0].charAt(1))),
                singoleComponenti[1].trim(),singoleComponenti[2].trim(),false);
    }


}

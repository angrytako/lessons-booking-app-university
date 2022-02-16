package com.happylearn.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.happylearn.R;
import com.happylearn.dao.Slot;
import com.happylearn.routes.HomeRequest;
import com.happylearn.views.EffettuaPrenotazioneFragment;
import com.happylearn.views.HappyLearnApplication;

import java.util.List;


public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {
    private List<Slot> slots;
    private AppCompatActivity activity;
    private LayoutInflater mInflater;
    private int blocco = 0;
    private Boolean flag = true;

    public HomePageAdapter(List<Slot> slots, Context context, AppCompatActivity activity) {
        this.slots = slots;
        this.mInflater = LayoutInflater.from(context);
        this.activity = activity;


    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_home_whith_time, parent, false);
        return new HomePageAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        Slot slot = slots.get(position);
        if (slot.getDay() == -1) {
            holder.ora.setText("Non ci sono altre lezioni disponibili");
            holder.btn.setText("0 Slot disponibili");
        } else {
            if (slot.getTime() > blocco || flag) {
                flag = false;
                blocco = slot.getTime();
                holder.ora.setText(HomeRequest.timeToString(slot.getTime()));
            } else {
                holder.ora.setHeight(1);
            }

            holder.btn.setText(slot.getCourse());
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(e.getContext(), "hai cliccato " + position, Toast.LENGTH_SHORT).show();
                    ((HappyLearnApplication) activity.getApplication()).setSlot(slot);
                    activity.getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .remove(activity.getSupportFragmentManager().getFragments().get(0))
                            .add(R.id.fragment_container, EffettuaPrenotazioneFragment.class, null)
                            .commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btn;
        TextView ora;

        ViewHolder(View itemView) {
            super(itemView);
            ora = itemView.findViewById(R.id.textOra);
            btn = itemView.findViewById(R.id.corsoItem);
        }


    }


}











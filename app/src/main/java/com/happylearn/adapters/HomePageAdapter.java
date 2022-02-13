package com.happylearn.adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.happylearn.dao.BindablePrenotazione;
import com.happylearn.dao.BindableSlots;
import com.happylearn.dao.Slot;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.happylearn.R;
import com.happylearn.databinding.ItemViewHomepageBinding;

import java.util.List;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {
    private List<BindableSlots> slots;
    private String type;

    public HomePageAdapter(List<BindableSlots> slots, String type) {
        this.slots = slots;
        this.type = type;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        ItemViewHomepageBinding slotsItemView = null;

//        TextView nameCourse;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview

        public ViewHolder(ItemViewHomepageBinding itemView) {
            super(itemView.getRoot());
            this.slotsItemView = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout with binding
        //since it's used both by MiePrenotazioni and Prenotazioni, the type of item
        //depends on which one is using it
        ItemViewHomepageBinding mBinding = null;

        //if(type.equals........)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.item_view_homepage,parent,false);

        ViewHolder viewHolder = new ViewHolder(mBinding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        BindableSlots slot = slots.get(position);

        // Set item views based on your views and data model
        if(holder.slotsItemView != null)
            holder.slotsItemView.setSlot(slot);
        else
            holder.slotsItemView.setSlot(slot);

        //setting buttons event listeners
        //each will have a toast

        if(holder.slotsItemView != null)
            holder.slotsItemView.courseBtn.setOnClickListener((e)->{
                Toast.makeText(e.getContext(), "hai cliccato ", Toast.LENGTH_SHORT).show();
            });

    }

    @Override
    public int getItemCount() {
        return slots.size();
    }
}

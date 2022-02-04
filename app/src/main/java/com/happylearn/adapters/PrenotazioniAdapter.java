package com.happylearn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.happylearn.R;
import com.happylearn.dao.BindablePrenotazione;
import com.happylearn.dao.Prenotazione;
import com.happylearn.databinding.PrenotazioniItemBinding;

import java.util.List;

public class PrenotazioniAdapter extends
        RecyclerView.Adapter<PrenotazioniAdapter.ViewHolder>{
    private List<BindablePrenotazione> bookings;

    public PrenotazioniAdapter(List<BindablePrenotazione> bookings) {
        this.bookings = bookings;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        PrenotazioniItemBinding itemView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(PrenotazioniItemBinding itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout with binding
        PrenotazioniItemBinding pBinding =  DataBindingUtil.inflate(inflater,R.layout.prenotazioni_item,parent,false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(pBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        BindablePrenotazione booking = bookings.get(position);

        // Set item views based on your views and data model
        holder.itemView.setBooking(booking);
        //TODO deal with buttons
//        Button button = holder.messageButton;
//        button.setText(contact.isOnline() ? "Message" : "Offline");
//        button.setEnabled(contact.isOnline());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }



}

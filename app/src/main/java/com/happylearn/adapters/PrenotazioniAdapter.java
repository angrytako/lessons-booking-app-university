package com.happylearn.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.happylearn.R;
import com.happylearn.dao.BindablePrenotazione;
import com.happylearn.dao.Prenotazione;
import com.happylearn.databinding.MiePrenotazioniItemBinding;
import com.happylearn.databinding.PrenotazioniItemBinding;
import com.happylearn.routes.ChangePrenotazioneRequest;

import java.util.List;

public class PrenotazioniAdapter extends
        RecyclerView.Adapter<PrenotazioniAdapter.ViewHolder>{
    private List<BindablePrenotazione> bookings;
    private String type;

    public PrenotazioniAdapter(List<BindablePrenotazione> bookings, String type) {
        this.type = type;
        this.bookings = bookings;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        PrenotazioniItemBinding bookingsItemView = null;
        MiePrenotazioniItemBinding MyBookingsItemView = null;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(PrenotazioniItemBinding itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView.getRoot());
            this.bookingsItemView = itemView;
        }

        public ViewHolder(MiePrenotazioniItemBinding itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView.getRoot());
            this.MyBookingsItemView = itemView;
        }
    }

    //allert dialog for confirmation of choice
    public static class ConfirmationDialogFragment extends DialogFragment {
        String action;
        Context ctx;
        List<BindablePrenotazione> bookings;
        int position;
        PrenotazioniAdapter adapter;

        public ConfirmationDialogFragment(String action, Context ctx, List<BindablePrenotazione> bookings, int position, PrenotazioniAdapter adapter) {
            this.action = action;
            this.ctx = ctx;
            this.bookings = bookings;
            this.position = position;
            this.adapter = adapter;
        }

        // are you sure? yes/no
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("La sua scelta potrebbe non essere reversibile. E' sicuro di voler procedere?")

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // In case of no, don't do anything
                        }
                    })
                    .setPositiveButton("S??", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Prenotazione newBooking = bookings.get(position).getSerializable();
                            newBooking.setStato(action);
                            ChangePrenotazioneRequest cp = new ChangePrenotazioneRequest(ctx, bookings,position,newBooking,adapter);
                            cp.start();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
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
        PrenotazioniItemBinding pBinding = null;
        MiePrenotazioniItemBinding mpBinding = null;
        if(type.equals("bookings"))
            pBinding =  DataBindingUtil.inflate(inflater,R.layout.prenotazioni_item,parent,false);
        else
            mpBinding =  DataBindingUtil.inflate(inflater,R.layout.mie_prenotazioni_item,parent,false);

        // Return a new holder instance
        ViewHolder viewHolder;
        if(pBinding != null)
            viewHolder = new ViewHolder(pBinding);
        else
            viewHolder = new ViewHolder(mpBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        BindablePrenotazione booking = bookings.get(position);

        // Set item views based on your views and data model
        if(holder.MyBookingsItemView != null)
            holder.MyBookingsItemView.setBooking(booking);
        else holder.bookingsItemView.setBooking(booking);

        //setting buttons event listeners
        //each will have a confirmation dialog
        if(holder.MyBookingsItemView != null)
            holder.MyBookingsItemView.cancelBtn.setOnClickListener((e)->{
            new ConfirmationDialogFragment(
                    "cancellata", e.getContext(),bookings, position, this)
                    .show(((AppCompatActivity)e.getContext()).getSupportFragmentManager(),"confirm");
        });
        else
            holder.bookingsItemView.cancelBtn.setOnClickListener((e)->{
                new ConfirmationDialogFragment(
                        "cancellata", e.getContext(),bookings, position, this)
                        .show(((AppCompatActivity)e.getContext()).getSupportFragmentManager(),"confirm");
            });
        if(holder.MyBookingsItemView != null)
            holder.MyBookingsItemView.completeBtn.setOnClickListener((e)->{
                new ConfirmationDialogFragment(
                        "effettuata", e.getContext(),bookings, position, this)
                        .show(((AppCompatActivity)e.getContext()).getSupportFragmentManager(),"confirm");

            });
        else
            holder.bookingsItemView.completeBtn.setOnClickListener((e)->{
                new ConfirmationDialogFragment(
                        "effettuata", e.getContext(),bookings, position, this)
                        .show(((AppCompatActivity)e.getContext()).getSupportFragmentManager(),"confirm");

            });

    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }



}

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
import com.happylearn.databinding.PrenotazioniItemBinding;
import com.happylearn.routes.ChangePrenotazioneRequest;

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
                    .setPositiveButton("Sì", new DialogInterface.OnClickListener() {
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

        //setting buttons event listeners
        //each will have a confirmation dialog
        holder.itemView.cancelBtn.setOnClickListener((e)->{
            new ConfirmationDialogFragment(
                    "cancellata", e.getContext(),bookings, position, this)
                    .show(((AppCompatActivity)e.getContext()).getSupportFragmentManager(),"confirm");
        });

        holder.itemView.completeBtn.setOnClickListener((e)->{
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

package com.happylearn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.happylearn.R;
import com.happylearn.dao.Prenotazione;

import java.util.List;

public class PrenotazioniAdapter extends
        RecyclerView.Adapter<PrenotazioniAdapter.ViewHolder>{
    private List<Prenotazione> bookings;

    public PrenotazioniAdapter(List<Prenotazione> bookings) {
        this.bookings = bookings;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView teacherTV, courseTV, dayTV, hourTV;
        public Button completeBtn, cancelBtn;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            teacherTV = (TextView) itemView.findViewById(R.id.teacher_tv);
            courseTV = (TextView) itemView.findViewById(R.id.course_tv);
            dayTV = (TextView) itemView.findViewById(R.id.day_tv);
            hourTV = (TextView) itemView.findViewById(R.id.hour_tv);
            completeBtn = (Button) itemView.findViewById(R.id.complete_btn);
            cancelBtn = (Button) itemView.findViewById(R.id.cancel_btn);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View prenotazione = inflater.inflate(R.layout.prenotazioni_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(prenotazione);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        Prenotazione booking = bookings.get(position);

        // Set item views based on your views and data model
        TextView teacherTV = holder.teacherTV;
        teacherTV.setText(booking.getNomeDocente() + " " + booking.getCognomeDocente());
        TextView courseTV = holder.courseTV;
        courseTV.setText(booking.getCorso());
        TextView dayTV = holder.dayTV;
        dayTV.setText(String.valueOf(booking.getGiorno()));
        TextView hourTV = holder.hourTV;
        dayTV.setText(String.valueOf(booking.getOrario()));
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

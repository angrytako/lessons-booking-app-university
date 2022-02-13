package com.happylearn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.happylearn.R;

import java.util.ArrayList;
import java.util.List;

public class DocentiListAdapter extends RecyclerView.Adapter<DocentiListAdapter.ViewHolder> {

    private Integer chois;
    private List<String> mData;
    private LayoutInflater mInflater;
    private List<CheckBox> groupButton;

    // data is passed into the constructor
    public DocentiListAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.groupButton= new ArrayList<>(data.size());
        chois = new Integer(-1);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.docenti_list_item, parent, false);
        return new ViewHolder(view);
    }

    public int getChois() {
        return chois.intValue();
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Integer index = new Integer(position);
        String animal = mData.get(position);
        holder.radioButton.setText(animal);
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chois.intValue()!=-1){
                    groupButton.get(chois.intValue()).toggle();
                    chois= new Integer(index.intValue());
                    //holder.radioButton.setText(chois.intValue());
                }
                else chois=new Integer(index.intValue());
            }
        });
        groupButton.add(position,holder.radioButton);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox radioButton;

        ViewHolder(View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.docenti_List_Item);
        }


    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }





}
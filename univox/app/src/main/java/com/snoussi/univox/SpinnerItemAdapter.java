package com.snoussi.univox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SpinnerItemAdapter extends ArrayAdapter {

    public SpinnerItemAdapter(Context context, ArrayList<EmergencyItem> arraylist){
        super(context,0,arraylist);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView(int position,View convertView, ViewGroup parent){
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_row,parent,false);
        }
        ImageView itemIcon = convertView.findViewById(R.id.spinner_image);
        TextView itemName = convertView.findViewById(R.id.spinner_name);

        EmergencyItem emergencyItem = (EmergencyItem) getItem(position);

        itemIcon.setImageResource(emergencyItem.getItemIcon());
        itemName.setText(emergencyItem.getItemName());

        return convertView;

    }
}

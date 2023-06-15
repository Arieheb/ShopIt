package com.ariehb_miriams.shopit;

import android.app.Activity;
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
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> {
    List<String> list;
    Context context;

    public CustomListAdapter(Context context, ArrayList<String> items) {
        super(context, R.layout.activity_list_item, items);
        this.context = context;
        list = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_list_item, null);
        }

        TextView number = convertView.findViewById(R.id.number);
        number.setText(position + 1 + ".");


        TextView textListName = convertView.findViewById(R.id.textViewListName);
        textListName.setText(list.get(position));


        ImageView checkIcon = convertView.findViewById(R.id.imageViewCheck);
        ImageView removeIcn = convertView.findViewById(R.id.remove);

        checkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        removeIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




        return convertView;
    }



}

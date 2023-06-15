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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> listNames;

    public CustomListAdapter(Context context, ArrayList<String> listNames) {
        super(context, R.layout.list_item, listNames);
        this.context = context;
        this.listNames = listNames;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView textListName = convertView.findViewById(R.id.textViewListName);


        String listName = listNames.get(position);
        textListName.setText(listName);

        ImageView checkIcon = convertView.findViewById(R.id.imageViewCheck);
        ImageView removeIcn = convertView.findViewById(R.id.remove);

        checkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return convertView;
    }



}

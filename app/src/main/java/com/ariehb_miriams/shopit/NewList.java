package com.ariehb_miriams.shopit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class NewList extends AppCompatActivity {

    static ListView listView;
    static ArrayList<String> items;
    static ListViewAdapter adapter;

    EditText input;
    ImageView enter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        listView = findViewById(R.id.listView);
        input = findViewById(R.id.itemName);
        enter = findViewById(R.id.add);


        items = new ArrayList<>();
        items.add ("Apple");
        items.add ("Milk");
        items.add ("Flour");
        items.add ("Sugar");
        items.add ("Coffee");
        items.add ("Pepper");

        adapter = new ListViewAdapter(getApplicationContext(), items);
        listView.setAdapter(adapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                makeToast("Removed: " + items.get(i));
                removeItem(i);
//                Toast.makeText(NewList.this, "Removed: " + items.get(i), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = input.getText().toString();
                if (text == null || text.length() == 0) {
                    makeToast("Enter an item");
                }
                else {
                    addItem(text);
                    input.setText("");
                    makeToast("Added: " + text);
                }
            }
        });
    }

    private static void addItem(String text) {
        items.add(text);
        listView.setAdapter(adapter);
    }

    static void removeItem(int remove){
        items.remove(remove);
        listView.setAdapter(adapter);
    }
    Toast t;
    private void makeToast (String s) {
        if (t!= null) t.cancel();
        t = Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT);
        t.show();
    }

}
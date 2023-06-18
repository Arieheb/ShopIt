package com.ariehb_miriams.shopit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NewList extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Button buttonLoadContacts;
    private List<String> contactList;
    static ListView listView;
    static ArrayList<String> items;
    static ListViewAdapter adapter;
    private static SharedPreferences sp;

    private static final int RESULT_PICK_CONTACT = 1;
    private Button select;

    EditText input;
    EditText NameOfList;
    ImageView enter;
    String newListName;
    String text;
    Button saveBtn;

    static String currentList;

    Set<String> itemSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        listView = findViewById(R.id.listView);
        input = findViewById(R.id.itemName);
        enter = findViewById(R.id.add);
        saveBtn = findViewById(R.id.saveListBtn);
        NameOfList = findViewById(R.id.listName);
        items = new ArrayList<>();
        select = findViewById(R.id.collabAdd);
        adapter = new ListViewAdapter(getApplicationContext(), items);
        listView.setAdapter(adapter);


        //// Contact Access ////


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(in, RESULT_PICK_CONTACT);


            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newListName = NameOfList.getText().toString();
                editor.putString("currentList", newListName);
                currentList = sp.getString("currentList",newListName);
                text = input.getText().toString();
                if (text == null || text.length() == 0) {
                    makeToast("Enter an item");
                }
                else {
                    itemSet = sp.getStringSet(currentList, null);
                    if (itemSet == null) {
                        itemSet = new HashSet<>();
                    }
                    else {
                        itemSet = new HashSet<>(itemSet);
                    }
                    itemSet.add(text);
                    editor.putStringSet(currentList,itemSet);
                    editor.apply();
                    addItem(text);
                    input.setText("");
                    makeToast("Added: " + text);
                }
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                List<String> collaborators = new ArrayList<>();
                collaborators.add(sp.getString("phone_number", ""));


                Map<String,Object> newList = new HashMap<>();
                newList.put("collab", collaborators);
                newList.put("list name", newListName);
                newList.put("items", items);

                db.collection("lists")
                        .add(newList)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(), "List saved to Firebase", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(NewList.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to save list: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Clear the SharedPreferences
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(currentList);
        editor.apply();
        Intent intent = new Intent(NewList.this, MainActivity.class);
        startActivity(intent);
        finish();
        // Clear the stack and navigate back to the previous activity

    }
    private static void addItem(String text) {
        items.add(text);
        listView.setAdapter(adapter);
    }

    static void removeItem(String item){
        items.remove(item);
        listView.setAdapter(adapter); // Notify the adapter of the data change
        saveItemsToSharedPreferences(); // Save the updated items to SharedPreferences
    }
    Toast t;
    private void makeToast (String s) {
        if (t!= null) t.cancel();
        t = Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT);
        t.show();
    }

    private static void saveItemsToSharedPreferences() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(currentList, new HashSet<>(items));
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {  /////
                case RESULT_PICK_CONTACT:
                    contactPicked(data);

            }
        }
        else {
            Toast.makeText(this, "failed to pick contact", Toast.LENGTH_SHORT).show();
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try{
            String phoneNo = null;
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            cursor = getContentResolver().query(uri,null,null,null,null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNo = cursor.getString(phoneIndex);
            Log.d("mylog", "contact number " + phoneNo);
//            phone.setText(phoneNo);
       }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
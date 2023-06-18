package com.ariehb_miriams.shopit;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SignInActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userCollection = db.collection("users");


    boolean exists = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();


        EditText phoneInput = findViewById(R.id.phoneInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button signInBtn = findViewById(R.id.signInButton);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        String phoneInp = phoneInput.getText().toString();
                        String passInput = passwordInput.getText().toString();



                        CollectionReference listsRef = db.collection("lists");

                        listsRef.whereArrayContains("collab", phoneInp)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    if (queryDocumentSnapshots.isEmpty()) {
                                        // Phone number does not exist in any collab array
                                        Log.d("PhoneNumberCheck", "Phone number does not exist in any collab array.");
                                    } else {
                                        // Phone number exists in at least one collab array
                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            // Get the list data
                                            String listName = documentSnapshot.getString("list name");
                                            String listId = documentSnapshot.getId();
                                            List<String> items = (List<String>) documentSnapshot.get("items");

                                            // Save the list to SharedPreferences
                                            saveListToSharedPreferences(listId,listName, items);
                                        }
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    // Error checking phone number in collab arrays
                                    Log.e("PhoneNumberCheck", "Error checking phone number in collab arrays: " + e.getMessage());
                                });



                        if (phoneInp.equals("")) {
                            Toast.makeText(SignInActivity.this, "Missing info", Toast.LENGTH_SHORT).show();
                        }
                        else if (passInput.equals("")) {
                            Toast.makeText(SignInActivity.this, "Missing info", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            for (QueryDocumentSnapshot document : value) {
                                String passVal = document.getString("password");
                                String phoneVal = document.getString("phone_Number");

                                if (phoneInp.equals(phoneVal)) {
                                    if (passInput.equals(passVal)) {
                                        exists =true;
                                        Toast.makeText(SignInActivity.this, "Success Login!", Toast.LENGTH_SHORT).show();
                                        String userId = document.getId() ;
                                        getAndSaveDataSP(userId);
                                    }
                                }

                            }
                            if (!exists) {
                                Toast.makeText(SignInActivity.this, "Phone/Password Incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


            }
        });



        //// link to sign up area ////

        TextView linkText = findViewById(R.id.textLink);

        linkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the new activity
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
    private void getAndSaveDataSP (String userId) {
        userCollection.document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String firstName = documentSnapshot.getString("first");
                String lastName = documentSnapshot.getString("last");
                String phoneNumber = documentSnapshot.getString("phone_Number");
                String password = documentSnapshot.getString("password");



                SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("first", firstName);
                editor.putString("last", lastName);
                editor.putString("phone_number", phoneNumber);
                editor.putString("password", password);
                editor.putString("userId", userId);
                editor.putBoolean("signedOut", false);
                editor.putBoolean("contactPer", false);
                editor.putBoolean("cameraPer", false);
                editor.commit();

                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void saveListToSharedPreferences(String listId, String listName, List<String> items) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("list_"+ listId, listName);

        // Save the items as a Set<String>
        Set<String> itemsSet = new HashSet<>(items);
        editor.putStringSet(listName , itemsSet);

        editor.apply();
    }

}
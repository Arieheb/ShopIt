package com.ariehb_miriams.shopit;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                            Log.e(TAG, "Error getting documents: ", error);
                            return;
                        }
                        String phoneInp = phoneInput.getText().toString();
                        String passInput = passwordInput.getText().toString();

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
                                        String userFirstName = document.getString("first");
                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                        intent.putExtra("userId", userId);
                                        intent.putExtra("userFirstName",userFirstName);
                                        startActivity(intent);
                                        finish();
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
}
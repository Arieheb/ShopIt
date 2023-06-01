package com.ariehb_miriams.shopit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();


        Button signUpBtn = findViewById(R.id.signupBtn);
        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText phone = findViewById(R.id.phoneInput);
        EditText newPass = findViewById(R.id.newPassword);
        EditText conPass = findViewById(R.id.conPassword);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((conPass.getText().toString()).equals(newPass.getText().toString()) && (!newPass.getText().toString().equals(" ")) ) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("first name", firstName.getText().toString());
                    user.put("last name", lastName.getText().toString());
                    user.put("phone number", phone.getText().toString());
                    user.put("password", conPass.getText().toString());

                    // Add a new document with a generated ID
                    db.collection("users")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                    Toast.makeText(SignUpActivity.this, "Success!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(SignUpActivity.this, HomeScreenActivity.class);
                    startActivity(intent);
                }

                else {
                    Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });






        //// Firebase section for use of information ////

        // Create a new user with a first and last name




    }


}
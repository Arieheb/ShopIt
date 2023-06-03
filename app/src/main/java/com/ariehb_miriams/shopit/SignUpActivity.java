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
        EditText firstName = findViewById(R.id.firstNameInput);
        EditText lastName = findViewById(R.id.lastNameInput);
        EditText phone = findViewById(R.id.phoneInput);
        EditText newPass = findViewById(R.id.newPasswordInput);
        EditText conPass = findViewById(R.id.conPasswordInput);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!conPass.getText().toString().equals(newPass.getText().toString())) { //if passwords are not the same
                    Toast.makeText(SignUpActivity.this, "Passwords Not Matching", Toast.LENGTH_LONG).show();
                }
                else if ((firstName.length() == 0) || (lastName.length() == 0) || (phone.length() == 0) || (newPass.length() == 0) || (conPass.length() == 0)) {
                    Toast.makeText(SignUpActivity.this, "Missing Info", Toast.LENGTH_LONG).show();
                }
                else {
                    Map<String, Object> user = new HashMap<>();
                    user.put("first", firstName.getText().toString());
                    user.put("last", lastName.getText().toString());
                    user.put("phone_Number", phone.getText().toString());
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

                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });


    }


}
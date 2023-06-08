package com.ariehb_miriams.shopit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    CollectionReference userCollection = db.collection("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String firstName = sp.getString("first", "");
        String lastName = sp.getString("last", "");
        String phoneNumber = sp.getString("phone_number", "");
        String password = sp.getString("password", "");

        EditText firstNameHint = findViewById(R.id.firstNameEdit);
        firstNameHint.setHint(firstName);
        EditText lastNameHint = findViewById(R.id.lastNameEdit);
        lastNameHint.setHint(lastName);
        EditText phoneNumberHint = findViewById(R.id.phoneEdit);
        phoneNumberHint.setHint(phoneNumber);
        EditText passwordHint = findViewById(R.id.passwordEdit);
        passwordHint.setHint(password);








        //// Sign out section ////
        Button signOutButton = findViewById(R.id.signOutBtn);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                exitAlertDialog();
            }
        });
    }

    private void exitAlertDialog()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sign Out");
        dialog.setMessage("Are you sure you want to sign out?");
        dialog.setCancelable(false);

        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                clearSharedPreferences();
                navigateToSignIn();
                ProfileActivity.super.finish();
                finish();   // destroy this activity

            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();   // close this dialog
            }
        });
        dialog.show();
    }

    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        finish();
    }
    private void navigateToSignIn() {
        Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
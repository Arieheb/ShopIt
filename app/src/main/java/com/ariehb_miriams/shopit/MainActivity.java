package com.ariehb_miriams.shopit;


import static android.content.ContentValues.TAG;
import static java.lang.System.exit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity implements MultipuleChoiceDialogFragment.onMultiChoiceListener {
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    CollectionReference userCollection = db.collection("users");
    private FirebaseFirestore db;
    private CollectionReference userCollection;
    String userID, userName;
    TextView mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        userCollection = db.collection("users");
        userID = getIntent().getStringExtra("userId");
        getAndSaveDataSP(userID);
        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String userFirstName = sp.getString("first","");
        TextView title = findViewById(R.id.mainHead);
        Log.d(TAG, "onCreate: " + userFirstName);
        title.setText("Hello " + userFirstName);


        mylist = findViewById(R.id.mylist);

        Button btnmylist = findViewById(R.id.btnmylist);
        Button btnmylist1 = findViewById(R.id.btnmylist1);
        Button btnmylist2 = findViewById(R.id.btnmylist2);
        btnmylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment multiChoiceDialog = new MultipuleChoiceDialogFragment();
                multiChoiceDialog.setCancelable(false);
                multiChoiceDialog.show(getSupportFragmentManager(), "multiChoice Dialog");

            }
        });

        btnmylist1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment multiChoiceDialog = new MultipuleChoiceDialogFragment();
                multiChoiceDialog.setCancelable(false);
                multiChoiceDialog.show(getSupportFragmentManager(), "multiChoice Dialog");
            }
        });

        btnmylist2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment multiChoiceDialog = new MultipuleChoiceDialogFragment();
                multiChoiceDialog.setCancelable(false);
                multiChoiceDialog.show(getSupportFragmentManager(), "multiChoice Dialog");
            }
        });
    }




    //// 3 dot menu section ////
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        MenuItem aboutMenu = menu.add("About");
        MenuItem settingsMenu = menu.add("Settings");
        MenuItem exitMenu = menu.add("Exit");

        settingsMenu.setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            return false;
        });

        aboutMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                aboutAlertDialog();
                return false;
            }
        });

        exitMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                exitAlertDialog();
                return false;
            }
        });

        return true;
    }


    private void aboutAlertDialog()
    {
        String strDeviceOS = "Android OS " + Build.VERSION.RELEASE + " API " + Build.VERSION.SDK_INT;

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("About App");
        dialog.setMessage("\nShopit is a social shopping list app" + "\n\n" + strDeviceOS + "\n\n" + "By Arieh Berlin & Miriam Sirota 2023.");

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();   // close this dialog
            }
        });
        dialog.show();
    }
    private void exitAlertDialog()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setIcon(R.drawable.icon_exit);
        dialog.setTitle("Exit App");
        dialog.setMessage("Are you sure ?");
        dialog.setCancelable(false);

        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
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


    //// defining exit if back button pressed on main screen ////
    @Override
    public void onBackPressed() {
        // Display an exit alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit the app?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Exit the app
                finish();

            }
        });
        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onPositiveButtonClicked(String[] list, ArrayList<String> selectedItemList) {
//        StringBuilder stringBuilder = new StringBuilder();
////        stringBuilder.append("Selected Choices = ");
//        for (String str:selectedItemList){
//            stringBuilder.append(str + " ");
//        }
//        mylist.setText(stringBuilder);
    }

    @Override
    public void onNegativeButtonClicked() {
//        mylist.setText("Dialog canceled");

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
               editor.commit();
           }
        });
    }
}
package com.ariehb_miriams.shopit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userCollection = db.collection("users");
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    Button mCaptureBtn;
    ImageView mImageView;

    Uri image_Uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String firstNameSp = sp.getString("first", "");
        String lastNameSp = sp.getString("last", "");
        String phoneNumberSp = sp.getString("phone_number", "");
        String passwordSp = sp.getString("password", "");

        EditText firstNameField = findViewById(R.id.firstNameEdit);
        EditText lastNameField = findViewById(R.id.lastNameEdit);
        EditText phoneNumberField = findViewById(R.id.phoneEdit);
        EditText passwordField = findViewById(R.id.passwordEdit);
        firstNameField.setHint(firstNameSp);
        lastNameField.setHint(lastNameSp);
        phoneNumberField.setHint(phoneNumberSp);
        passwordField.setHint(passwordSp);



        Button savBtn = findViewById(R.id.SaveBtn);

        savBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstNameChg = firstNameField.getText().toString();
                String lastNameChg = lastNameField.getText().toString();
                String phonenumChg = phoneNumberField.getText().toString();
                String passwordChg = passwordField.getText().toString();


                if (!firstNameChg.isEmpty()) {
                    firstNameField.setText("");
                    updateData(firstNameSp,firstNameChg,"first");
                    editor.putString("first", firstNameChg);
                    editor.apply();
                    firstNameField.setHint(firstNameChg);
                }
                if (!lastNameChg.isEmpty()) {
                    lastNameField.setText("");
                    updateData(lastNameSp,lastNameChg,"last");
                    editor.putString("last", lastNameChg);
                    editor.apply();
                    lastNameField.setHint(lastNameChg);
                }
                if (!phonenumChg.isEmpty()) {
                    phoneNumberField.setText("");
                    updateData(phoneNumberSp,phonenumChg,"phone_Number");
                    editor.putString("phone_number", phonenumChg);
                    editor.apply();
                    phoneNumberField.setHint(phonenumChg);
                }
                if (!passwordChg.isEmpty()) {
                    passwordField.setText("");
                    updateData(passwordSp, passwordChg,"password");
                    editor.putString("password", passwordChg);
                    editor.apply();
                    passwordField.setHint(passwordChg);
                }
            }
        });



        //// Sign out section ////
        Button signOutButton = findViewById(R.id.signOutBtn);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                exitAlertDialog();
            }
        });

        mImageView = findViewById(R.id.imageView);
        mCaptureBtn = findViewById(R.id.button);

        //button click
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if system os is >= marshmallow, request runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                            || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ){
                        //permission not enabled, request it
                        String [] permission = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        // show popup to request permissions
                        requestPermissions(permission, PERMISSION_CODE);
                    }

                    else {
                        //permission already granted
                        openCamera();

                    }
                }
                else {
                    //system os is < marshmallow
                    openCamera();
                }
            }
        });
    }


    //// Camera Section ////
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "new picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "from the camera");
        image_Uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_Uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // this method is called when user presses allow or deny from permission request popup
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission from popup grandet
                    openCamera();
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //called when image was captured from camera
        if(resultCode == RESULT_OK){
            //set the image captured to our image view
            mImageView.setImageURI(image_Uri);
        }
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
//                ProfileActivity.super.finish();
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
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void updateData(String existingName, String newName, String updateField) {
        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put(updateField, newName);
        db.collection("users")
                .whereEqualTo(updateField, existingName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();
                            db.collection("users")
                                    .document(documentID)
                                    .update (userUpdate)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ProfileActivity.this, "Info Saved", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ProfileActivity.this, "error", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                            }
                        else {
                            Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
}
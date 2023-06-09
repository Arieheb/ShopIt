package com.ariehb_miriams.shopit;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomListAdapter extends ArrayAdapter<String> {
    List<String> list;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CustomListAdapter(Context context, ArrayList<String> items) {
        super(context, R.layout.activity_list_item, items);
        this.context = context;
        list = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_list_item, null);
        }

        TextView textListName = convertView.findViewById(R.id.textViewListName);
        textListName.setText(list.get(position));

        ImageView removeIcn = convertView.findViewById(R.id.remove);

        removeIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String removedItem = list.get(position);
                list.remove(position);

                // Update SharedPreferences
                updateSharedPreferences(removedItem);

                // Update Firebase
                updateFirebase(removedItem);

                // Notify the adapter that the data has changed
                notifyDataSetChanged();
            }
        });




        return convertView;
    }

    private void updateSharedPreferences(String removedItem) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(removedItem);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        // Remove the matching item from the set
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key != null && key.startsWith("list_") && value != null && value.equals(removedItem)) {
                // Remove the entry from SharedPreferences
                editor.remove(key);
            }
        }
        editor.apply();
    }


    private void updateFirebase(String removedItem) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
//
        String phoneNumber = sharedPreferences.getString("phone_number", ""); // Replace with your actual phone number
        CollectionReference userLists = db.collection("lists");
        Log.d(TAG, "updateFirebase:  userLists is: " + userLists);

        userLists.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String listId = document.getId();
                        Log.d(TAG, "onComplete: list id is: "+listId);
                        List<String> phoneNumberArray = (List<String>) document.get("collab");


                        if (document.getString("list name").equals(removedItem)) {
                            if (phoneNumberArray.size() == 1 && phoneNumberArray.contains(phoneNumber)) {
                                // If your phone number is the only one in 'collab', delete the entire list
                                userLists.document(listId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // List deletion successful, handle any additional actions
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle the failure case
                                                Log.e(TAG, "Failed to delete list", e);
                                            }
                                        });
                            } else if (phoneNumberArray.contains(phoneNumber)) {
                                // If your phone number exists in 'collab', remove it from the array
                                phoneNumberArray.remove(phoneNumber);
                                userLists.document(listId)
                                        .update("collab", phoneNumberArray)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Update successful, handle any additional actions
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle the failure case
                                                Log.e(TAG, "Failed to update list", e);
                                            }
                                        });
                            } 
                            break; // Exit the loop once the desired list is found
                        }
                    }
                } else {
                    // Task failed to retrieve documents
                    Exception exception = task.getException();
                    Log.e(TAG, "Failed to retrieve documents", exception);
                }
            }
        });
    }

}

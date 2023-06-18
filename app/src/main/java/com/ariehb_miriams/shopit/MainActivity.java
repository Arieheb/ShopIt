package com.ariehb_miriams.shopit;


import static android.content.ContentValues.TAG;
import static java.lang.System.exit;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.content.BroadcastReceiver;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity implements MultipuleChoiceDialogFragment.onMultiChoiceListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userCollection = db.collection("users");
    CollectionReference listCollection = db.collection("lists");

    SharedPreferences sp;
    BroadcastReceiver broadcastReceiver = null;
    String userID;
    TextView mylist;
    static ArrayList<String> items;

    ListView listView;
    CustomListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("mylog","111");
        sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        listView = findViewById(R.id.listView);
        items = new ArrayList<>();
        loadKeyNames(); // Call the method to load key names into the ListView

//        Log.d(TAG, "items is array of all list names " + items);
        adapter = new CustomListAdapter(getApplicationContext(),items);
        listView.setAdapter(adapter);



        //// alert dialog for list ////

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Step 3: Create and display an AlertDialog

//                Toast.makeText(MainActivity.this, "Touched an item", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "items.getPosition is name of list: " + items.get(position));

//                Log.d(TAG, "retrive items is list of items: " + retrieveItemsFromSharedPreferences(items.get(position)));
                ArrayList<String> itemList = (ArrayList<String>) retrieveItemsFromSharedPreferences(items.get(position));
                DialogFragment multiChoiceDialog = new MultipuleChoiceDialogFragment(items.get(position), itemList);
//                DialogFragment multiChoiceDialog = new MultipuleChoiceDialogFragment();
                multiChoiceDialog.setCancelable(false);
                multiChoiceDialog.show(getSupportFragmentManager(),"multiChoice Dialog");
            }
        });


        userID = getIntent().getStringExtra("userId");
        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String userFirstName = sp.getString("first",null);
        TextView title = findViewById(R.id.mainHead);
        title.setText("Hello " + userFirstName);



        mylist = findViewById(R.id.mylist);

        Button listBtn = findViewById(R.id.newListBtn);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewList.class);
                startActivity(intent);
                finish();
            }
        });

        ///_ INTERNET CONNECTION___

        broadcastReceiver = new internetReceiver();
        internetStatus();
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


    private void aboutAlertDialog()    {
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
    private void exitAlertDialog()    {
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

    public void internetStatus(){
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void loadKeyNames() {
        Map<String, ?> allEntries = sp.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key != null && key.startsWith("list_")) {
                // Perform the necessary operations
                String listName = sp.getString(key, "");
//                Log.d(TAG, "loadKeyNames: " + key);
                items.add(listName);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.activity_list_item, items);
        listView.setAdapter(adapter);
    }

    private List<String> retrieveItemsFromSharedPreferences(String listName) {
        // Retrieve the SharedPreferences instance
        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        // Retrieve the StringSet based on the clicked row position
        Set<String> itemSet = sp.getStringSet(listName, new HashSet<>());
//        Log.d(TAG, "item set is: " + itemSet);
        // Convert the Set to a List for easier manipulation
        List<String> itemList = new ArrayList<>(itemSet);
//        Log.d(TAG, "itemList is " + itemList);

        return itemList;
    }
}
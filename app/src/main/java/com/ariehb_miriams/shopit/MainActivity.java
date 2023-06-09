package com.ariehb_miriams.shopit;


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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class MainActivity extends AppCompatActivity implements MultipuleChoiceDialogFragment.onMultiChoiceListener {
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
        sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        listView = findViewById(R.id.listView);
        items = new ArrayList<>();
        loadKeyNames(); // Call the method to load key names into the ListView

        adapter = new CustomListAdapter(getApplicationContext(),items);
        listView.setAdapter(adapter);


        //// alert dialog for list ////

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Step 3: Create and display an AlertDialog

                ArrayList<String> itemList = (ArrayList<String>) retrieveItemsFromSharedPreferences(items.get(position));
                DialogFragment multiChoiceDialog = new MultipuleChoiceDialogFragment(items.get(position), itemList);
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

    }
    @Override
    public void onNegativeButtonClicked() {

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
                items.add(listName);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.activity_list_item, items);
        listView.setAdapter(adapter);
    }

    private List<String> retrieveItemsFromSharedPreferences(String listName) {
        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Set<String> itemSet = sp.getStringSet(listName, new HashSet<>());
        List<String> itemList = new ArrayList<>(itemSet);
        return itemList;
    }
}
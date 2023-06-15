package com.ariehb_miriams.shopit;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.SimpleCursorAdapter;

public class ContactList extends ListActivity{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {Phone._ID, Phone.DISPLAY_NAME, Phone.NUMBER}, null, null, null);

        startManagingCursor(cursor);

        String[] from = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER};

        int[] to = new int[] { R.id.nameC, R.id.numberC};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_list_entry, cursor, from, to);
        this.setListAdapter(adapter);
    }
}


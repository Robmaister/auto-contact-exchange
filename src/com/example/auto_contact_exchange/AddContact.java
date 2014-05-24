package com.example.auto_contact_exchange;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class AddContact extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_contact_screen);

		final ArrayList<String> oldContacts = getContacts();

		Button next = (Button) findViewById(R.id.next);
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent callContactAdd = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI); 
				startActivity(callContactAdd);
				String changedNum = getChangedNumber(oldContacts, getContacts());
				Log.d("id", changedNum);
			}
		});
	}

	public ArrayList<String> getContacts() {
		ArrayList<String> contacts = new ArrayList<String>();
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if(cur.getCount() > 0) {
			while(cur.moveToNext()) {
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)); 
				contacts.add(id);
			}
		}
		
		return contacts;
	}

	public String getChangedNumber(ArrayList<String> oldContacts, ArrayList<String> newContacts) {
		newContacts.removeAll(oldContacts);
		if(newContacts.isEmpty())
			return "";
		
		String id = newContacts.get(0);
		
		String number = "";
		
		
		return id;
	}
	
	
	
	
	/*public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/
}

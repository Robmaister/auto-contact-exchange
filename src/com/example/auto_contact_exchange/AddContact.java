package com.example.auto_contact_exchange;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.telephony.SmsManager;
import android.test.mock.MockContentResolver;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class AddContact extends Activity implements OnAddedContactFound {

	private static final int CONTACT_ADDED = 100;

	private ArrayList<Long> oldContacts;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_contact_screen);

		oldContacts = getContacts();

		Button next = (Button) findViewById(R.id.next);
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent callContactAdd = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI); 
				startActivityForResult(callContactAdd, CONTACT_ADDED);
			}
		});
	}

	public ArrayList<Long> getContacts() {
		ArrayList<Long> contacts = new ArrayList<Long>();
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if(cur.getCount() > 0) {
			while(cur.moveToNext()) {
				contacts.add(cur.getLong(cur.getColumnIndex(ContactsContract.Contacts._ID)));
			}
		}

		return contacts;
	}

	@Override
	public void onAddedContactFound(Long contact) {
		Toast.makeText(this, ""+contact, Toast.LENGTH_LONG).show();
		
		/****************************
		 * Find Rest of Contact Info*
		 ****************************/
		Cursor result = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, 
				ContactsContract.Contacts._ID +" = ?", 
				new String[]{""+contact}, null);      
		if (result.moveToFirst()) {

			for(int i=0; i< result.getColumnCount(); i++){
				Log.i("CONTACTSTAG", result.getColumnName(i) + ": "
						+ result.getString(i));
			}         
		}

		String phoneNumber = getPhoneNumber(contact);
		
		/****************
		 * To Send texts*
		 ***************/
		/*SmsManager manager = SmsManager.getDefault();
		manager.sendTextMessage(phoneNumber, null, "What's Up?", null, null);*/
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		ContactListPair clp = new ContactListPair(oldContacts, getContacts());
		FindAddedContactTask task = new FindAddedContactTask(AddContact.this);
		task.execute(clp);
	}

	public String getPhoneNumber(long id) {
		ArrayList<String> phones = new ArrayList<String>();
		ContentResolver m = getContentResolver();
		Cursor cursor = m.query(
				CommonDataKinds.Phone.CONTENT_URI, 
				null, 
				CommonDataKinds.Phone.CONTACT_ID +" = ?", 
				new String[]{""+id}, null);

		while (cursor.moveToNext()) 
		{
			phones.add(cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER)));
		} 

		cursor.close();
		
		return phones.get(0);
	}


}

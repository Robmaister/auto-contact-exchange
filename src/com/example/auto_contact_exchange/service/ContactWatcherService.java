package com.example.auto_contact_exchange.service;

import java.util.ArrayList;

import com.example.auto_contact_exchange.AddContact;
import com.example.auto_contact_exchange.ContactListPair;
import com.example.auto_contact_exchange.FindAddedContactTask;
import com.example.auto_contact_exchange.OnAddedContactFound;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

public class ContactWatcherService extends Service implements OnContactChanged, OnAddedContactFound {

	ContactObserver observer;
	ArrayList<String> oldContacts;
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d("SERVICE", "BOUND");
		Toast.makeText(this, "CWS bound", Toast.LENGTH_SHORT).show();
		return null;
	}
	
	@Override
	public void onCreate() {
		oldContacts = getContacts();
		observer = new ContactObserver(this);
		getApplicationContext().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_VCARD_URI, false, observer);
	}

	@Override
	public void onContactChanged(Uri contact) {
		Log.d("SERVICE", "CWS: " + contact.toString());
		ContactListPair clp = new ContactListPair(oldContacts, getContacts());
		FindAddedContactTask task = new FindAddedContactTask(this);
		task.execute(clp);
	}
	
	@Override
	public void onDestroy() {
		getApplicationContext().getContentResolver().unregisterContentObserver(observer);
	}
	
	@Override
	public void onAddedContactFound(String contact) {
		// TODO Auto-generated method stub
		
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
}

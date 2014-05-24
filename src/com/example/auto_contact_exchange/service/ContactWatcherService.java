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
	ArrayList<Long> oldContacts;
	
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
	public void onAddedContactFound(Long contact) {
		// TODO Auto-generated method stub
		
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
}

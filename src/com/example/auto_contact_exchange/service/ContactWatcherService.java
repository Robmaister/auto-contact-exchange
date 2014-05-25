package com.example.auto_contact_exchange.service;

import java.util.ArrayList;

import com.example.auto_contact_exchange.AddContact;
import com.example.auto_contact_exchange.ContactListPair;
import com.example.auto_contact_exchange.FindAddedContactTask;
import com.example.auto_contact_exchange.OnAddedContactFound;
import com.example.auto_contact_exchange.R;
import com.example.auto_contact_exchange.SettingsActivity;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class ContactWatcherService extends Service implements OnContactChanged, OnAddedContactFound {

	ContactObserver observer;
	ArrayList<Long> oldContacts;
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d("SERVICE", "BOUND");
		return null;
	}
	
	@Override
	public void onCreate() {
		PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
		
		oldContacts = getContacts();
		observer = new ContactObserver(this);
		getApplicationContext().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_VCARD_URI, false, observer);
	}

	@Override
	public void onContactChanged(Uri contact) {
		ContactListPair clp = new ContactListPair(oldContacts, getContacts());
		FindAddedContactTask task = new FindAddedContactTask(this);
		task.execute(clp);
	}
	
	@Override
	public void onDestroy() {
		getApplicationContext().getContentResolver().unregisterContentObserver(observer);
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onAddedContactFound(Long contact) {
		oldContacts = getContacts();
		
		if (contact == -1)
			return;
		
		/****************************
		 * Find Rest of Contact Info*
		 ****************************/
		Cursor result = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, 
				ContactsContract.Contacts._ID +" = ?", 
				new String[]{""+contact}, null);      
		/*if (result.moveToFirst()) {

			for(int i=0; i< result.getColumnCount(); i++){
				Log.i("CONTACTSTAG", result.getColumnName(i) + ": "
						+ result.getString(i));
			}        
		}*/

		
		/*************
		 * Get Number*
		 *************/
		
		String phoneNumber = getPhoneNumber(contact);
		
		/*****************
		 * Create Message*
		 *****************/
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String prefMesg = sharedPref.getString("pref_mesg_str", "");
		if (prefMesg.equals(""))
			prefMesg = "Yo, what's up? Couldn't find the string from preferences. My name is {myname} though. Sorry dude.";
		
		String myName;
		
		Cursor c = getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
		c.moveToFirst();
		myName = c.getString(c.getColumnIndex("display_name"));
		
		prefMesg = prefMesg.replaceAll("\\Q{myname}\\E", myName);
		
		/***********
		 * Send SMS*
		 ***********/
		SmsManager manager = SmsManager.getDefault();
		manager.sendTextMessage(phoneNumber, null, prefMesg, null, null);
		
		/***********
		 * Send MMS*
		 ***********/
		
		/***************************
		 * Send SMS with WebService*
		 ***************************/
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

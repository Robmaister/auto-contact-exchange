package com.example.auto_contact_exchange.service;

import android.database.ContentObserver;
import android.net.Uri;

public class ContactObserver extends ContentObserver {

	OnContactChanged callback;
	
	public ContactObserver(OnContactChanged c) {
		super(null);
		callback = c;
	}

	@Override
	public void onChange(boolean selfChange)
	{
		this.onChange(selfChange, null);
	}
	
	@Override
	public void onChange(boolean selfChange, Uri uri)
	{
		callback.onContactChanged(uri);
	}
}

package com.example.auto_contact_exchange;

import java.util.ArrayList;

import android.os.AsyncTask;

public class FindAddedContactTask extends AsyncTask<ContactListPair, Void, String>
{
	private OnAddedContactFound callback;
	
	public FindAddedContactTask(OnAddedContactFound c)
	{
		callback = c;
	}
	
	@Override
	protected String doInBackground(ContactListPair... params)
	{
		int count = params.length;
		if (count == 0)
			return "";
		
		ArrayList<String> oldContacts = params[0].getOldContacts();
		ArrayList<String> newContacts = params[0].getNewContacts();
		
		newContacts.removeAll(oldContacts);
		if(newContacts.isEmpty())
			return "";
		
		String id = newContacts.get(0);
		return id;
	}
	
	protected void onPostExecute(String result)
	{
		callback.onAddedContactFound(result);
	}

}



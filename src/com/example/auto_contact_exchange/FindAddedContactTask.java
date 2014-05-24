package com.example.auto_contact_exchange;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;

public class FindAddedContactTask extends AsyncTask<ContactListPair, Void, Long>
{
	private OnAddedContactFound callback;
	
	public FindAddedContactTask(OnAddedContactFound c)
	{
		callback = c;
	}
	
	@Override
	protected Long doInBackground(ContactListPair... params)
	{
		int count = params.length;
		if (count == 0)
			return -1l;
		
		ArrayList<Long> oldContacts = params[0].getOldContacts();
		ArrayList<Long> newContacts = params[0].getNewContacts();
		
		newContacts.removeAll(oldContacts);
		Log.d("length", "" + newContacts.size());
		if(newContacts.isEmpty())
			return -1l;
		
		long id = newContacts.get(0);
		return id;
	}
	
	protected void onPostExecute(Long result)
	{
		callback.onAddedContactFound(result);
	}

}



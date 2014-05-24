package com.example.auto_contact_exchange;

import java.util.ArrayList;

public class ContactListPair
{
	private ArrayList<Long> oldContacts;
	private ArrayList<Long> newContacts;
	
	public ContactListPair(ArrayList<Long> oldContacts2, ArrayList<Long> arrayList)
	{
		oldContacts = oldContacts2;
		newContacts = arrayList;
	}
	
	public ArrayList<Long> getOldContacts()
	{
		return oldContacts;
	}
	
	public ArrayList<Long> getNewContacts()
	{
		return newContacts;
	}
}

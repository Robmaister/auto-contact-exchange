package com.example.auto_contact_exchange;

import java.util.ArrayList;

public class ContactListPair
{
	private ArrayList<String> oldContacts;
	private ArrayList<String> newContacts;
	
	public ContactListPair(ArrayList<String> old, ArrayList<String> n)
	{
		oldContacts = old;
		newContacts = n;
	}
	
	public ArrayList<String> getOldContacts()
	{
		return oldContacts;
	}
	
	public ArrayList<String> getNewContacts()
	{
		return newContacts;
	}
}

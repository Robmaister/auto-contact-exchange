package com.example.auto_contact_exchange;


import com.example.auto_contact_exchange.service.ContactWatcherService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startServiceIntent = new Intent(context, ContactWatcherService.class);
		context.startService(startServiceIntent);
	}
	
}

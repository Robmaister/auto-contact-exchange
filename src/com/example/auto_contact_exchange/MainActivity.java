package com.example.auto_contact_exchange;

import com.example.auto_contact_exchange.service.ContactWatcherService;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//makes sure the service is running
		Intent startServiceIntent = new Intent(this, ContactWatcherService.class);
		startService(startServiceIntent);
		
		Button settingsBtn = (Button)findViewById(R.id.button1);
		settingsBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivity(settingsIntent);
			}
		});
		
		/*Button next = (Button) findViewById(R.id.next);
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent nextScreen = new Intent(getApplicationContext(), AddContact.class);
                startActivity(nextScreen);
			}
		});*/
	}
}

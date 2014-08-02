package com.kukool.iosapp.weather.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SystemStartReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent serviceIntent = new Intent(context, WeatherService.class);  
        context.startService(serviceIntent);  
	}

}

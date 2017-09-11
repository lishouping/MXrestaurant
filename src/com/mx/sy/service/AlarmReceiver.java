package com.mx.sy.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		// 再次开启LongRunningService这个服务，从而可以
		Intent i = new Intent(context, PendingRemindedService.class);
		context.startService(i);

	}

}

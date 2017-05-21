package com.labs.invicta.refalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class alarmReceiver extends BroadcastReceiver {
    private boolean screenOff;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            //Do something when the screen goes off
            Log.v("screen off",", hopefully");
            screenOff=true;

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            //Do something when it's back on
            Log.v("screen on",", hopefully");
            screenOff=false;
        }
        Intent i = new Intent(context, alarmService.class);
        i.putExtra("screen_state", screenOff);
        context.startService(i);
    }
}
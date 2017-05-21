package com.labs.invicta.refalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

public class alarmService extends Service {

    private final BroadcastReceiver myReceiver = new alarmReceiver();
    //used for register alarm manager
    PendingIntent pendingIntent;
    //used to store running alarmmanager instance
    AlarmManager alarmManager;
    //Callback function for Alarmmanager event
    BroadcastReceiver mReceiver;

    Context context=this;
    Intent svc;

    boolean alarmSet= false;

    @Override
    public void onCreate() {
        super.onCreate();
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(myReceiver, filter);
        RegisterAlarmBroadcast();
    }
    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        SetAlarm(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
        try{
            UnregisterAlarmBroadcast();
        } catch (Exception e){
            // already unregistered
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void SetAlarm(Intent intent)
    {

        boolean screenOff ;

        try{
            // Get ON/OFF values sent from receiver ( AEScreenOnOffReceiver.java )
            screenOff = intent.getBooleanExtra("screen_state", false);

        }catch(Exception e){
            screenOff=false;
        }
        SharedPreferences getAlarmFrequency = PreferenceManager.
                getDefaultSharedPreferences(getBaseContext());
        int keyvalue=Integer.parseInt(getAlarmFrequency.getString("frequency","0"));
        long FreqinMS=15000;
        if(keyvalue==1)
            FreqinMS=15*60*1000;
        else if(keyvalue==2)
        FreqinMS=30*60*1000;
        else if(keyvalue==3)
        FreqinMS=60*60*1000;
        else if(keyvalue==4)
        FreqinMS=3*60*60*1000/2;
        else if(keyvalue==5)
        FreqinMS=2*60*60*1000;
        else if(keyvalue==6)
        FreqinMS=5*60*60*1000/2;
        else if(keyvalue==7)
        FreqinMS=3*60*60*1000;
        else if(keyvalue==8)
        FreqinMS=4*60*60*1000;
        else if(keyvalue==9)
        FreqinMS=5*60*60*1000;
        else if(keyvalue==10)
        FreqinMS=6*60*60*1000;
        else if(keyvalue==11)
        FreqinMS=7*60*60*1000;
        else if(keyvalue==12)
        FreqinMS=8*60*60*1000;
        else if(keyvalue==13)
        FreqinMS=9*60*60*1000;
        else if(keyvalue==14)
        FreqinMS=10*60*60*1000;
        if (screenOff) {
            alarmManager.set( AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + FreqinMS , pendingIntent );
            alarmSet=true;
            Log.v("Alarm set",", hopefully");
        }else if(!screenOff && alarmSet ){
            try{
                alarmManager.cancel(pendingIntent);
                stopService(svc);
                alarmSet=false;
            } catch (Exception e){
                // already unregistered
            }
        }
    }
    private void RegisterAlarmBroadcast()
    {
        Log.i("Alarm", "Going to register Intent.RegisterAlramBroadcast");

        //This is the call back function(BroadcastReceiver) which will be call when your
        //alarm time will reached.
        mReceiver = new BroadcastReceiver()
        {
            private static final String TAG = "Alarm Example Receiver";
            @Override
            public void onReceive(Context context, Intent intent)
            {
                svc=new Intent(context, BackgroundSoundService.class);
                context.startService(svc);
                Log.v("Sound service called",", hopefully");
            }
        };

        // register the alarm broadcast here
        context.registerReceiver(mReceiver, new IntentFilter("com.labs.invicta.refalarm") );
        pendingIntent = PendingIntent.getBroadcast(context , 0, new Intent("com.labs.invicta.refalarm"),0 );
        alarmManager = (AlarmManager)(context.getSystemService( Context.ALARM_SERVICE ));
    }
    private void UnregisterAlarmBroadcast()
    {
        Log.v("Sound service stopped",", hopefully");
        stopService(svc);
        alarmManager.cancel(pendingIntent);
        context.unregisterReceiver(mReceiver);
        alarmSet=false;
    }
}

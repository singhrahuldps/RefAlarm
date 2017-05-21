package com.labs.invicta.refalarm;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;

public class BackgroundSoundService extends Service {
    private static final String TAG = null;
    MediaPlayer mMediaPlayer;
    public IBinder onBind(Intent arg0) {

        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        alarm();
    }

    private void alarm(){
        SharedPreferences getAlarms = PreferenceManager.
                getDefaultSharedPreferences(getBaseContext());
        String alarms = getAlarms.getString("ringtone", "default ringtone");
        Uri uri = Uri.parse(alarms);
        Log.v("Sound service started",", hopefully");
        playSound(this, uri);

        //call mMediaPlayer.stop(); when you want the sound to stop
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        mMediaPlayer.start();
        return 1;
    }

    public void onStart(Intent intent, int startId) {
        // TO DO
    }
    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {

    }
    public void onPause() {

    }
    @Override
    public void onDestroy() {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
    }

    @Override
    public void onLowMemory() {

    }
    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.setLooping(true); // Set looping
                mMediaPlayer.setVolume(100,100);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }
}

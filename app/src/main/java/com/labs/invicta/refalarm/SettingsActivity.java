package com.labs.invicta.refalarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            SwitchPreference onoffSwitch = (SwitchPreference) findPreference("onoff");

            if (onoffSwitch != null) {
                onoffSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference arg0, Object isAlarmOnObject) {
                        boolean isAlarmOn = (Boolean) isAlarmOnObject;
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        Intent mServiceIntent = new Intent(getActivity(), alarmService.class);
                        String alarms = sharedPrefs.getString("ringtone", "default ringtone");
                        Uri uri = Uri.parse(alarms);
                        mServiceIntent.setData(uri);
                        if (isAlarmOn) {
                            getActivity().startService(mServiceIntent);
                            Log.v("service started",", hopefully");
                        }else{
                            getActivity().stopService(mServiceIntent);
                            Log.v("service stopped",", hopefully");
                        }
                        return true;
                    }
                });

            }
        }
    }
}

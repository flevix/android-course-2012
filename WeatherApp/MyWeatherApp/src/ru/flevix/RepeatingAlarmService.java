package ru.flevix;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 05.12.12
 * Time: 5:42
 */

public class RepeatingAlarmService extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try{
            Log.i("//service//", "repeatingAlarmServiceIn");
            new DownloadHelper().getForecast(context.getContentResolver(), sharedPreferences.getInt("id", 773), sharedPreferences.getString("city", "Санкт-Петербург"));
        } catch (Exception ignored) {
            Log.i("//service//", "repeatingAlarmServiceFail");
        }
        Log.i("//service//", "repeatingAlarmServiceOut");
    }
}

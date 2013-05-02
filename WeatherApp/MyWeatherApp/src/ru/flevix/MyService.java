package ru.flevix;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 05.12.12
 * Time: 2:39
 */
public class MyService extends Service {
    public static final long INTERVAL = 60 * 1000 * 30; // 30 min
    public static final int FIRST_RUN = 1000; // 1 mls

    private AlarmManager alarmManager;
    private static final int alarmCode = 1;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("//service//", "onCreate");
        startService();
    }

    @Override
    public void onDestroy() {
        Log.i("//service//", "onDestroy");
        if (alarmManager != null) {
            Intent intent = new Intent(this, RepeatingAlarmService.class);
            alarmManager.cancel(PendingIntent.getBroadcast(this, alarmCode, intent, 0));
        }
    }

    private void startService() {
        Log.i("//service//", "startService");
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        long interval = preferences.getLong("interval", INTERVAL);
        Log.i("//service//", "interval: " + (interval/1000) + 'c');
        Intent intent = new Intent(this, RepeatingAlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmCode, intent, 0);

        Log.i("//service//", "alarmManager");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + FIRST_RUN,
                interval,
                pendingIntent);
    }
}

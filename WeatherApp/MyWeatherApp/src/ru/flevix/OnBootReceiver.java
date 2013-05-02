package ru.flevix;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 05.12.12
 * Time: 5:35
 */
public class OnBootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent serviceLauncher = new Intent(context, MyService.class);
            context.startService(serviceLauncher);
            Log.i("//service//", "service loaded after reboot");
        }
    }
}

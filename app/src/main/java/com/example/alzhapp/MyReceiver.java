package com.example.alzhapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    public Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    public static Ringtone ringtone;
    @Override
    public void onReceive(Context context, Intent intent) {
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();
    }

}

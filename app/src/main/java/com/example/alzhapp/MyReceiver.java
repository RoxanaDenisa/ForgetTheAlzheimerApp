package com.example.alzhapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    public static final String ACTION_RECEIVER = "Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MainActivity", "triggered");
        BroadcastObserver.getInstance().updateValue(intent);
    }
}

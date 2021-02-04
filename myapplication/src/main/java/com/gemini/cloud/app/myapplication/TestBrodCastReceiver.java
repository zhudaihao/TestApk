package com.gemini.cloud.app.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TestBrodCastReceiver extends BroadcastReceiver {
    private static final String TAG = "zdh";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG,"-------------TestBrodCastReceiver "+intent.getAction());
    }
}

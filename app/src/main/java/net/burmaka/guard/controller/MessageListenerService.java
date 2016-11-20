package net.burmaka.guard.controller;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class MessageListenerService extends Service {
    private static final String TAG = "myLogs";
    public static final String SMS_RECEIVER_TAG = "SMS_RECEIVER_TAG";
    BroadcastReceiver smsReceiver;

    public MessageListenerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "------MessageListenerService : onCreate: ");
        smsReceiver = new SMSReceiver();

        IntentFilter intentFilter = new IntentFilter(SMS_RECEIVER_TAG);
        registerReceiver(smsReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "------MessageListenerService : onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
        Log.d(TAG, "------MessageListenerService : onDestroy: ");
    }
}

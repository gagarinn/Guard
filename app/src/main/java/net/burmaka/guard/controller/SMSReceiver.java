package net.burmaka.guard.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Burmaka V on 24.10.2016.
 */
public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "myLogs";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "------MessageListenerService : onReceive: ");
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        Log.d(TAG, "------MessageListenerService : onReceive: =========== " + msgBody);
                    }
                }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
}

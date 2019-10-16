package com.efunhub.groceryshop.broadcastRecivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.efunhub.groceryshop.interfaces.SmsListener;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class IncomingSms extends BroadcastReceiver {

    private String TAG = "IncomingSms";
    private static SmsListener mListener;

    Handler h = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            doTimeout();
        }
    };

    public void setTimeout() {
        h.postDelayed(r, 1800000);
    }

    public void cancelTimeout() {
        h.removeCallbacks(r);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(action)) {
            cancelTimeout();
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            switch(status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    String smsMessage = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    smsMessage.replaceAll("[^0-9]","");
                    Log.d(TAG, "Retrieved sms code: " +  smsMessage.replaceAll("[^0-9]",""));
                    if (smsMessage != null) {
                        //verifyMessage(smsMessage);
                        mListener.messageReceived( smsMessage.replaceAll("[^0-9]",""));
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    doTimeout();
                    break;
                default:
                    break;
            }
        }
    }

    private void doTimeout() {
        Log.d(TAG, "Waiting for sms timed out.");
            /*Toast.makeText(PhoneNumberVerifier.this,
                    getString(R.string.toast_unverified), Toast.LENGTH_LONG).show();*/
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}

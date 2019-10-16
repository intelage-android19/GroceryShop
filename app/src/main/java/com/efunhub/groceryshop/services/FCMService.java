package com.efunhub.groceryshop.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.activities.MainActivity;
import com.efunhub.groceryshop.utility.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class FCMService extends FirebaseMessagingService {

    private static final String TAG = FCMService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.v(TAG, "From: " + remoteMessage.getFrom());
        Log.v(TAG, "Data: " + remoteMessage.getData());
        Log.v(TAG, "Type: " + remoteMessage.getMessageType());
        Log.v(TAG, "Notification: " + remoteMessage.getNotification());
        Log.v(TAG, "Time: " + remoteMessage.getSentTime());
        Log.v(TAG, "Ttl: " + remoteMessage.getTtl());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification object
        if (remoteMessage.getNotification() != null) {
            Log.v(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data object
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.v(TAG, "Exception: " + e.getMessage());
            }
        }

    }

    private void handleNotification(String notificationbody) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

            sendNotification(notificationbody);

        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject jsonObject) {
        try {
            //JSONObject data = json.getJSONObject("notification");

            String title = jsonObject.getString("title");
            String message = jsonObject.getString("body");

            String clickaction = null;
            int indexFragmentType = 0;

            if (jsonObject.has("click_action") && !jsonObject.isNull("click_action")) {
                clickaction = jsonObject.getString("click_action");
            }

            if (jsonObject.has("index") && !jsonObject.isNull("index")) {
                indexFragmentType = jsonObject.getInt("index");
            }


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

                sendNotification(message);

            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent;
                if (clickaction == null) {
                    resultIntent = new Intent(this, MainActivity.class);
                } else {
                    resultIntent = new Intent(clickaction);
                }

                if (indexFragmentType == 3) {
                    resultIntent.putExtra("navItemIndex", 3);
                    notifyNavBroadcast(indexFragmentType);
                }

                resultIntent.putExtra("message", message);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.showNotificationMessage(title, message, resultIntent, null);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void notifyNavBroadcast(int indexFragmentType) {
        //Send Notification navigation Broadcast receiver
        Intent intent = new Intent("notificationNavigation");
        intent.putExtra("index", indexFragmentType);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/notification");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Organic Vegetable")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}

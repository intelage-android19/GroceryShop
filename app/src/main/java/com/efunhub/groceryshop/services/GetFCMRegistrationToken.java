package com.efunhub.groceryshop.services;

//import com.google.firebase.iid.FirebaseInstanceIdService;

public class GetFCMRegistrationToken {// extends FirebaseInstanceId

   /* private static final String TAG = "Refresh Token";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.v("Firbase Veg App token", refreshedToken);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.storeRegIdInPref(refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        *//*Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);*//*
    }



    private void sendRegistrationToServer(String refreshedToken) {
        // sending gcm token to server
        //Log.v(TAG, "sendRegistrationToServer: " + refreshedToken);
    }*/
}

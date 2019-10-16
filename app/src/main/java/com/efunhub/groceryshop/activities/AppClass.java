package com.efunhub.groceryshop.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.efunhub.groceryshop.utility.SessionManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AppClass extends MultiDexApplication {

    private SessionManager sessionManager;

    @Override
    public void onCreate() {
        super.onCreate();
        printKeyHsh();
       /* FacebookSdk.sdkInitialize(this);
        sessionManager = new SessionManager(this);

        NetworkCall.setActionPerformer(new DefaultActionPerformer() {
            @Override
            public void onActionPerform(HashMap<String, String> headers, HashMap<String, String> params) {
                if (sessionManager.getToken() != null) {
                    String token = sessionManager.getToken();
                    headers.put("Authorization", token);
                }
            }
        });*/
    }
    private void printKeyHsh() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("mytag", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

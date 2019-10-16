package com.efunhub.groceryshop.utility;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Admin on 26-02-2018.
 */

public class ToastClass {

    public void makeToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}

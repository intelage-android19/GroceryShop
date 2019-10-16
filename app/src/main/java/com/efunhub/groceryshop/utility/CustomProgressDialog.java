package com.efunhub.groceryshop.utility;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.efunhub.groceryshop.R;


public class CustomProgressDialog extends AlertDialog {

    private String title;
    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, String title) {
        super(context);
        this.title = title;
        super.setCancelable(false);
    }


    public void show(String text){
        title = text;
        show();
    }

    @Override
    public void show() {

        try {
            super.show();
            super.setCanceledOnTouchOutside(false);

            if (title!=null && !TextUtils.isEmpty(title)) {
                setContentView(R.layout.custom_progressdialog);
                TextView tvTitle =  findViewById(R.id.progress_bar_text);
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(title);
            } else {
                setContentView(R.layout.custom_progressdialog);
            }

            getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

}
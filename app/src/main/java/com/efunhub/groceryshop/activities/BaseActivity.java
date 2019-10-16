package com.efunhub.groceryshop.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.utility.CommonDialogs;
import com.efunhub.groceryshop.utility.CustomProgressDialog;
import com.efunhub.groceryshop.utility.SessionManager;
import com.efunhub.groceryshop.widget.CustomTextView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


/**
 * Each Activity must extends BaseActivity.
 * It is having some common implementation that each Application is having
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected boolean shouldPerformDispatchTouch = true;
    protected SessionManager session;
    //protected GlideLoader glideLoader;
    CustomTextView title;
    protected long lastClickTime = 0;
    private Snackbar snackbar;


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    // Progress
    private CustomProgressDialog progressDialog;

    // CommonDialogs
    protected CommonDialogs mDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());
        mDialogs = new CommonDialogs(this);
        //glideLoader = new GlideLoader(this);
    }

    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(findViewById(android.R.id.content).getContext(), R.color.colorWhite));
        snackbar.show();
    }

    public void showSnackbar(View view, String msg, int LENGTH) {
        if (view == null) return;
        snackbar = Snackbar.make(view, msg, LENGTH);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        snackbar.show();
    }

    public void showToastShort(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void showErrorSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(findViewById(android.R.id.content).getContext(), R.color.colorPrimaryDark));
        snackbar.show();
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void preventDoubleClick(View view) {
        // preventing double, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if (shouldPerformDispatchTouch) {
            if (view instanceof EditText) {
                try {
                    View w = getCurrentFocus();
                    int scrcords[] = new int[2];
                    w.getLocationOnScreen(scrcords);
                    float x = event.getRawX() + w.getLeft() - scrcords[0];
                    float y = event.getRawY() + w.getTop() - scrcords[1];

                    if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (getWindow() != null && getWindow().getCurrentFocus() != null) {
                            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }


    /**
     * Show Progress Dailog without any message
     */
    public void showProgress() {
        showProgress(null);
    }

    /**
     * Show Progress dialog with Message
     *
     * @param message Message to show under Progress
     */
    public void showProgress(String message) {
        if (progressDialog != null) {
            progressDialog.show(message);
        } else {
            progressDialog = new CustomProgressDialog(this, message);
            progressDialog.show();
        }
    }


    /**
     * Stop Progress if running and dismiss progress Dialog
     */
    public void stopProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * To check weather the progress is running
     *
     * @return True if Progress is running false else.
     */
    protected boolean isProgressShowing() {
        return progressDialog != null && progressDialog.isShowing();
    }

    public void errorHandleFromApi(ArrayList<String> messages) {
        if (messages.size() == 1) {
            showErrorSnackBar(messages.get(0));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(R.string.str_erroe);
            String[] items = messages.toArray(new String[0]);
            builder.setItems(items, null);
            builder.setPositiveButton(R.string.str_Ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void openActivity(Class<?> aClass) {
        startActivity(new Intent(this, aClass));
    }


    public void setUpToolbarWithBackArrow(Toolbar toolbar, String strTitle, boolean isBackArrow, final Activity activity) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(isBackArrow);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.finish();
                }
            });
            ImageView ivBack = (ImageView) toolbar.findViewById(R.id.icMenu);
            ivBack.setImageResource(R.drawable.ic_back_arrow);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            title = toolbar.findViewById(R.id.title);
            title.setText(strTitle);
        }
    }


    public void setUpToolbarWithTitle(Toolbar toolbar, String strTitle, boolean isBackArrow, final Activity activity) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(isBackArrow);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.finish();
                }
            });
            ImageView ivBack = (ImageView) toolbar.findViewById(R.id.icMenu);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            title = toolbar.findViewById(R.id.title);
            title.setText(strTitle);
        }
    }


    public void setUpToolbarWithMenu(Toolbar toolbar, String strTitle, boolean status) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            if (status) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            } else {
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
           /* ImageView iv_edit = (ImageView) toolbar.findViewById(R.id.iv_edit_profile);
            iv_edit.setVisibility(View.GONE);*/
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            title = toolbar.findViewById(R.id.title);
            title.setText(strTitle);


        }
    }

    public void setUpToolbarWithoutTitle(Toolbar toolbar, boolean status) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            if (status) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            } else {
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
           /* ImageView iv_edit = (ImageView) toolbar.findViewById(R.id.iv_edit_profile);
            iv_edit.setVisibility(View.GONE);*/
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);



        }
    }


}

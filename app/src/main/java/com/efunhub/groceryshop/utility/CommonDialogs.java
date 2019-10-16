package com.efunhub.groceryshop.utility;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.efunhub.groceryshop.R;
import com.google.android.material.snackbar.Snackbar;


public class CommonDialogs {

    private Activity activity;

    /**
     * Initializing CommonDialogs that Include, Toast,SnackbarView, AlertDialogs
     * @param acc BaseActivity object to be passed here. Each Activity musht extends BaseActivity in Application
     */
    public CommonDialogs(Activity acc) {
        activity=acc;

    }


    /**
     * Method is called to show Toast in Application
     * @param StringResourceId String resource id. Default it will show Toast for Short time.
     */
    public void showToast(int StringResourceId) {
        showToast(activity.getString(StringResourceId));
    }


    /**
     * Method is called to show Toast in Application
     * @param StringResourceId String resource id. Default it will show Toast for Short time.
     */
    public void showToastForLong(int StringResourceId) {
        showToastForLong(activity.getString(StringResourceId));
    }

    /**
     * Method is called to show Toast in Application
     * @param message String message. Default it will show Toast for Short time.
     */
    public void showToast(String message) {
        Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Method is called to show Toast in Application
     * @param message String message. Default it will show Toast for Short time.
     */
    public void showToastForLong(String message) {
        Toast toast = Toast.makeText(activity, message, Toast.LENGTH_LONG);
        toast.show();
    }



    /**
     * Alert Dialog to show for No internet
     */
    public void showNoInternetDialog() {
        showAlertDialog(activity.getResources().getString(R.string.NetworkError)
                , activity.getResources().getString(R.string.no_internet)
                , activity.getResources().getString(R.string.str_Ok)
                , false, null);
    }

    /**
     *  Alert Dialog to show for No internet
     * @param OnOkclick Get the call back of the OK click event for Alert Dialog
     */
    public void showNoInternetDialog(OnOkClickListner OnOkclick) {
        showAlertDialog(activity.getResources().getString(R.string.NetworkError)
                , activity.getResources().getString(R.string.no_internet)
                , activity.getResources().getString(R.string.str_Ok)
                , false, OnOkclick);
    }


    /**
     * Alert Dialog to show for Error Message in Application
     * @param Message String Value for AlertDialog
     */
    public void showErrorAlertDialog(String Message) {
        showAlertDialog(activity.getResources().getString(R.string.Error)
                , Message
                , activity.getResources().getString(R.string.str_Ok)
                , false, null);
    }

    /**
     * Alert Dialog to show for Error Message in Application
     * @param Message String Value for AlertDialog
     * @param OnOkClick CallBack to get the Ok click event of AlertDialog
     */
    public void showErrorAlertDialog(String Message, OnOkClickListner OnOkClick) {
        showAlertDialog(activity.getResources().getString(R.string.Error)
                , Message
                , activity.getResources().getString(R.string.str_Ok)
                , false, OnOkClick);
    }

    /**
     * Application Alert Dialog to show
     * @param titleResourceId title Text resource id
     * @param message String message
     * @param buttonTextResourseId Button text to show in AlertDialog
     * @param shouldCloseActivityOnDismiss Should Close Activity on Dialog Dismiss. Default value if false
     * @param OnOkclick Button Click callback
     */
    public void showAlertDialog(int titleResourceId, String message, int buttonTextResourseId,
                                final boolean shouldCloseActivityOnDismiss, final OnOkClickListner OnOkclick) {
        showAlertDialog(activity.getResources().getString(titleResourceId)
                , message
                , activity.getResources().getString(buttonTextResourseId)
                , shouldCloseActivityOnDismiss, OnOkclick);
    }

    public void showAlertDialog(int titleResourceId, String message, int buttonTextResourseId,
                                final boolean shouldCloseActivityOnDismiss) {
        showAlertDialog(activity.getResources().getString(titleResourceId)
                , message
                , activity.getResources().getString(buttonTextResourseId)
                , shouldCloseActivityOnDismiss, null);
    }

    public void showAlertDialog(int titleResourceId, String message, int buttonTextResourseId) {
        showAlertDialog(activity.getResources().getString(titleResourceId)
                , message
                , activity.getResources().getString(buttonTextResourseId)
                , false, null);
    }


    /**
     * Application Alert Dialog to show
     * @param title title String
     * @param message message String
     * @param buttonText button String
     * @param shouldCloseActivityOnDismiss Should Close Activity on Dialog Dismiss. Default value if false
     * @param onOkClickListner Button Click callback
     */
    public void showAlertDialog(String title, String message, String buttonText,
                                final boolean shouldCloseActivityOnDismiss, final OnOkClickListner onOkClickListner) {
        AlertDialog alertDialog;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        // set title
        alertDialogBuilder.setTitle(title);
        // set dialog message
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (shouldCloseActivityOnDismiss) {
                    activity.onBackPressed();
                } else {
                    if (onOkClickListner != null) {
                        onOkClickListner.OnOkClick();
                    }
                }
                dialog.dismiss();
            }
        });
        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showAlertDialog(String title, String message, String buttonText) {
        showAlertDialog(title
                , message
                , buttonText
                , false, null);
    }


    /**
     * Application Alert Dialog to show with Two options
     * @param title  title String
     * @param message message String to show
     * @param positiveText Postive button text
     * @param negativeButtonText nagative button Text
     * @param onMultipleButtonClickListener To handel both button click event Callback is passed here.
     */
    public void showAlertDialogWithOptions(String title, String message, String positiveText,
                                           String negativeButtonText, final OnMultipleButtonClickListener
                                                   onMultipleButtonClickListener) {
        AlertDialog alertDialog;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        // set title
        alertDialogBuilder.setTitle(title);
        // set dialog message
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if (onMultipleButtonClickListener != null) {
                    onMultipleButtonClickListener.OnPositiveClick();
                }
            }
        });

        alertDialogBuilder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                dialog.dismiss();
                if (onMultipleButtonClickListener != null) {
                    onMultipleButtonClickListener.OnNegativeClick();
                }
            }
        });

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    /**
     * To show SnackBar Message in Application for Long time
     * @param view Veiw of which parent is considered for Snackbar to show
     * @param message Message text as String to show in SnackBar
     */
    public void showSnackBarForLong(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        TextView textView = (TextView) snackBarView.findViewById(com.google.android.material.R.id.snackbar_text); //Get reference of snackbar textview
        textView.setMaxLines(3); // Change your max lines
        snackBarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
        snackbar.show();
    }


    /**
     * To show SnackBar Message in Application for Short time
     * @param view Veiw of which parent is considered for Snackbar to show
     * @param message Message text as String to show in SnackBar
     */
    public void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
        snackbar.show();
    }


    public interface OnOkClickListner {
        void OnOkClick();
    }

    public interface OnMultipleButtonClickListener {
        void OnPositiveClick();

        void OnNegativeClick();
    }

}


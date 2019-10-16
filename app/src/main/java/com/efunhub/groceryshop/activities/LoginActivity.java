package com.efunhub.groceryshop.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.broadcastRecivers.IncomingSms;
import com.efunhub.groceryshop.databinding.ActivityLoginBinding;
import com.efunhub.groceryshop.interfaces.IResult;
import com.efunhub.groceryshop.interfaces.SmsListener;
import com.efunhub.groceryshop.utility.AppSignatureHelper;
import com.efunhub.groceryshop.utility.SessionManager;
import com.efunhub.groceryshop.utility.ToastClass;
import com.efunhub.groceryshop.utility.VolleyService;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.efunhub.groceryshop.utility.ConstantVariables.CHECK_OTP;
import static com.efunhub.groceryshop.utility.ConstantVariables.RESEND_OTP;
import static com.efunhub.groceryshop.utility.ConstantVariables.USER_LOGIN;
import static com.efunhub.groceryshop.utility.SessionManager.KEY_FCM_TOKEN;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ActivityLoginBinding mBinder;


    //Check internet connectivity
    private boolean connectivityStatus = true;
    private RelativeLayout relativeLayout;
    private LinearLayout noInternetConn;
    private TextView tvRetry;

    private Snackbar snackbar;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 0;
    public static int TYPE_NOT_CONNECTED = 2;

    private SmsRetrieverClient smsRetrieverClient;
    private IncomingSms incomingSms;
    private String TAG = "LoginActiviyt";

    //OTP Dialog
    private EditText edtEnterOTP;
    private TextView txtResend, tvOtpMessage;
    private Button btnSend;
    private AlertDialog alertDialog;
    private ProgressBar pbOTP;
    private String OTP;
    private ProgressBar pgBar;

    //Volley service
    private IResult mResultCallback;
    private VolleyService mVolleyService;

    //all urls
    private String CustomerLoginURL = "Login-Customer";
    private String CheckOTPUrl = "check_otp_customer.php";

    private SessionManager sessionManager;
    private ToastClass toastClass;
    private String cid;

    private String regFcmTokenId;

    // SessionManager sessionManager = new SessionManager(getApplicationContext());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_login);

       setUpUi();

       getFcmToken();

       checkPermissions();

        mBinder.btnLogin.setOnClickListener(this);
        mBinder.tvSignUp.setOnClickListener(this);
        mBinder.tvSkip.setOnClickListener(this);
        mBinder.tvForgotPassword.setOnClickListener(this);

    }


    private void setUpUi() {

        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorTextBlack));

        toastClass = new ToastClass();
        sessionManager = new SessionManager(this);

        //internet connection validation
        //noInternetConn = mBinder.llNoInternet;
        relativeLayout = mBinder.logInlayout;

        smsRetrieverClient = SmsRetriever.getClient(this);
        // Start SMS receiver code
        Task<Void> task = smsRetrieverClient.startSmsRetriever();

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //incomingSms.setTimeout();
                Log.d(TAG, "SmsRetrievalResult status: Success");
                listenSms();

            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "SmsRetrievalResult start failed.", e);
            }
        });
    }

    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }


    private void getFcmToken() {

        // Get token
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        HashMap<String, String> fcmToken = sessionManager.getRegIdPref();

                        regFcmTokenId = fcmToken.get(KEY_FCM_TOKEN);

                        if (regFcmTokenId == null || regFcmTokenId.equals("")) {
                            sessionManager.storeRegIdInPref(token);
                        }


                    }
                });
        // [END retrieve_current_token]
    }

    public void listenSms() {
        IncomingSms.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                if (!TextUtils.isEmpty(messageText)) {
                    edtEnterOTP.setText(messageText);
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
        registerSMSRetriveReceiver();

    }

    private void registerSMSRetriveReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        this.registerReceiver(incomingSms, intentFilter);
    }


    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiver);

        if (incomingSms != null) {
            this.unregisterReceiver(incomingSms);
            incomingSms.cancelTimeout();
            incomingSms = null;
        }
    }

    //to check internet connectivity
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            setSnackbarMessage(status, false);
        }
    };

    /**
     * Method to register runtime broadcast receiver to show snackbar alert for internet connection..
     */
    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(broadcastReceiver, internetFilter);
    }

    public static String getConnectivityStatusString(Context context) {

        int conn = getConnectivityStatus(context);

        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";

        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    public static int getConnectivityStatus(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork) {
            if (activeNetwork.getType() == TYPE_WIFI)
                return TYPE_WIFI;
            if (activeNetwork.getType() == TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    private void setSnackbarMessage(String status, boolean showBar) {

        String internetStatus = "";

        if (status.equalsIgnoreCase("Wifi enabled")) {
            internetStatus = "Internet Connected";
        }
        if (status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = "Internet Connected";
        }
        if (status.equalsIgnoreCase("Not connected to Internet")) {
            internetStatus = "Please check internet connection";
        }
        snackbar = Snackbar
                .make(relativeLayout, internetStatus, Snackbar.LENGTH_LONG)
                .setAction("X", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        // Changing message text color
        snackbar.setActionTextColor(Color.WHITE);
        // Changing action button text color
        View sbView = snackbar.getView();

        TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        if (internetStatus.equalsIgnoreCase("Please check internet connection")) {
            if (connectivityStatus) {
                sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRED));
                snackbar.show();
                connectivityStatus = false;
                //mBinder.no.setVisibility(View.VISIBLE);
            }
        } else {
            if (!connectivityStatus) {
                sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDarkGreen));
                connectivityStatus = true;
                snackbar.show();
                //noInternetConn.setVisibility(View.GONE);

            }
        }
    }


    private void userLogin() {

        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallback, this);

        Map<String, String> params = new HashMap<>();
        //params.put("name", edtName.getText().toString());
        params.put("contact", mBinder.edtLoginMobile.getText().toString());
        params.put("password", mBinder.edtLoginPassword.getText().toString());

        //need to change when implemented notification functionality


      //  String url = getApplicationContext().getResources().getString(R.string.base_url) + CustomerLoginURL;

        mVolleyService.postDataVolleyParameters(USER_LOGIN,
                getApplicationContext().getResources().getString(R.string.base_url) + CustomerLoginURL,
                params);

    }

    private void resendOTP() {

        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallback, this);

        Map<String, String> params = new HashMap<>();
        params.put("contact", mBinder.edtLoginMobile.getText().toString());
        params.put("type", "Resend");

        mVolleyService.postDataVolleyParameters(RESEND_OTP, this.getResources().getString(R.string.base_url) + CheckOTPUrl, params);
    }

    private void sendOTP() {

        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallback, this);

        Map<String, String> params = new HashMap<>();
        params.put("contact", mBinder.edtLoginMobile.getText().toString());
        params.put("otp", edtEnterOTP.getText().toString());
        params.put("type", "Send");

        mVolleyService.postDataVolleyParameters(CHECK_OTP, this.getResources().getString(R.string.base_url) + CheckOTPUrl, params);
    }

    private boolean checkValidations() {

        String passwordRegex = "^[a-zA-Z0-9]{6,16}$";


        if (mBinder.edtLoginMobile.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mBinder.edtLoginMobile.getText().toString().length() != 10) {
            Toast.makeText(this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (mBinder.edtLoginPassword.getText().toString().equalsIgnoreCase("")) {
            //  toastClass.makeToast(this, "Please enter mobile number");
            mBinder.edtLoginPassword.setError("Please enter password ");
            return false;
        } else if (!mBinder.edtLoginPassword.getText().toString().matches(passwordRegex)) {
            mBinder.edtLoginPassword.setError("Please enter valid password ");
            return false;
        }
        return true;
    }

    private boolean checkOTP() {
        if (edtEnterOTP.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edtEnterOTP.getText().toString().length() != 4) {
            Toast.makeText(this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {
                JSONObject jsonObj = null;

                switch (requestId) {

                    case USER_LOGIN:
                        try {
                            jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            if (status == 0) {
                                toastClass.makeToast(getApplicationContext(), "Error");

                            }
                            else if (status == 1) {

                                cid = jsonObj.getString("customer_auto_id");

                                sessionManager.createLoginSession(cid);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                }

                            else if (status == 2) {
                                toastClass.makeToast(getApplicationContext(), "Your account not activated yet. Please Try after some time.");
                            } else if (status == 3) {
                                toastClass.makeToast(getApplicationContext(), "Sorry, an account not exists with this contact");
                            } else if (status == 4) {
                                toastClass.makeToast(getApplicationContext(), "Invalid Contact");

                            }
                        } catch (Exception e) {

                            Log.v("Register_Login", e.toString());
                        }
                        mBinder.btnLogin.setVisibility(View.VISIBLE);
                        mBinder.cvPgBar.setVisibility(View.GONE);

                        break;

                    case CHECK_OTP:

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");

                            if (status == 0) {
                                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
                            } else if (status == 1) {

                                alertDialog.dismiss();

                                cid = jsonObject.getString("cust_id");

                                sessionManager.createLoginSession(cid);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            } else if (status == 2) {
                                Toast.makeText(LoginActivity.this, "Invalid OTP, Please Try Again", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                        pbOTP.setVisibility(View.GONE);
                        btnSend.setVisibility(View.VISIBLE);
                        break;

                    case RESEND_OTP:
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");

                            if (status == 3) {
                                Toast.makeText(LoginActivity.this, "OTP is send to your mobile number!!!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Something went wrong. Please try again later !!!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;



                }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again !!!", Toast.LENGTH_LONG).show();
                Log.v("Volley requestid", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
            }
        };
    }


    private void showOTPDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.otp_dialog, null);
        dialogBuilder.setView(dialogView);

        edtEnterOTP = dialogView.findViewById(R.id.edtotp);
        txtResend = dialogView.findViewById(R.id.txtresendotp);
        pbOTP = dialogView.findViewById(R.id.pbOTP);

        txtResend.setPaintFlags(txtResend.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnSend = dialogView.findViewById(R.id.btnsendotp);

        Log.v("HELLO", edtEnterOTP.getText().toString());

        //dialogBuilder.show();

        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidations()) {
                   // pbOTP.setVisibility(View.VISIBLE);
                    btnSend.setVisibility(View.GONE);
userLogin();
                }
            }
        });

        txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //resendOTP();
            }
        });
    }

    public void genrateHash() {
        //addPreferencesFromResource(R.xml.pref_main);

        //PreferenceScreen screen = getPreferenceScreen();

        AppSignatureHelper signatureHelper = new AppSignatureHelper(this);

        ArrayList<String> appSignatures = signatureHelper.getAppSignatures();

        /*Preference pref = screen.findPreference("app_signature");
        if (!appSignatures.isEmpty() && pref != null) {
            pref.setSummary(appSignatures.get(0));
        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnLogin:
                if (checkValidations()) {
                    userLogin();
                    mBinder.cvPgBar.setVisibility(View.VISIBLE);
                    mBinder.btnLogin.setVisibility(View.GONE);
                }
                break;


            case R.id.tvSignUp:
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                break;

            case R.id.tvSkip:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.tvForgotPassword:
                Intent intent1 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                finish();
                break;

        }
    }
}

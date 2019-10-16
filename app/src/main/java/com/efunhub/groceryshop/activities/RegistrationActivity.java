package com.efunhub.groceryshop.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.databinding.ActivityRegistrationBinding;
import com.efunhub.groceryshop.interfaces.IResult;
import com.efunhub.groceryshop.interfaces.NoInternetListener;
import com.efunhub.groceryshop.utility.CheckConnectivity;
import com.efunhub.groceryshop.utility.SessionManager;
import com.efunhub.groceryshop.utility.ToastClass;
import com.efunhub.groceryshop.utility.VolleyService;
import com.efunhub.groceryshop.widget.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.efunhub.groceryshop.utility.ConstantVariables.USER_REGISTRATION;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityRegistrationBinding mBinder;

    private ToastClass toastClass;
    private ProgressDialog progressDialog;
    private boolean connectivityStatus = true;

    private IResult mResultCallback;
    private VolleyService mVollyService;
    private String userRegistrationURL = "Registration-Customer";
    private CheckConnectivity checkConnectivity;

    TextView tvLogin, tvTerms;
    Button btnRegister;
    Context context;
    CustomEditText etNameProfile, etEmailProfile, edtMobile,edtPassword,edtCountryCode;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_registration);

        setUpUi();
        init();
        btnRegister.setOnClickListener(this);

    }


    private void setUpUi() {


        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        mBinder.tvAlreadyHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorTextBlack));
    }


    private void userRegistration() {

        initVolleyCallback();

        //  rlLayout.setVisibility(View.VISIBLE);

        mVollyService = new VolleyService(mResultCallback, this);

        HashMap<String, String> params = new HashMap<>();
        params.put("name", etNameProfile.getText().toString());
        params.put("email", etEmailProfile.getText().toString());
        params.put("contact", edtMobile.getText().toString());
        params.put("country_code", edtCountryCode.getText().toString());
        params.put("password", edtPassword.getText().toString());

        mVollyService.postDataVolleyParameters(USER_REGISTRATION,
                this.getResources().getString(R.string.base_url) + userRegistrationURL, params);
    }


    private void initVolleyCallback() {
        try {
            mResultCallback = new IResult() {
                @Override
                public void notifySuccess(int requestId, String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            Toast.makeText(getApplicationContext(), "successfully Registered", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else if (status == 2) {
                            Toast.makeText(getApplicationContext(), "Sorry, an account already exists with this email id and mobile no.", Toast.LENGTH_SHORT).show();
                        } else if (status == 3) {
                            Toast.makeText(getApplicationContext(), "Sorry, an account already exists with this email id", Toast.LENGTH_SHORT).show();
                        } else if (status == 4) {
                            Toast.makeText(getApplicationContext(), "Sorry, an account already exists with this mobile no.", Toast.LENGTH_SHORT).show();
                        } else if (status == 5) {
                            Toast.makeText(getApplicationContext(), "Invalid email format.", Toast.LENGTH_SHORT).show();
                        } else if (status == 6) {
                            Toast.makeText(getApplicationContext(), "Invalid contact.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void notifyError(int requestId, VolleyError error) {
                    Log.v("Volley requestid ", String.valueOf(requestId));
                    Log.v("Volley Error", String.valueOf(error));
                }
            };
        } catch (Exception ex) {

            Log.d("RegisterationActivity", "initVolleyCallback: " + ex);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                if (checkValid()) {
                    progressDialog = ProgressDialog.show(this, "GroKisan",
                            "Please wait while loading..", false, true);
                    progressDialog.show();
                    userRegistration();
                   /* if (ConstantMethod.isInternetOn(RegistrationActivity.this)) {
                        {
                        }
                    }*/
                }
                break;
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
    }

    //validations

    public boolean checkValid() {

        String namePattern = "^[a-zA-Z -]+$";

        String emailPattern = "^[a-zA-Z]\\w+(.\\w+)*@\\w+(.[0-9a-zA-Z]+)*.[a-zA-Z]{2,4}$";

        String phoneRegex = "^[6-9][0-9]{9}$";

        String passwordRegex = "^[a-zA-Z0-9]{6,16}$";

        if(TextUtils.isEmpty(etNameProfile.getText().toString())){
            etNameProfile.setError("Please enter name");
            return false;
        }
        else if (etEmailProfile.getText().toString().equalsIgnoreCase("")) {
            etEmailProfile.setError("Please enter email id");
            return false;
        } else if (!etEmailProfile.getText().toString().matches(emailPattern)) {
            etEmailProfile.setError("Please enter valid email id");
            return false;
        } else if (edtMobile.getText().toString().equalsIgnoreCase("")) {
            //  toastClass.makeToast(this, "Please enter mobile number");
            edtMobile.setError("Please enter mobile number");
            return false;
        } else if (!edtMobile.getText().toString().matches(phoneRegex)) {
            edtMobile.setError("Please enter valid phone number");
            return false;
        }else if (edtCountryCode.getText().toString().equalsIgnoreCase("")) {
            //  toastClass.makeToast(this, "Please enter mobile number");
            edtCountryCode.setError("Please enter country code ");
            return false;
        }
        else if (edtPassword.getText().toString().equalsIgnoreCase("")) {
            //  toastClass.makeToast(this, "Please enter mobile number");
            edtPassword.setError("Please enter password ");
            return false;
        } else if (!edtPassword.getText().toString().matches(passwordRegex)) {
            edtPassword.setError("Please enter valid password ");
            return false;
        }
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();

        //Check connectivity
        checkConnectivity = new CheckConnectivity(RegistrationActivity.this, new NoInternetListener() {
            @Override
            public void availConnection(boolean connection) {
                if (connection) {
                    connectivityStatus = true;
                } else {
                    connectivityStatus = false;
                }
            }
        });
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        RegistrationActivity.this.registerReceiver(checkConnectivity, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        /*UnRegister receiver for connectivity*/
        this.unregisterReceiver(checkConnectivity);
    }


    public void init() {

        btnRegister = (Button) findViewById(R.id.btnRegister);
        etNameProfile = (CustomEditText) findViewById(R.id.etNameProfile);
        etEmailProfile = (CustomEditText) findViewById(R.id.edtEmail);
        edtMobile = (CustomEditText) findViewById(R.id.edtMobile);
        edtPassword = (CustomEditText) findViewById(R.id.edtPassword);
        edtCountryCode = (CustomEditText) findViewById(R.id.edtCountryCode);


    }
}

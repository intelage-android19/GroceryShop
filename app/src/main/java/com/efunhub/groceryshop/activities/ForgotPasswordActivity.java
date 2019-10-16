package com.efunhub.groceryshop.activities;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.databinding.ForgotPasswordActivityBinding;
import com.efunhub.groceryshop.interfaces.IResult;
import com.efunhub.groceryshop.utility.ToastClass;
import com.efunhub.groceryshop.utility.VolleyService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.efunhub.groceryshop.utility.ConstantVariables.CHECK_OTP;
import static com.efunhub.groceryshop.utility.ConstantVariables.RESEND_OTP;
import static com.efunhub.groceryshop.utility.ConstantVariables.USER_LOGIN;

public class ForgotPasswordActivity extends AppCompatActivity {

    ForgotPasswordActivityBinding mBinder;

    //Volley service
    private IResult mResultCallback;
    private VolleyService mVolleyService;

    //all urls
    private String forgotPassword = "Forgot-Password-Customer";

    private ToastClass toastClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinder = DataBindingUtil.setContentView(this, R.layout.forgot_password_activity);
        toastClass = new ToastClass();

mBinder.btnSendForgotEmail.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        forgotPassword();
    }
});

    }

    private void forgotPassword() {

        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallback, this);

        Map<String, String> params = new HashMap<>();
        //params.put("name", edtName.getText().toString());
        params.put("email", mBinder.edtForgotPassword.getText().toString());

        //need to change when implemented notification functionality


        //  String url = getApplicationContext().getResources().getString(R.string.base_url) + CustomerLoginURL;

        mVolleyService.postDataVolleyParameters(USER_LOGIN,
                getApplicationContext().getResources().getString(R.string.base_url) + forgotPassword,
                params);

    }

    private void initVolleyCallback() {
        mBinder.cvPgBar.setVisibility(View.VISIBLE);

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

                            } else if (status == 1) {

                                toastClass.makeToast(getApplicationContext(), "Please check you email ");


                                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else if (status == 2) {
                                toastClass.makeToast(getApplicationContext(), "Your account not activated yet. Please Try after some time.");
                            } else if (status == 3) {
                                toastClass.makeToast(getApplicationContext(), "Sorry, an account not exists with this contact");
                            } else if (status == 4) {
                                toastClass.makeToast(getApplicationContext(), "Invalid Contact");

                            }
                        } catch (Exception e) {

                            Log.v("Register_Login", e.toString());
                        }
                        mBinder.btnSendForgotEmail.setVisibility(View.VISIBLE);
                        mBinder.cvPgBar.setVisibility(View.GONE);

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







}

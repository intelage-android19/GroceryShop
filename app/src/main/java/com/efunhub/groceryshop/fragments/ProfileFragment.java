package com.efunhub.groceryshop.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.databinding.FragmentProfileBinding;
import com.efunhub.groceryshop.interfaces.IResult;
import com.efunhub.groceryshop.interfaces.NoInternetListener;
import com.efunhub.groceryshop.model.ProfileBaseModel;
import com.efunhub.groceryshop.model.ProfileUpdateBaseModel;
import com.efunhub.groceryshop.model.ProfileUpdateModel;
import com.efunhub.groceryshop.model.Profilemodel;
import com.efunhub.groceryshop.utility.CheckConnectivity;
import com.efunhub.groceryshop.utility.SessionManager;
import com.efunhub.groceryshop.utility.ToastClass;
import com.efunhub.groceryshop.utility.VolleyService;
import com.efunhub.groceryshop.validator.InputValidatorHelper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.efunhub.groceryshop.utility.ConstantVariables.UPDATE_PASSWORD;
import static com.efunhub.groceryshop.utility.ConstantVariables.UPDATE_USER_PROFILE;
import static com.efunhub.groceryshop.utility.ConstantVariables.USER_PROFILE;
import static com.efunhub.groceryshop.utility.SessionManager.KEY_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {

    private FragmentProfileBinding mBinder;
    private IResult mResultCallback;
    private VolleyService mVollyService;
    private ToastClass toastClass;
    private ProgressDialog progressDialog;
    private CheckConnectivity checkConnectivity;
    private boolean connectivityStatus = true;
    private String ProfileURL = "Profile-Customer";
    private String UpdateProfileURL = "Update-Profile-Customer";
    private String UpdateProfilePassword = "Update-Password-Customer";
    Dialog dialog, dialogPassword;


    Profilemodel profilemodel;
    ProfileBaseModel profileBaseModel;
    ProfileUpdateModel profileUpdateModel;
    ProfileUpdateBaseModel profileUpdateBaseModel;

    TextView tvProfileName;

    EditText dialogName, dialogContact, dialogEmail, dialogAddress, dialogCity, dialogPincode, dialogArea, dialogCountrycode, dialogOldPassword, dialogNewPassword;

    Button btnUpdatePassword;
    private SessionManager sessionManager;

    private String userId;

    String name, contact, email, pincode, country_code, area, city, address, oldp, newp;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {

        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinder = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        setHasOptionsMenu(true);

        setUpToolbarWithoutTitle(mBinder.toolbar.toolbar, true);

        sessionManager = new SessionManager(getActivity());

        HashMap<String, String> userInfo = sessionManager.getUserDetails();

        userId = userInfo.get(KEY_ID);

        tvProfileName = mBinder.tvProfileFullName;

        loadProfile();


        mBinder.btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogPassword();

            }

        });

        mBinder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog();
            }

        });
        toastClass = new ToastClass();
        progressDialog = ProgressDialog.show(getContext(), "GroKisan",
                "Please wait while loading..", false, false);

        return mBinder.getRoot();

    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_cart);
        if (item != null)
            item.setVisible(false);
    }

    private void loadProfile() {

        initVolleyCallback();

        HashMap<String, String> param = new HashMap<>();

        param.put("customer_auto_id", userId);//"5d2577d462c29"

        mVollyService = new VolleyService(mResultCallback, getContext());
        mVollyService.postDataVolleyParameters(USER_PROFILE,
                this.getResources().getString(R.string.base_url) + ProfileURL, param);

    }

    private void updateProfile() {

        final String fullName = dialogName.getText().toString();
        final String email = dialogEmail.getText().toString();
        final String contact = dialogContact.getText().toString();
        final String address = dialogAddress.getText().toString();


        final String dialogcity = dialogCity.getText().toString();
        final String dialogarea = dialogArea.getText().toString();
        final String dialogpincode = dialogPincode.getText().toString();
        final String dialogcountrycode = dialogPincode.getText().toString();


        initVolleyCallback();

        mVollyService = new VolleyService(mResultCallback, getContext());

        HashMap<String, String> params = new HashMap<>();

        params.put("customer_auto_id", userId);//"5d2577d462c29"
        params.put("name", fullName);
        params.put("email", email);
        params.put("contact", contact);

        params.put("city", dialogcity);
        params.put("area", dialogarea);
        params.put("pincode", dialogpincode);
        params.put("country_code", dialogcountrycode);
        params.put("address", address);


        mVollyService.postDataVolleyParameters(UPDATE_USER_PROFILE,
                this.getResources().getString(R.string.base_url) + UpdateProfileURL, params);
    }

    private void updatePassword() {

        oldp = dialogOldPassword.getText().toString();
        newp = dialogNewPassword.getText().toString();


        initVolleyCallback();

        mVollyService = new VolleyService(mResultCallback, getContext());

        HashMap<String, String> params = new HashMap<>();


        params.put("customer_auto_id", userId);//"5d2577d462c29"
        params.put("oldp", oldp);
        params.put("newp", newp);


        mVollyService.postDataVolleyParameters(UPDATE_PASSWORD,
                this.getResources().getString(R.string.base_url) + UpdateProfilePassword, params);
    }


    private boolean checkValidation() {

        InputValidatorHelper inputValidatorHelper = new InputValidatorHelper();

        if (inputValidatorHelper.isNullOrEmpty(dialogName.getText().toString())) {
            dialogName.setError("Please enter full name");
            return false;

        } else if (inputValidatorHelper.isNullOrEmpty(dialogContact.getText().toString())) {
            dialogContact.setError("Please enter mobile number");
            return false;

        } else if (!inputValidatorHelper.isValidMobile(dialogContact.getText().toString())) {
            dialogContact.setError("Please enter valid mobile number");
            return false;

        } else if (!inputValidatorHelper.isValidMobileNoLength(dialogContact.getText().toString())) {
            dialogContact.setError("Please enter valid mobile number");
            return false;

        } else if (inputValidatorHelper.isNullOrEmpty(dialogEmail.getText().toString())) {
            dialogEmail.setError("Please enter email id");
            return false;

        } else if (!inputValidatorHelper.isValidEmail(dialogEmail.getText().toString())) {
            dialogEmail.setError("Please enter valid email id");
            return false;

        }
        return true;
    }

    private boolean checkValidationPassword() {

        InputValidatorHelper inputValidatorHelper = new InputValidatorHelper();

        String passwordRegex = "^[a-zA-Z0-9]{6,16}$";

        if (dialogOldPassword.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Please enter old password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dialogOldPassword.getText().toString().equals(passwordRegex)) {
            Toast.makeText(getContext(), "Please enter valid old password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dialogNewPassword.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Please enter new password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dialogNewPassword.getText().toString().equals(passwordRegex)) {
            Toast.makeText(getContext(), "Please enter valid new password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dialogOldPassword.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Please enter old password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dialogOldPassword.getText().toString().equals(passwordRegex)) {
            Toast.makeText(getContext(), "Please enter valid old password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void initVolleyCallback() {
        try {


            mResultCallback = new IResult() {
                @Override
                public void notifySuccess(int requestId, String response) {

                    switch (requestId) {

                        case USER_PROFILE:
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                int status = jsonObject.getInt("status");

                                if (status == 1) {

                                    //  progressDialog.cancel();

                                    JSONObject jsonObject1 = jsonObject.getJSONObject("profile");

                                    name = jsonObject1.getString("name");
                                    contact = jsonObject1.getString("contact");
                                    email = jsonObject1.getString("email");

                                    city = jsonObject1.getString("city");
                                    area = jsonObject1.getString("area");
                                    address = jsonObject1.getString("address");
                                    pincode = jsonObject1.getString("pincode");
                                    country_code = jsonObject1.getString("country_code");

                                    mBinder.tvProfileFullName.setText(name);
                                    mBinder.tvPrfileMobileNumber.setText(contact);
                                    mBinder.tvProfileEmail.setText(email);
                                    mBinder.tvProfileAddress.setText(address);
                                    mBinder.tvArea.setText(area);
                                    mBinder.tvPincode.setText(pincode);
                                    mBinder.tvCity.setText(city);
                                    mBinder.tvProfileCountryCode.setText(country_code);


                                } else {
                                    progressDialog.cancel();
                                    toastClass.makeToast(getContext(), "Sorry an account does not exist");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progressDialog.dismiss();
                            break;

                        case UPDATE_USER_PROFILE:

                            try {


                                JSONObject jsonObject = new JSONObject(response);

                                int status = jsonObject.getInt("status");
                                if (status == 0) {
                                    progressDialog.cancel();
                                    toastClass.makeToast(getContext(), "Please try again.");

                                } else if (status == 1) {
                                    progressDialog.cancel();
                                    toastClass.makeToast(getContext(), " Updated Sucessfully");
                                    loadProfile();

                                } else if (status == 2) {
                                    progressDialog.cancel();
                                    toastClass.makeToast(getContext(), "Enter Valid Contact");
                                } else if (status == 3) {
                                    progressDialog.cancel();
                                    toastClass.makeToast(getContext(), "Check Email Format");
                                }

                            } catch (JSONException e) {
                                progressDialog.cancel();
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();

                            break;

                        case UPDATE_PASSWORD:

                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                int status = jsonObject.getInt("status");
                                if (status == 0) {
                                    progressDialog.cancel();
                                    toastClass.makeToast(getContext(), "Your old password does not match");

                                } else if (status == 1) {
                                    progressDialog.cancel();
                                    toastClass.makeToast(getContext(), " Password has been updated sucessfully");

                                    dialogPassword.dismiss();
                                } else if (status == 2) {
                                    progressDialog.cancel();
                                    toastClass.makeToast(getContext(), "Password must be atleast 6 characters");
                                } else if (status == 3) {
                                    progressDialog.cancel();
                                    toastClass.makeToast(getContext(), "Password must be atleast 6 characters");
                                }else if (status == 4) {
                                    progressDialog.cancel();
                                    toastClass.makeToast(getContext(), "Sorry, an account not exist");
                                }

                            } catch (JSONException e) {
                                progressDialog.cancel();
                                e.printStackTrace();
                            }
                    }

                }

                @Override
                public void notifyError(int requestId, VolleyError error) {
                    Log.v("Volley requestid ", String.valueOf(requestId));
                    Log.v("Volley Error", String.valueOf(error));
                }
            };
        } catch (Exception ex) {

            Log.d("ProfileActivity", "initVolleyCallback: " + ex);
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        //Check connectivity
        checkConnectivity = new CheckConnectivity(getContext(), new NoInternetListener() {
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
        getContext().registerReceiver(checkConnectivity, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        /*UnRegister receiver for connectivity*/
        getContext().unregisterReceiver(checkConnectivity);
    }


    void dialog() {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.update_profile);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
        dialog.setCanceledOnTouchOutside(true);

        ImageView ivCancle = dialog.findViewById(R.id.ivClose);
        Button btnUpdateProfile = dialog.findViewById(R.id.btnUpdateProfile);

        dialogName = dialog.findViewById(R.id.cetName);
        dialogContact = dialog.findViewById(R.id.cetContact);
        dialogEmail = dialog.findViewById(R.id.cetEmail);
        dialogAddress = dialog.findViewById(R.id.cetAddress);

        dialogCity = dialog.findViewById(R.id.tvCity);
        dialogArea = dialog.findViewById(R.id.tvArea);
        dialogPincode = dialog.findViewById(R.id.tvPincode);
        dialogCountrycode = dialog.findViewById(R.id.cetCountryCode);

        dialogName.setText(name);
        dialogContact.setText(contact);
        dialogEmail.setText(email);
        dialogAddress.setText(address);
        dialogArea.setText(area);
        dialogCity.setText(city);
        dialogPincode.setText(pincode);
        dialogCountrycode.setText(country_code);

        dialog.show();

        ivCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkValidation()) {

                    updateProfile();

                    dialog.cancel();

                }

            }
        });


    }

    void dialogPassword() {

        dialogPassword = new Dialog(getContext());
        dialogPassword.setContentView(R.layout.update_password);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
        dialogPassword.setCanceledOnTouchOutside(true);

        ImageView ivCancle = dialogPassword.findViewById(R.id.ivClose);

        dialogOldPassword = dialogPassword.findViewById(R.id.edtOldPassword);
        dialogNewPassword = dialogPassword.findViewById(R.id.edtNewPassword);
        btnUpdatePassword = dialogPassword.findViewById(R.id.btnUpdatePassword);




        dialogPassword.show();

        ivCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPassword.cancel();
            }
        });

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkValidationPassword()) {

                    updatePassword();

                }

            }
        });


    }


}


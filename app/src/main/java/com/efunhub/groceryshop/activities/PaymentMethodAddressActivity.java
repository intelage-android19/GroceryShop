package com.efunhub.groceryshop.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.VolleyError;
import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.databinding.PaymentMethodAddressActivityBinding;
import com.efunhub.groceryshop.fragments.OrderErrorFragment;
import com.efunhub.groceryshop.fragments.OrderSuccessFragment;
import com.efunhub.groceryshop.interfaces.IResult;
import com.efunhub.groceryshop.model.CartRVModel;
import com.efunhub.groceryshop.utility.CheckConnectivity;
import com.efunhub.groceryshop.utility.SessionManager;
import com.efunhub.groceryshop.utility.ToastClass;
import com.efunhub.groceryshop.utility.VolleyService;
import com.google.android.material.snackbar.Snackbar;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static com.efunhub.groceryshop.utility.ConstantVariables.GET_DELIVERY_AREA_POSTALCODE;
import static com.efunhub.groceryshop.utility.ConstantVariables.MAKE_ORDER_COD;
import static com.efunhub.groceryshop.utility.ConstantVariables.PAY_MONEY_DETAILS;
import static com.efunhub.groceryshop.utility.ConstantVariables.USER_PROFILE;
import static com.efunhub.groceryshop.utility.SessionManager.KEY_POSTAL_CODE;

/**
 * Created by Admin on 24-01-2019.
 */

public class PaymentMethodAddressActivity extends BaseActivity implements View.OnClickListener {


    private Toolbar mToolbar;
    private FrameLayout flPayment;
    private LinearLayout llPaymentLayout;
    private SessionManager sessionManager;
    private EditText edtPayemntAddress, edtPayemntPincode, edtArea;
    private RadioGroup radioGroupPayment;
    private RadioButton radioCOD, radioCard, radioWallet;
    private Spinner spnTimeSlots;
    private Spinner spnArea;
    private Button btnFinalPayment;
    private ToastClass toastClass;
    private ArrayList<CartRVModel> cartRVModelArrayList;
    private ArrayList<String> areaArrayList = new ArrayList<>();
    private List<String> timeSlotsArrayList = new ArrayList<>();


    private float payamount;
    //private float cutwalletamount;
    //private float deliveryCharges;

    private VolleyService mVolleyService;
    private IResult mResultCallBack = null;
    private String MakeOrderUrl = "make_order.php";
    private String profileUrl = "profile_customer.php";
    private String DeliveryAreaPosatlCodeUrl = "pincode.php";
    private CheckConnectivity checkConnectivity;
    private Calendar c;
    private SimpleDateFormat df;
    private String formattedDate;

    private static int PAYMENT_COD = 1;
    private static int PAYMENT_CARD = 0;
    private static int PAYMENT_WALLET = 2;

    private String customerId, ProductId, ProductName, ProductEName, ProductMName, ProductQty, productPrice, ProductWeight, deliveryCharges;
    private String Area, Address, Pincode, wallet, cutwalletAmount, payPrice, PaidPrice, totalAmount;
    private String SGSTPrice, CGSTPrice, SGSTPercentage, CGSTPercentage, productWQty, productStatus;


    private String userName, userEmail, usercontact;

    private String customerName, customerMobile, customerEmail, userLanguage;

    private ProgressBar progressBar;
    private RelativeLayout mainPgBarLayout;

    // Final Variables
    String mMerchantKey = "oXMTlk7P";//4s4Md2rQ
    String mSalt = "OhcE3I5YMq";//Xt6dMXh2HR
    String mBaseURL = "https://secure.payu.in";

    private String mAction = ""; // For Final URL
    private String mTXNId; // This will create below randomly
    private String mHash; // This will create below randomly

    private String mServiceProvider = "payu_paisa";
    private String mSuccessUrl = "https://www.payumoney.com/mobileapp/payumoney/success.php";
    private String mFailedUrl = "https://www.payumoney.com/mobileapp/payumoney/failure.php";

    private String PayMoneyDetailsURL = "update_personal.php";
    private String DeliveryAreaUrl = "area.php";

    public static final String inputFormat = "HH:mm a";

    private Date date;
    private Date dateCompareOne;
    private Date dateCompareTwo;
    private String compareStringOne = "9:00 AM";
    private String compareStringTwo = "9:00 PM";
    ArrayAdapter<String> adapter;

    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.ENGLISH);

    private RelativeLayout paymentLayout;
    private boolean connectivityStatus = true;
    private Snackbar snackbar;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 0;
    public static int TYPE_NOT_CONNECTED = 2;

    private LinearLayout noInternetConn;
    private TextView tvRetry;
    private PaymentMethodAddressActivityBinding mBinder;

    private String userPincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinder = DataBindingUtil.setContentView(this, R.layout.payment_method_address_activity);

        init();

        setUpToolbarWithBackArrow(mBinder.toolbar.toolbar, "Payment details", false, this);
        // setUpToolbar();

        if (connectivityStatus) {
            //get postal code to deliver the order
            getPostalCOde();
        }


        /*if (payamount == 0f) {
            radioWallet.setVisibility(View.GONE);
            radioWallet.setEnabled(true);
            radioWallet.setChecked(true);
            radioCard.setEnabled(false);
            radioCOD.setEnabled(false);
            radioCOD.setChecked(false);
        }*/


        HashMap<String, String> hashMapLang = sessionManager.getUserPostal();
        userPincode = hashMapLang.get(KEY_POSTAL_CODE);

        //edtArea.setOnClickListener(this);

        if (connectivityStatus) {
            btnFinalPayment.setOnClickListener(this);
        } else {
            toastClass.makeToast(getApplicationContext(), "Please check internet connection");
        }

    }

    public void init() {

        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorTextBlack));


        c = Calendar.getInstance();
        toastClass = new ToastClass();
        sessionManager = new SessionManager(this);
        mToolbar = mBinder.toolbar.toolbar;
        radioGroupPayment = mBinder.radioPayment;
        radioWallet = mBinder.radioWallet;
        radioCOD = mBinder.radioCOD;
        radioCard = mBinder.radioCard;
        edtPayemntAddress = mBinder.edtPayemntAddress;
        //edtPayemntPincode = findViewById(R.id.edtPayemntPincode);
        //edtArea = findViewById(R.id.edtArea);
        //spnArea = findViewById(R.id.spnArea);
        btnFinalPayment = mBinder.btnFinalPayment;
        llPaymentLayout = mBinder.linearLayoutMain;
        flPayment = mBinder.flPayment;
        paymentLayout = mBinder.relativelayout;

        mainPgBarLayout = mBinder.mainPgBarLayout;

        /*spnTimeSlots = mBinder.spnTimeSlots;
        timeSlotsArrayList = Arrays.asList(this.getResources().getStringArray(R.array.time_slots));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                PaymentMethodAddressActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                timeSlotsArrayList
        );

        spnTimeSlots.setAdapter(adapter);*/

        //Intent intent = getIntent();

       /* if(intent.hasExtra("BUNDLE")){
            Bundle args = intent.getBundleExtra("BUNDLE");
            ArrayList<CartRVModel> arraylist = (ArrayList<CartRVModel>) args.getSerializable("ARRAYLIST");
        }*/


        if (getIntent().hasExtra("productId")) {
            ProductId = getIntent().getStringExtra("productId");
        }

        if (getIntent().hasExtra("pStatus")) {
            productStatus = getIntent().getStringExtra("pStatus");
        }

        if (getIntent().hasExtra("productName")) {
            ProductName = getIntent().getStringExtra("productName");
        }


        if (getIntent().hasExtra("productQty")) {
            ProductQty = getIntent().getStringExtra("productQty");
        }

        if (getIntent().hasExtra("productWeight")) {
            ProductWeight = getIntent().getStringExtra("productWeight");
        }

        if (getIntent().hasExtra("area")) {
            Area = getIntent().getStringExtra("area");

        }

        if (getIntent().hasExtra("address")) {
            Address = getIntent().getStringExtra("address");
            edtPayemntAddress.setText(Address);
        }

        if (getIntent().hasExtra("pinCode")) {
            Pincode = getIntent().getStringExtra("pinCode");
            edtPayemntPincode.setText(Pincode);
        }

        if (getIntent().hasExtra("walletBalance")) {
            wallet = getIntent().getStringExtra("walletBalance");
        }

        if (getIntent().hasExtra("payPrice")) {
            payPrice = getIntent().getStringExtra("payPrice");
            payamount = Float.parseFloat(payPrice);
        }

        if (getIntent().hasExtra("paidPrice")) {
            PaidPrice = getIntent().getStringExtra("paidPrice");
        }

        if (getIntent().hasExtra("SGSTPrice")) {
            SGSTPrice = getIntent().getStringExtra("SGSTPrice");
        }

        if (getIntent().hasExtra("CGSTPrice")) {
            CGSTPrice = getIntent().getStringExtra("CGSTPrice");
        }

        if (getIntent().hasExtra("SGSTPER")) {
            SGSTPercentage = getIntent().getStringExtra("SGSTPER");
        }

        if (getIntent().hasExtra("CGSTPER")) {
            CGSTPercentage = getIntent().getStringExtra("CGSTPER");
        }

        if (getIntent().hasExtra("productPrice")) {
            productPrice = getIntent().getStringExtra("productPrice");
        }

        if (getIntent().hasExtra("cutbalance")) {
            cutwalletAmount = getIntent().getStringExtra("cutbalance");
        }

        if (getIntent().hasExtra("deliveryCharges")) {
            deliveryCharges = getIntent().getStringExtra("deliveryCharges");
        }

        if (getIntent().hasExtra("totalAmount")) {
            totalAmount = getIntent().getStringExtra("totalAmount");
        }

        if (getIntent().hasExtra("cutomerId")) {
            customerId = getIntent().getStringExtra("cutomerId");
        }

        if (getIntent().hasExtra("productEName")) {
            ProductEName = getIntent().getStringExtra("productEName");
        }

        if (getIntent().hasExtra("productMName")) {
            ProductMName = getIntent().getStringExtra("productMName");
        }

        if (getIntent().hasExtra("productWQty")) {
            productWQty = getIntent().getStringExtra("productWQty");
        }

    }

    private Date parseDate(String date) {

        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnFinalPayment:
                if (connectivityStatus) {
                    if (checkValid()) {
                        if (radioGroupPayment.getCheckedRadioButtonId() == R.id.radioCOD) {
                            // make payment by cash on delivery
                            mainPgBarLayout.setVisibility(View.VISIBLE);
                            btnFinalPayment.setVisibility(View.GONE);
                            paymentMode(PAYMENT_COD);
                        } else if (radioGroupPayment.getCheckedRadioButtonId() == R.id.radioCard) {
                            // make payment by card
                            //paymentMode(PAYMENT_CARD);
                        } /*else {
                        paymentMode(PAYMENT_WALLET);
                    }*/
                    }
                } else {
                    toastClass.makeToast(getApplicationContext(), "Please check internet connection.");
                }

                break;
        }
    }

    private void paymentMode(int paymentMethod) {

        mainPgBarLayout.setVisibility(View.VISIBLE);

        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        formattedDate = df.format(c.getTime());

        if (paymentMethod == PAYMENT_COD) {

            initVolleyCallback();

            mVolleyService = new VolleyService(mResultCallBack, this);
            Map<String, String> params = new HashMap<>();
            params.put("cust_id", customerId);
            //params.put("lvid", orderId);
            params.put("product_id", ProductId);
            params.put("product_ename", ProductEName);
            params.put("product_mname", ProductMName);
            params.put("product_price", productPrice);
            params.put("qty", ProductQty);
            params.put("product_wqty", productWQty);
            params.put("product_weight", ProductWeight);
            params.put("wallet", wallet);
            params.put("cgst", CGSTPercentage);
            params.put("sgst", SGSTPercentage);
            params.put("cgstprice", CGSTPrice);
            params.put("sgstprice", SGSTPrice);
            params.put("delivery_charge", deliveryCharges);
            params.put("total_price", totalAmount);
            params.put("cutbalance", cutwalletAmount);
            params.put("payprice", payPrice);
            params.put("paidprice", PaidPrice);
            params.put("pmode", "CASH");
            params.put("address", edtPayemntAddress.getText().toString());
            params.put("area", mBinder.edtArea.getText().toString());
            params.put("pincode",mBinder.spnPincode.getSelectedItem().toString());
            params.put("cstatus", productStatus);
            params.put("timing", "");
            //params.put("dtime", "");
            params.put("tdate", formattedDate);

            mVolleyService.postDataVolleyParameters(MAKE_ORDER_COD, this.getResources().getString(R.string.base_url) + MakeOrderUrl, params);

        } else if (paymentMethod == PAYMENT_CARD) {
            makePayment();

        }/* else {
            initVolleyCallback();

            mVolleyService = new VolleyService(mResultCallBack, this);

            Map<String, String> params = new HashMap<>();

            params.put("uid", uid);
            params.put("payamount", String.valueOf(payamount));
            params.put("address", edtPayemntAddress.getText().toString());
            params.put("pincode", edtPayemntPincode.getText().toString());
            params.put("area", spnArea.getSelectedItem().toString());
            params.put("productlist", productlist);
            params.put("odate", formattedDate);
            params.put("cutbalance", String.valueOf(cutwalletamount));
            params.put("pmode", "Wallet");
            params.put("dcharge", String.valueOf(deliveryCharges));

            mVolleyService.postDataVolleyParameters(MAKE_ORDER_WALLET, this.getResources().getString(R.string.base_url) + MakeOrderUrl, params);
        }*/
    }


    private void makePayment() {
        /**
         * Creating Transaction Id
         */
        Random rand = new Random();
        String randomString = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
        mTXNId = hashCal("SHA-256", randomString).substring(0, 20);

        //payamount = new BigDecimal(payamount).setScale(0, RoundingMode.UP).intValue();

        double roundOffAmount = Math.round(payamount * 100.0) / 100.0;

        String productName = "OrganicVegetable";

        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        /**
         * Creating Hash Key
         */
        mHash = hashCal("SHA-512",
                mMerchantKey + "|" +
                        mTXNId + "|" +
                        roundOffAmount + "|" +
                        productName + "|" +
                        userName + "|" +
                        userEmail + "|" +
                        udf1 + "|" +
                        udf2 + "|" +
                        udf3 + "|" +
                        udf4 + "|" +
                        udf5 + "|" +
                        udf6 + "|" +
                        udf7 + "|" +
                        udf8 + "|" +
                        udf9 + "|" +
                        udf10 + "|" +
                        mSalt);

        /**
         * Final Action URL...
         */
        mAction = mBaseURL.concat("/_payment");

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new
                PayUmoneySdkInitializer.PaymentParam.Builder();
        builder.setAmount(roundOffAmount) // Payment amount
                .setTxnId(mTXNId) // Transaction ID
                .setPhone(usercontact) // User Phone number
                .setProductName(productName) // Product Name or description
                .setFirstName(userName) // User First name
                .setEmail(userEmail) // User Email ID
                .setsUrl(mSuccessUrl) // Success URL (surl)
                .setfUrl(mFailedUrl) //Failure URL (furl)
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(false) // Integration environment - true (Debug)/ false(Production)
                .setKey(mMerchantKey) // Merchant key
                .setMerchantId("6616461"); //6616461//(Main ---> 6463187)

        //declare paymentParam object
        PayUmoneySdkInitializer.PaymentParam paymentParam = null;
        try {
            paymentParam = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //set the hash
        paymentParam.setMerchantHash(mHash);


        // Invoke the following function to open the checkout page.
        PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, PaymentMethodAddressActivity.this, R.style.PaymentAppTheme, true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction

                    parsePayuMoneyResponse(transactionResponse.getPayuResponse());
                } else {
                    //Failure Transaction

                    parsePayuMoneyResponse(transactionResponse.getPayuResponse());
                    Log.v("payuResponse", transactionResponse.getPayuResponse());
                }

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("ERROR", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("NO RESULT", "Both objects are null!");
            }
        }
    }


    /**
     * Hash Key Calculation
     *
     * @param type
     * @param str
     * @return
     */
    public String hashCal(String type, String str) {
        byte[] hashSequence = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashSequence);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1)
                    hexString.append("0");
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException NSAE) {
        }
        return hexString.toString();
    }

    private void parsePayuMoneyResponse(String payuResponse) {
        //converting response to json object
        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(payuResponse);
            JSONObject jsonObjectResult = jsonObj.getJSONObject("result");

            String status = jsonObjectResult.getString("status");
            String paymentId = jsonObjectResult.getString("paymentId");
            //Log.v("status", status);

            if (status.equals("success")) {
                //sendDataToServer(paymentId);
            }
        } catch (Exception e) {
            Log.v("Sending Data to server", e.toString());
        }
    }

    private void sendDataToServer(String paymentId) {
        initVolleyCallback();

        mainPgBarLayout.setVisibility(View.VISIBLE);

        mVolleyService = new VolleyService(mResultCallBack, this);

        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedDate = df.format(c.getTime());

        c.add(Calendar.DATE, 1);  // number of days to add
        String nextdate = df.format(c.getTime());

        mVolleyService = new VolleyService(mResultCallBack, this);
        Map<String, String> params = new HashMap<>();
        params.put("cust_id", customerId);
        //params.put("lvid", orderId);
        params.put("product_id", ProductId);
        params.put("product_ename", ProductEName);
        params.put("product_mname", ProductMName);
        params.put("product_price", productPrice);
        params.put("qty", ProductQty);
        params.put("product_wqty", ProductWeight);
        params.put("product_weight", ProductWeight);
        params.put("wallet", wallet);
        params.put("cgst", CGSTPercentage);
        params.put("sgst", SGSTPercentage);
        params.put("cgstprice", CGSTPrice);
        params.put("sgstprice", SGSTPrice);
        params.put("delivery_charge", deliveryCharges);
        params.put("total_price", totalAmount);
        params.put("cutbalance", cutwalletAmount);
        params.put("payprice", payPrice);
        params.put("paidprice", PaidPrice);
        params.put("pmode", "CASH");
        params.put("address", Address);
        params.put("area", Area);
        params.put("pincode", Pincode);
        params.put("tid", paymentId);
        params.put("tdate", formattedDate);

        mVolleyService.postDataVolleyParameters(PAY_MONEY_DETAILS, getApplicationContext().getResources().getString(R.string.base_url) + MakeOrderUrl, params);
    }

    private void getPostalCOde() {

        mainPgBarLayout.setVisibility(View.VISIBLE);

        btnFinalPayment.setVisibility(View.GONE);

        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallBack, this);

        mVolleyService.postDataVolley(GET_DELIVERY_AREA_POSTALCODE, this.getResources().getString(R.string.base_url) + DeliveryAreaPosatlCodeUrl);
    }

    private void getProfile() {

        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallBack, this);

        Map<String, String> params = new HashMap<>();

        params.put("cust_id", customerId);

        mVolleyService.postDataVolleyParameters(USER_PROFILE, this.getResources().getString(R.string.base_url) + profileUrl, params);
    }

    private void initVolleyCallback() {
        mResultCallBack = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {

                switch (requestId) {

                    case MAKE_ORDER_COD:
                        try {

                            mainPgBarLayout.setVisibility(View.GONE);

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 1) {

                                //clear all products from cart
                                sessionManager.deleteCartList();

                                llPaymentLayout.setVisibility(View.GONE);
                                flPayment.setVisibility(View.VISIBLE);
                                Fragment fragment = new OrderSuccessFragment();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                                fragmentTransaction.add(R.id.flPayment, fragment);
                                fragmentTransaction.commit();

                            } else {
                                llPaymentLayout.setVisibility(View.GONE);
                                flPayment.setVisibility(View.VISIBLE);
                                Fragment fragment = new OrderErrorFragment();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                                fragmentTransaction.add(R.id.flPayment, fragment);
                                fragmentTransaction.commit();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            mainPgBarLayout.setVisibility(View.GONE);
                            btnFinalPayment.setVisibility(View.VISIBLE);
                        }
                        break;

                  /* case PAY_MONEY_DETAILS: //make payment by card

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            mainPgBarLayout.setVisibility(View.GONE);
                            if (Integer.parseInt(status) == 1) {
                                sessionManager.deleteCartList();
                                llPaymentLayout.setVisibility(View.GONE);
                                flPayment.setVisibility(View.VISIBLE);
                                Fragment fragment = new OrderSuccessFragment();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                                fragmentTransaction.add(R.id.flPayment, fragment);
                                fragmentTransaction.commit();
                            } else {
                                llPaymentLayout.setVisibility(View.GONE);
                                flPayment.setVisibility(View.VISIBLE);
                                Fragment fragment = new OrderErrorFragment();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                                fragmentTransaction.add(R.id.flPayment, fragment);
                                fragmentTransaction.commit();}

                        } catch (JSONException e) {
                            e.printStackTrace();
                            mainPgBarLayout.setVisibility(View.GONE);
                            btnFinalPayment.setVisibility(View.VISIBLE);
                        }
                        break;

                       case USER_PROFILE:

                           try {
                               JSONObject jsonObject = new JSONObject(response);
                               String status = jsonObject.getString("status");

                               if (Integer.parseInt(status) == 1) {

                                   JSONArray jsonArray = jsonObject.getJSONArray("allprofile");

                                   for (int i = 0; i < jsonArray.length(); i++) {

                                       JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                       userName = jsonObject1.getString("name");
                                       userEmail =jsonObject1.getString("email");
                                       usercontact=jsonObject1.getString("contact");

                                       edtPayemntAddress.setText(jsonObject1.getString("address"));
                                       edtPayemntPincode.setText(jsonObject1.getString("pincode"));
                                       int deafultAreaPostion = adapter.getPosition(Area);
                                       spnArea.setSelection(deafultAreaPostion);
                                   }
                                   mainPgBarLayout.setVisibility(View.GONE);
                                   btnFinalPayment.setVisibility(View.VISIBLE);
                               }
                           } catch (JSONException e) {
                               e.printStackTrace();
                               mainPgBarLayout.setVisibility(View.GONE);
                               btnFinalPayment.setVisibility(View.GONE);
                           }
                        break;*/


                    case GET_DELIVERY_AREA_POSTALCODE:

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 1) {

                                areaArrayList.clear();

                                JSONArray jsonArray = jsonObject.getJSONArray("allpincode");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String area = jsonObject1.getString("name");
                                    areaArrayList.add(area);
                                }

                                adapter = new ArrayAdapter<String>(
                                        PaymentMethodAddressActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        areaArrayList
                                );

                                mBinder.spnPincode.setAdapter(adapter);

                                int deafultAreaPostion = adapter.getPosition(Area);
                                mBinder.spnPincode.setSelection(deafultAreaPostion);
                                mainPgBarLayout.setVisibility(View.GONE);
                                btnFinalPayment.setVisibility(View.VISIBLE);
                                //getProfile();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            btnFinalPayment.setVisibility(View.GONE);
                            //toastClass.makeToast(getApplicationContext(),"Please try after some time.");
                        }

                        break;
                }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                Log.v("Volley requestid ", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
            }
        };
    }


    public void showAreaDialog() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(PaymentMethodAddressActivity.this);
        builderSingle.setTitle("Select Area");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PaymentMethodAddressActivity.this,
                android.R.layout.simple_list_item_1, areaArrayList);

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                edtArea.setText(strName);
                dialog.dismiss();
            }
        });
        builderSingle.show();

    }

    public boolean checkValid() {

        if (edtPayemntAddress.getText().toString().equalsIgnoreCase("")) {
            toastClass.makeToast(this, "Please enter address");
            return false;
        } else if (mBinder.edtArea.getText().toString().equalsIgnoreCase("")) {
            toastClass.makeToast(this, "Please enter area");
            return false;
        }  else if (mBinder.spnPincode.getSelectedItem() == null) {
            toastClass.makeToast(this, "Please select delivery area");
            return false;
        } /*else if (spnTimeSlots.getSelectedItem() == null) {
            toastClass.makeToast(this, "Please select time");
            return false;
        }*/ else if (!userPincode.equalsIgnoreCase(mBinder.spnPincode.getSelectedItem().toString())) {
            toastClass.makeToast(this, "Please select correct pincode");
            return false;
        }
        return true;

    }

    @Override
    public void onResume() {
        super.onResume();
        registerInternetCheckReceiver();

    }

    @Override
    public void onPause() {
        super.onPause();
        /*UnRegister receiver for connectivity*/
        this.unregisterReceiver(broadcastReceiver);
    }

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
        registerReceiver(broadcastReceiver, internetFilter);
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

    private void setSnackbarMessage(String status, boolean showBar) {

        String internetStatus = "";

        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = "Internet Connected";
        } else if (status.equalsIgnoreCase("Not connected to Internet")) {
            internetStatus = "Please check internet connection";
        }
        snackbar = Snackbar
                .make(paymentLayout, internetStatus, Snackbar.LENGTH_LONG)
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
                snackbar.show();
                sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                connectivityStatus = false;
            }
        } else {
            if (!connectivityStatus) {
                connectivityStatus = true;
                sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_green));
                snackbar.show();
            }
        }
    }
}

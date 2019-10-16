package com.efunhub.groceryshop.activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.adapters.WalletAdapter;
import com.efunhub.groceryshop.databinding.ActivityWalletBinding;
import com.efunhub.groceryshop.fragments.CreditedFragment;
import com.efunhub.groceryshop.fragments.DebitedHistoryFragment;
import com.efunhub.groceryshop.interfaces.IResult;
import com.efunhub.groceryshop.model.CreditedTransactionHistoryModel;
import com.efunhub.groceryshop.model.DebitedTransactionHistoryModel;
import com.efunhub.groceryshop.model.TransactionHistoryBaseModel;
import com.efunhub.groceryshop.model.WalletModel;
import com.efunhub.groceryshop.utility.SessionManager;
import com.efunhub.groceryshop.utility.ToastClass;
import com.efunhub.groceryshop.utility.VolleyService;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.efunhub.groceryshop.utility.ConstantVariables.WALLET;
import static com.efunhub.groceryshop.utility.ConstantVariables.WALLET_HISTORY;
import static com.efunhub.groceryshop.utility.ConstantVariables.WALLET_TRANSFER_AMOUNT;
import static com.efunhub.groceryshop.utility.SessionManager.KEY_ID;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityWalletBinding mBinder;
    TextView title;

    //Volley service
    private IResult mResultCallback;
    private VolleyService mVolleyService;


    //all urls
    private String walletUrl = "Wallet";
    private String walletHistoryUrl = "Show-Wallet-Tranzaction-History";
    private String walletTransferAmount = "Transfer-Amount";

    private SessionManager sessionManager;
    private ToastClass toastClass;
    private String cid;
    private String wallet_amount;

    WalletAdapter walletAdapter;
    private List<WalletModel> walletlist = new ArrayList<>();

    Dialog dalogTransaction;

    EditText edtContact, edtAmount;
    Button btnSend;

    TabLayout tbHistoryTransaction;
    ViewPager vpTransactionHistory;

    CreditedTransactionHistoryModel creditedTransactionHistoryModel ;

    private List<CreditedTransactionHistoryModel> creditedTransactionHistoryModelsList = new ArrayList<>();

    DebitedTransactionHistoryModel debitedTransactionHistoryModel ;

    private List<DebitedTransactionHistoryModel> debitedTransactionHistoryModelList = new ArrayList<>();

    TransactionHistoryBaseModel transactionHistoryBaseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_wallet);

        setUpToolbarWithBackArrow(mBinder.toolbar.toolbar, "My Wallet", false, this);

        sessionManager = new SessionManager(this);
        toastClass = new ToastClass();
        mBinder.btnTransferAmount.setOnClickListener(this);

        getWalletBalance();
        getWalleHistory();


    }

    private void getWalletBalance() {

        initVolleyCallback();


        HashMap<String, String> userInfo = sessionManager.getUserDetails();

        cid = userInfo.get(KEY_ID);


        HashMap<String, String> param = new HashMap<>();

        param.put("customer_auto_id", cid);//"5d2577d462c29"

        mVolleyService = new VolleyService(mResultCallback, this);
        mVolleyService.postDataVolleyParameters(WALLET,
                this.getResources().getString(R.string.base_url) + walletUrl, param);

    }

    private void getWalleHistory() {

        initVolleyCallback();


        HashMap<String, String> userInfo = sessionManager.getUserDetails();

        cid = userInfo.get(KEY_ID);


        HashMap<String, String> param = new HashMap<>();

        param.put("customer_auto_id", cid);//"5d2577d462c29"

        mVolleyService = new VolleyService(mResultCallback, this);
        mVolleyService.postDataVolleyParameters(WALLET_HISTORY,
                this.getResources().getString(R.string.base_url) + walletHistoryUrl, param);

    }

    private void transferAmount() {

        initVolleyCallback();


        HashMap<String, String> userInfo = sessionManager.getUserDetails();

        cid = userInfo.get(KEY_ID);


        HashMap<String, String> param = new HashMap<>();

        param.put("customer_auto_id", cid);//"5d2577d462c29"
        param.put("amount", edtAmount.getText().toString());//"5d2577d462c29"
        param.put("contact", edtContact.getText().toString());//"5d2577d462c29"

        mVolleyService = new VolleyService(mResultCallback, this);
        mVolleyService.postDataVolleyParameters(WALLET_TRANSFER_AMOUNT,
                this.getResources().getString(R.string.base_url) + walletTransferAmount, param);

    }


    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {
                JSONObject jsonObj = null;

                switch (requestId) {

                    case WALLET:
                        try {
                            jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            if (status == 0) {
                                toastClass.makeToast(getApplicationContext(), "Error");

                            } else if (status == 1) {

                                JSONArray jsonArray = jsonObj.getJSONArray("wallet");

                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                wallet_amount = jsonObject.getString("amount");

                                mBinder.tvWalletBalance.setText("₹" + " " + wallet_amount);


                            } else if (status == 2) {
                                toastClass.makeToast(getApplicationContext(), "Your account not activated yet. Please Try after some time.");
                            } else if (status == 3) {
                                toastClass.makeToast(getApplicationContext(), "Sorry, an account not exists with this contact");
                            } else if (status == 4) {
                                toastClass.makeToast(getApplicationContext(), "Invalid Contact");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    case WALLET_HISTORY:
                        try {
                            jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            if (status == 0) {
                                toastClass.makeToast(getApplicationContext(), "Error");

                            } else if (status == 1) {


                                Gson gson = new Gson();

                                transactionHistoryBaseModel = gson.fromJson(
                                        response, TransactionHistoryBaseModel.class);

                                creditedTransactionHistoryModelsList=transactionHistoryBaseModel.getCreditedHistory();
                                debitedTransactionHistoryModelList=transactionHistoryBaseModel.getDebittedHistory();

                                setUpViewPager(mBinder.viewpagerHistory);
                                mBinder.tbTransactionHistory.setupWithViewPager(mBinder.viewpagerHistory);

                            }

                            if (walletlist.isEmpty()) {
                                //   mBinder.pbsoldcarlist.setVisibility(View.GONE);
                              //  mBinder.notransaction.setVisibility(View.VISIBLE);
                            }


                        } catch (Exception e) {

                            Log.v("Wallet Activity", e.toString());
                        }
                        break;


                    case WALLET_TRANSFER_AMOUNT:

                        try {
                            jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            if (status == 0) {
                                toastClass.makeToast(getApplicationContext(), "Not available any data");

                            } else if (status == 1) {
                                toastClass.makeToast(WalletActivity.this, "Amount transferred successfully");
                                dalogTransaction.cancel();
                            }else if (status == 2) {
                                toastClass.makeToast(WalletActivity.this, "Your current balance is low");
                            }else if (status == 3) {
                                toastClass.makeToast(WalletActivity.this, "Sorry, an account not exist with this contact");
                            }else if (status == 4) {
                                toastClass.makeToast(WalletActivity.this, "You are using your own contact");
                            }


                        } catch (Exception e) {
                            Log.v("Wallet Activity", e.toString());
                        }

                        //  mBinder.btnLogin.setVisibility(View.VISIBLE);
                        //   mBinder.cvPgBar.setVisibility(View.GONE);

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



    private void setUpViewPager(ViewPager vpBikeDetails) {


        WalletActivity.ViewPagerAdapter adapter = new WalletActivity.ViewPagerAdapter(getSupportFragmentManager());

        CreditedFragment creditedhstory = CreditedFragment.newInstance(creditedTransactionHistoryModelsList);

        adapter.addFragment(creditedhstory, "CREDITED ");

        DebitedHistoryFragment debitedHistoryFragment = DebitedHistoryFragment.newInstance
                (debitedTransactionHistoryModelList);

        adapter.addFragment(debitedHistoryFragment, "DEBITED ");

        vpBikeDetails.setAdapter(adapter);

    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
/*

            if (position == mFragmentTitleList.size() - 1) {

                Fragment fragment = new UsedCarGalleryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                fragment.setArguments(bundle);
                return fragment;
            } else {
*/
            return mFragmentList.get(position);
            //  }
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }




    private void dialogTransferAmount() {


        dalogTransaction = new Dialog(WalletActivity.this);

        dalogTransaction.setContentView(R.layout.wallet_transaction);
        edtContact = dalogTransaction.findViewById(R.id.edtContact);
        edtAmount = dalogTransaction.findViewById(R.id.edtAmount);
        btnSend = dalogTransaction.findViewById(R.id.btnSend);
        ImageView ivCancle = dalogTransaction.findViewById(R.id.ivClose);



        dalogTransaction.setCanceledOnTouchOutside(true);

        dalogTransaction.show();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    transferAmount();
                }
            }
        });

        ivCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dalogTransaction.cancel();
            }
        });


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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnTransferAmount: {

                dialogTransferAmount();

            }
        }
    }

    private boolean checkValidation() {
        if (edtContact.getText().toString().equalsIgnoreCase("")) {
            edtContact.setError("Please enter contact number");
            return false;
        } else if (edtContact.getText().toString().length()!= 10) {
            edtContact.setError("Please enter valid contact number");
            return false;
        } else if (edtAmount.getText().toString().equalsIgnoreCase("")) {
            edtAmount.setError("Please enter amount");
            return false;
        }
        return true;
    }

}
package com.efunhub.groceryshop.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.activities.LoginActivity;
import com.efunhub.groceryshop.activities.RegistrationActivity;
import com.efunhub.groceryshop.adapters.OrdersRVAdapter;
import com.efunhub.groceryshop.databinding.FragmentOrdersBinding;
import com.efunhub.groceryshop.interfaces.IResult;
import com.efunhub.groceryshop.model.SpecificOrderRVModel;
import com.efunhub.groceryshop.utility.SessionManager;
import com.efunhub.groceryshop.utility.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.efunhub.groceryshop.utility.ConstantVariables.USER_ORDER_HISTORY;
import static com.efunhub.groceryshop.utility.SessionManager.KEY_ID;

/**
 * Created by Admin on 19-02-2018.
 */

public class OrdersFragment extends BaseFragment {//implements View.OnClickListener

    private FragmentOrdersBinding mBinder;


    private View view;
    private RecyclerView rvOrders;
    private OrdersRVAdapter ordersRVAdapter;
    private ArrayList<OrdersRVModel> ordersRVModelArrayList = new ArrayList<>();
    private ArrayList<SpecificOrderRVModel> specificOrderRVModelArrayList;

    private TextView tvOrderhistory, tvOrderLogin, tvOrderSignup;
    private LinearLayout llOrder;
    private ProgressBar progressBar;

    private SessionManager sessionManager;
    private VolleyService mVolleyService;
    private IResult mResultCallBack = null;
    private String OrderHistoryUrl = "customer_order_history.php";

    private String uid;


    public static OrdersFragment newInstance() {

        OrdersFragment ordersFragment = new OrdersFragment();
        return ordersFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinder = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false);

        setUpToolbarWithoutTitle(mBinder.toolbar.toolbar, true);

        //setupToolBarWithMenu(mBinder.toolbar.toolbar, getString(R.string.str_orders));

        sessionManager = new SessionManager(getActivity());

        if (sessionManager.isLoggedIn()) {
            mBinder.llOrder.setVisibility(View.VISIBLE);
            mBinder.rvOrders.setVisibility(View.GONE);
        } else {
            getOrderHistory();
        }

        HashMap<String, String> userInfo = sessionManager.getUserDetails();
        uid = userInfo.get(KEY_ID);

        mBinder.tvOrderLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        mBinder.tvOrderSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });


        return mBinder.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        //  getOrderHistory();

        getActivity().registerReceiver(mReceiverLocation, new IntentFilter("refresh_order_list"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        //register reciever for connectivity
        getContext().unregisterReceiver(mReceiverLocation);
        super.onPause();
    }

    private void getOrderHistory() {
        mBinder.progressBar.setVisibility(View.VISIBLE);

        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallBack, getContext());

        Map<String, String> params = new HashMap<>();
        params.put("cust_id", uid);

        mVolleyService.postDataVolleyParameters(USER_ORDER_HISTORY, this.getResources().getString(R.string.base_url) + OrderHistoryUrl, params);
    }

    private void initVolleyCallback() {
        mResultCallBack = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String productName, productImage, price, dDate, oStatus, qty, productOId, pId;
                    String oid, totalprice, odate;
                    OrdersRVModel ordersRVModel = null;

                    if (Integer.parseInt(status) == 1) {
                        mBinder.progressBar.setVisibility(View.GONE);
                        ordersRVModelArrayList.clear();

                        JSONArray jsonArray = jsonObject.getJSONArray("allorders");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            specificOrderRVModelArrayList = new ArrayList<>();

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            oid = jsonObject1.getString("order_id");
                            odate = jsonObject1.getString("order_date");
                            totalprice = jsonObject1.getString("total_price");

                            /*JSONArray historyJsonArray = jsonObject1.getJSONArray("history");

                            for (int k = 0; k < historyJsonArray.length(); k++) {
                                JSONObject historyJsonObject = historyJsonArray.getJSONObject(k);

                                productOId = historyJsonObject.getString("oid");
                                pId = historyJsonObject.getString("mid");
                                productName = historyJsonObject.getString("name");
                                productImage = historyJsonObject.getString("image");
                                price = historyJsonObject.getString("price");
                                qty = historyJsonObject.getString("quantity");
                                dDate = historyJsonObject.getString("ddate");
                                oStatus = historyJsonObject.getString("status");

                                SpecificOrderRVModel specificOrderRVModel = new SpecificOrderRVModel();
                                specificOrderRVModel.setoId(productOId);
                                specificOrderRVModel.setpId(pId);
                                specificOrderRVModel.setImageUrl(productImage);
                                specificOrderRVModel.setProductName(productName);
                                specificOrderRVModel.setProductPrice(Float.parseFloat(price));
                                specificOrderRVModel.setProductQty(qty);
                                specificOrderRVModel.setDeliveryDate(dDate);
                                specificOrderRVModel.setOrderstatus(oStatus);

                                specificOrderRVModelArrayList.add(specificOrderRVModel);
                            }*/

                            ordersRVModel = new OrdersRVModel();
                            ordersRVModel.setOid(oid);
                            ordersRVModel.setOdate(odate);
                            ordersRVModel.setTotalPrice(Float.parseFloat(totalprice));
                            //ordersRVModel.setSpecificOrderRVModelArrayList(specificOrderRVModelArrayList);

                            ordersRVModelArrayList.add(ordersRVModel);
                        }

                        ordersRVAdapter = new OrdersRVAdapter(getContext(), ordersRVModelArrayList);
                        mBinder.rvOrders.setHasFixedSize(true);
                        mBinder.rvOrders.setNestedScrollingEnabled(false);
                        mBinder.rvOrders.setLayoutManager(new GridLayoutManager(getContext(), 1));
                        mBinder.rvOrders.setItemAnimator(new DefaultItemAnimator());
                        mBinder.rvOrders.setAdapter(ordersRVAdapter);

                        if (ordersRVModelArrayList.size() == 0) {
                            mBinder.rvOrders.setVisibility(View.GONE);
                            mBinder.tvOrderhistory.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mBinder.rvOrders.setVisibility(View.GONE);
                        mBinder.progressBar.setVisibility(View.GONE);
                        mBinder.tvOrderhistory.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mBinder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                mBinder.progressBar.setVisibility(View.GONE);
                Log.v("Volley requestid ", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
            }
        };
    }

    private BroadcastReceiver mReceiverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                // getOrderHistory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

/*
    private void init() {
        sessionManager = new SessionManager(getContext());

        rvOrders = view.findViewById(R.id.rvOrders);
        tvOrderhistory = view.findViewById(R.id.tvOrderhistory);
        progressBar = view.findViewById(R.id.progressBar);
        llOrder = view.findViewById(R.id.llOrder);
        tvOrderLogin = view.findViewById(R.id.tvOrderLogin);
        tvOrderSignup = view.findViewById(R.id.tvOrderSignup);
    }
*/

/*
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvOrderLogin:
                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.tvOrderSignup:
                Intent signupIntent = new Intent(getContext(), RegisterActivity.class);
                startActivity(signupIntent);
                break;
        }
    }
*/
}

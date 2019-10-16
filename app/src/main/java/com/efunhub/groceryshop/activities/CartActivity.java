package com.efunhub.groceryshop.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.adapters.CartAdapter;
import com.efunhub.groceryshop.databinding.ActivityCartBinding;
import com.efunhub.groceryshop.interfaces.AddToCartListener;
import com.efunhub.groceryshop.interfaces.IResult;
import com.efunhub.groceryshop.interfaces.RemoveCart;
import com.efunhub.groceryshop.interfaces.UpdateItemPrice;
import com.efunhub.groceryshop.model.CartRVModel;
import com.efunhub.groceryshop.model.ModelCartDetails;
import com.efunhub.groceryshop.utility.SessionManager;
import com.efunhub.groceryshop.utility.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.efunhub.groceryshop.utility.ConstantVariables.RETRIVE_ALL_CART_DETAILS;
import static com.efunhub.groceryshop.utility.SessionManager.KEY_ID;

public class CartActivity extends BaseActivity implements View.OnClickListener, RemoveCart, AddToCartListener, UpdateItemPrice {

    private ActivityCartBinding mBinder;

    private List<CartRVModel> cartRVModel = new ArrayList<>();
    RecyclerView recyclerViewCart;
    CartAdapter cartAdapter;
    LinearLayout llMyCart, llCartInvisible;

    NestedScrollView nestedCart;
    SessionManager sessionManager;
    CardView cvCart;
    ImageView ivEmptyCart;
    Button btnPlaceOrder;

    ArrayList<CartRVModel> arrayList = new ArrayList<>();

    private String cartUrl = "cart_charges.php";
    private IResult mResultCallback;
    private VolleyService mVolleyService;

    private String custId;

    private int subTotalAmount;

    private ModelCartDetails modelCartDetails;

    //private String sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_cart);

        setUpToolbarWithBackArrow(mBinder.toolbar.toolbar, "My Cart", false, this);

        init();

        if (sessionManager.isLoggedIn()) {
            getAllCartDetails();
        } else {
            mBinder.llOrder.setVisibility(View.VISIBLE);

        }
        mBinder.tvOrderLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mBinder.tvOrderSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        mBinder.btnShopNow.setOnClickListener(this);
        mBinder.btnPlaceOrder.setOnClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void init() {


        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        nestedCart = (NestedScrollView) findViewById(R.id.nestedCartView);
        llMyCart = (LinearLayout) findViewById(R.id.llmyCart);
        ivEmptyCart = (ImageView) findViewById(R.id.ivEmptyCart);
        cvCart = (CardView) findViewById(R.id.cvCart);
        llCartInvisible = (LinearLayout) findViewById(R.id.llCartInvisible);

        sessionManager = new SessionManager(this);

        HashMap<String, String> userInfo = sessionManager.getUserDetails();

        custId = userInfo.get(KEY_ID);

        if (arrayList.size() != 0) {
            arrayList.clear();
        }

        arrayList = sessionManager.getAddToCartList(this);
        subTotalAmount = sessionManager.getSubTotal(this);

        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorTextBlack));


        // recyclerViewCart = (RecyclerView) findViewById(R.id.rvCart);
        @SuppressLint("WrongConstant")
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartActivity.this,
                LinearLayoutManager.VERTICAL, false);
        mBinder.rvCart.setLayoutManager(linearLayoutManager);
        mBinder.rvCart.setHasFixedSize(true);
        mBinder.rvCart.setNestedScrollingEnabled(false);
        mBinder.rvCart.setItemAnimator(new DefaultItemAnimator());

    }


    private void getAllCartDetails() {

        initVolleyCallback();

        mBinder.shimmerViewContainer.startShimmer();

        HashMap<String, String> params = new HashMap<>();

        params.put("cust_id", custId);

        params.put("amount", String.valueOf(subTotalAmount));

        mVolleyService = new VolleyService(mResultCallback, this);
        mVolleyService.postDataVolleyParameters(RETRIVE_ALL_CART_DETAILS,
                this.getResources().getString(R.string.base_url) + cartUrl, params);
    }


    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {

                switch (requestId) {

                    case RETRIVE_ALL_CART_DETAILS:

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 1) {

                                //JSONObject image = jsonObject.getJSONObject("allscreenshortimages");
                                Gson gson = new Gson();
                                modelCartDetails = gson.fromJson(
                                        response, ModelCartDetails.class);
                                mBinder.shimmerViewContainer.stopShimmer();
                                mBinder.shimmerViewContainer.setVisibility(View.GONE);
                                showCart();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        break;

                }


            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                Log.v("Volley requester ", String.valueOf(requestId));
                Log.v("Volley JSON post", String.valueOf(error));
            }
        };
    }

    public void showCart() {

        if ((arrayList == null) || (arrayList.size() == 0)) {

            mBinder.nestedCartView.setVisibility(View.GONE);
            mBinder.llCartInvisible.setVisibility(View.VISIBLE);

        } else {

            mBinder.btnPlaceOrder.setVisibility(View.VISIBLE);

            mBinder.tvSubTotal.setText(this.getResources().getString(R.string.currency) + " " + String.valueOf(subTotalAmount));

            mBinder.tvDeliveryCharges.setText(this.getResources().getString(R.string.currency) + " " + modelCartDetails.getDeliveryCharges());

            mBinder.tvCartTotalAmount.setText(this.getResources().getString(R.string.currency) + " " + modelCartDetails.getTotalPrice());

            if (Integer.parseInt(modelCartDetails.getWallet()) == 0) {

                mBinder.tvWalletAmountCharges.setVisibility(View.GONE);
                mBinder.tvWalletAmountLabel.setVisibility(View.GONE);
            } else {
                mBinder.tvWalletAmountLabel.setText(modelCartDetails.getWallet());
            }

            cartAdapter = new CartAdapter(this, arrayList);

            cartAdapter.notifyDataSetChanged();

            mBinder.rvCart.setAdapter(cartAdapter);

            mBinder.nestedCartView.setVisibility(View.VISIBLE);

            mBinder.llCartInvisible.setVisibility(View.GONE);

        }

    }

    @Override
    public void removeCart() {
        showCart();

    }


    @Override
    public void addToCart() {

    }

    @Override
    public void onItemCountChanged(int size, int price) {

        subTotalAmount = price;
        mBinder.tvSubTotal.setText(this.getResources().getString(R.string.currency) + " " + price);
        getAllCartDetails();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnPlaceOrder:
                StringBuilder producId = new StringBuilder();
                StringBuilder productName = new StringBuilder();
                StringBuilder productEName = new StringBuilder();
                StringBuilder productMName = new StringBuilder();
                StringBuilder Qty = new StringBuilder();
                StringBuilder Weight = new StringBuilder();
                StringBuilder proPrice = new StringBuilder();
                StringBuilder wQTY = new StringBuilder();
                StringBuilder pStatus = new StringBuilder();

                for (CartRVModel cartModel : arrayList) {
                    producId.append(cartModel.getProductID()).append("|");
                    productName.append(cartModel.getProdName()).append("|");
                    productEName.append(cartModel.getProductEName()).append("|");
                    productMName.append(cartModel.getProductMName()).append("|");
                    Qty.append(cartModel.getProdQty()).append("|");
                    Weight.append(cartModel.getpWeight()).append("|");
                    proPrice.append(cartModel.getProdPrice()).append("|");
                    wQTY.append(cartModel.getProdQty()).append("|");
                    pStatus.append("0").append("|");
                }

                Intent intentPlaceOrd = new Intent(CartActivity.this, PaymentMethodAddressActivity.class);
                intentPlaceOrd.putExtra("cutomerId", custId);
                intentPlaceOrd.putExtra("productId", producId.substring(0, producId.length() - 1));
                intentPlaceOrd.putExtra("productName", productName.substring(0, productName.length() - 1));
                intentPlaceOrd.putExtra("productEName", productEName.substring(0, productEName.length() - 1));
                intentPlaceOrd.putExtra("productMName", productMName.substring(0, productMName.length() - 1));
                intentPlaceOrd.putExtra("productQty", Qty.substring(0, Qty.length() - 1));
                intentPlaceOrd.putExtra("productWQty", Qty.substring(0, Qty.length() - 1));
                intentPlaceOrd.putExtra("productWeight", Weight.substring(0, Weight.length() - 1));
                intentPlaceOrd.putExtra("productPrice", proPrice.substring(0, proPrice.length() - 1));
                /*intentPlaceOrd.putExtra("area", "");
                intentPlaceOrd.putExtra("address", "");
                intentPlaceOrd.putExtra("pinCode", "");*/
                intentPlaceOrd.putExtra("walletBalance", "");
                intentPlaceOrd.putExtra("SGSTPrice", "");
                intentPlaceOrd.putExtra("CGSTPrice", "");
                intentPlaceOrd.putExtra("cutbalance", "");
                intentPlaceOrd.putExtra("SGSTPER", "");
                intentPlaceOrd.putExtra("CGSTPER", "");
                intentPlaceOrd.putExtra("deliveryCharges", mBinder.tvDeliveryCharges.getText().toString().replace(this.getResources().getString(R.string.currency), ""));
                intentPlaceOrd.putExtra("walletAmount", mBinder.tvWalletAmountLabel.getText().toString().replace(this.getResources().getString(R.string.currency), ""));
                intentPlaceOrd.putExtra("totalAmount", mBinder.tvCartTotalAmount.getText().toString().replace(this.getResources().getString(R.string.currency), ""));
                intentPlaceOrd.putExtra("payPrice", mBinder.tvCartTotalAmount.getText().toString().replace(this.getResources().getString(R.string.currency), ""));
                intentPlaceOrd.putExtra("paidPrice", mBinder.tvCartTotalAmount.getText().toString().replace(this.getResources().getString(R.string.currency), ""));
                intentPlaceOrd.putExtra("pStatus", pStatus.substring(0, pStatus.length() - 1));
                startActivity(intentPlaceOrd);

                break;


            case R.id.btnShopNow:

                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                break;
        }
    }
}

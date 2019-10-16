package com.efunhub.groceryshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.VolleyError;
import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.adapters.SearchItemsRVAdapter;
import com.efunhub.groceryshop.databinding.ActivitySearchItemsBinding;
import com.efunhub.groceryshop.interfaces.AddToCartListener;
import com.efunhub.groceryshop.interfaces.IResult;
import com.efunhub.groceryshop.model.AllSearchItemBaseModelClass;
import com.efunhub.groceryshop.model.CartRVModel;
import com.efunhub.groceryshop.model.ModelAllSearchItems;
import com.efunhub.groceryshop.utility.SessionManager;
import com.efunhub.groceryshop.utility.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.efunhub.groceryshop.utility.ConstantVariables.RETRIVE_ALL_SEARCH_DATA;

public class SearchItemsActivity extends BaseActivity implements AddToCartListener {

    private ActivitySearchItemsBinding mBinder;
    private IResult mResultCallback;
    private VolleyService mVolleyService;

    private SearchItemsRVAdapter searchItemsRVAdapter;

    private String searchALlItemsUrl = "searchresult.php";

    private String pmid;

    private AllSearchItemBaseModelClass allSearchItemsModelBaseClass;

    private List<ModelAllSearchItems> modelAllSearchItemList;

    private SessionManager sessionManager;

    TextView textCartItemCount;
    int mCartItemCount = 10;

    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_search_items);

        setUpToolbarWithBackArrow(mBinder.toolbar.toolbar, "GroKisan", false, this);

        init();
    }

    private void init() {

        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorTextBlack));


        modelAllSearchItemList = new ArrayList<>();

        sessionManager = new SessionManager(this);

        if (getIntent().hasExtra("pmid")) {
            pmid = getIntent().getStringExtra("pmid");
        }
        getALlSearchItemsData();

    }

    private void getALlSearchItemsData() {

        mBinder.shimmerViewContainer.startShimmer();
        mBinder.rvSearchItems.setVisibility(View.GONE);

        initVolleyCallback();

        HashMap<String, String> params = new HashMap<>();
        params.put("pmid", pmid);

        mVolleyService = new VolleyService(mResultCallback, this);
        mVolleyService.postDataVolleyParameters(RETRIVE_ALL_SEARCH_DATA,
                this.getResources().getString(R.string.base_url) + searchALlItemsUrl, params);

    }


    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {

                switch (requestId) {


                    case RETRIVE_ALL_SEARCH_DATA:

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 1) {

                                Gson gson = new Gson();
                                allSearchItemsModelBaseClass = gson.fromJson(
                                        response, AllSearchItemBaseModelClass.class);

                                modelAllSearchItemList = allSearchItemsModelBaseClass.getAllitems();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mBinder.shimmerViewContainer.stopShimmer();
                        mBinder.shimmerViewContainer.setVisibility(View.GONE);
                        mBinder.rvSearchItems.setVisibility(View.VISIBLE);

                        setUpUi();

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

    private void setUpUi() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setStackFromEnd(false);
        gridLayoutManager.setReverseLayout(false);
        mBinder.rvSearchItems.setLayoutManager(gridLayoutManager);
        searchItemsRVAdapter = new SearchItemsRVAdapter(this, modelAllSearchItemList);
        mBinder.rvSearchItems.setAdapter(searchItemsRVAdapter);


    }

    @Override
    public void onResume() {
        super.onResume();

        this.invalidateOptionsMenu();
    }

    @Override
    public void addToCart() {

        setupBadge();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);

        menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_cart: {
                //Do something

                startActivity(new Intent(SearchItemsActivity.this, CartActivity.class));

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        ArrayList<CartRVModel> cartModels = sessionManager.getAddToCartList(this);

        if (cartModels != null) {

            if (textCartItemCount != null) {
                if (cartModels.size() == 0) {
                    if (textCartItemCount.getVisibility() != View.GONE) {
                        textCartItemCount.setVisibility(View.GONE);
                    }
                } else {

                    textCartItemCount.setText(String.valueOf(Math.min(cartModels.size(), 99)));

                    if (textCartItemCount.getVisibility() != View.VISIBLE) {
                        textCartItemCount.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
}

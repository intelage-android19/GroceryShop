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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.adapters.SubCategoryItemsRVAdapter;
import com.efunhub.groceryshop.databinding.ActivitySubCategoryListBinding;
import com.efunhub.groceryshop.interfaces.AddToCartListener;
import com.efunhub.groceryshop.interfaces.IResult;
import com.efunhub.groceryshop.model.AllSubCategoryModelBaseClass;
import com.efunhub.groceryshop.model.CartRVModel;
import com.efunhub.groceryshop.model.ModelAllSubCategroy;
import com.efunhub.groceryshop.utility.SessionManager;
import com.efunhub.groceryshop.utility.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.efunhub.groceryshop.utility.ConstantVariables.RETRIVE_ALL_SUB_CATEGORY;

public class SubCategoryListActivity extends BaseActivity implements AddToCartListener {

    private ActivitySubCategoryListBinding mBinder;

    private String subCatUrl = "subcategory_products.php";

    private IResult mResultCallback;
    private VolleyService mVolleyService;

    private AllSubCategoryModelBaseClass allSubCategoryBaseClass;

    private List<ModelAllSubCategroy> allSubCategroyList;

    private SubCategoryItemsRVAdapter subCategoryItemsRVAdapter;

    private String psid;
    private String subCatName;

    TextView textCartItemCount;
    int mCartItemCount = 10;

    MenuItem menuItem;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_sub_category_list);

        init();

        getSubCategory();
    }

    private void init() {

        sessionManager = new SessionManager(this);

        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorTextBlack));

        if (getIntent().hasExtra("name")) {

            subCatName = getIntent().getStringExtra("name");
        }

        setUpToolbarWithBackArrow(mBinder.toolbar.toolbar, subCatName, false, this);

        if (getIntent().hasExtra("psid")) {

            psid = getIntent().getStringExtra("psid");
        }

        allSubCategroyList = new ArrayList<>();

    }

    private void getSubCategory() {

        mBinder.subCatPgBar.setVisibility(View.VISIBLE);

        initVolleyCallback();

        HashMap<String, String> params = new HashMap<>();

        params.put("psid", psid);

        mVolleyService = new VolleyService(mResultCallback, this);
        mVolleyService.postDataVolleyParameters(RETRIVE_ALL_SUB_CATEGORY,
                this.getResources().getString(R.string.base_url) + subCatUrl, params);
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {

                switch (requestId) {

                    case RETRIVE_ALL_SUB_CATEGORY:

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 1) {

                                //JSONObject image = jsonObject.getJSONObject("allscreenshortimages");
                                Gson gson = new Gson();
                                allSubCategoryBaseClass = gson.fromJson(
                                        response, AllSubCategoryModelBaseClass.class);
                                allSubCategroyList = allSubCategoryBaseClass.getAllsubcategoryproducts();

                                setUpSubCategoryList();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mBinder.subCatPgBar.setVisibility(View.GONE);

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

    private void setUpSubCategoryList() {

        LinearLayoutManager linearLayoutManagerVegetables = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);

        linearLayoutManagerVegetables.setStackFromEnd(true);
        linearLayoutManagerVegetables.setReverseLayout(true);
        mBinder.rvSubCategories.setLayoutManager(linearLayoutManagerVegetables);
        //mBinder.rvBestsellingItems.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL));
        subCategoryItemsRVAdapter = new SubCategoryItemsRVAdapter(this, allSubCategroyList,
                mBinder.rvSubCategories);
        mBinder.rvSubCategories.setAdapter(subCategoryItemsRVAdapter);
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

                startActivity(new Intent(SubCategoryListActivity.this, CartActivity.class));

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

package com.efunhub.groceryshop.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.adapters.BestsellingRVAdapter;
import com.efunhub.groceryshop.adapters.FruitRVAdapter;
import com.efunhub.groceryshop.adapters.GrainsRVAdapter;
import com.efunhub.groceryshop.adapters.VegetablesRVAdapter;
import com.efunhub.groceryshop.databinding.ActivityCategoriesBinding;
import com.efunhub.groceryshop.interfaces.AddToCartListener;
import com.efunhub.groceryshop.model.CartRVModel;
import com.efunhub.groceryshop.model.ModelBestselling;
import com.efunhub.groceryshop.model.ModelFruits;
import com.efunhub.groceryshop.model.ModelGrains;
import com.efunhub.groceryshop.model.ModelVegetables;
import com.efunhub.groceryshop.utility.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends BaseActivity implements AddToCartListener {


    private ActivityCategoriesBinding mBinder;
    Dialog dialog;

    private VegetablesRVAdapter mVegetablesRVAdapter;
    private List<ModelVegetables> arrayListvegetables = new ArrayList<>();

    private GrainsRVAdapter mGrainsRVAdapter;
    private List<ModelGrains> arrayListGrains = new ArrayList<>();

    private FruitRVAdapter mFruitsRVAdapter;
    private List<ModelFruits> arrayListFruits = new ArrayList<>();


    private BestsellingRVAdapter mBestsellingRVAdapter;
    private List<ModelBestselling> arrayListBestSellingItems = new ArrayList<>();


    String prodName[];
    String catPrice[];
    int prodImage[];

    Toolbar toolbar;
    MenuItem cartItem;
    Menu mMenu;
    SessionManager sessionManager;
    Context context;

    private String categoryId;

    TextView textCartItemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_categories);

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

        sessionManager = new SessionManager(this);

        if (getIntent().hasExtra("catId")) {
            categoryId = getIntent().getStringExtra("catId");

        }

        if (Integer.parseInt(categoryId) == 0) {
            setUpToolbarWithBackArrow(mBinder.toolbar.toolbar, "Best Selling Items", false, this);
            //for vegetable item

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                arrayListBestSellingItems = (List<ModelBestselling>) extras.getSerializable("bestsellitems");
            }


            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            gridLayoutManager.setStackFromEnd(false);
            gridLayoutManager.setReverseLayout(false);
            mBinder.rvCategories.setLayoutManager(gridLayoutManager);
            mBestsellingRVAdapter = new BestsellingRVAdapter(this, arrayListBestSellingItems, mBinder.rvCategories);
            mBinder.rvCategories.setAdapter(mBestsellingRVAdapter);
        } else if (Integer.parseInt(categoryId) == 1) {
            setUpToolbarWithBackArrow(mBinder.toolbar.toolbar, "Vegetables", false, this);
            //for vegetable item

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                arrayListvegetables = (List<ModelVegetables>) extras.getSerializable("vegetables");
            }

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            gridLayoutManager.setStackFromEnd(false);
            gridLayoutManager.setReverseLayout(false);
            mBinder.rvCategories.setLayoutManager(gridLayoutManager);
            mVegetablesRVAdapter = new VegetablesRVAdapter(this, arrayListvegetables, mBinder.rvCategories);
            mBinder.rvCategories.setAdapter(mVegetablesRVAdapter);

            mBinder.rvCategories.setItemAnimator(new DefaultItemAnimator());
        } else if (Integer.parseInt(categoryId) == 2) {
            //for fruits items
            setUpToolbarWithBackArrow(mBinder.toolbar.toolbar, "Fruits", false, this);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                arrayListFruits = (List<ModelFruits>) extras.getSerializable("fruits");
            }

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            gridLayoutManager.setStackFromEnd(false);
            gridLayoutManager.setReverseLayout(false);
            mBinder.rvCategories.setLayoutManager(gridLayoutManager);
            mFruitsRVAdapter = new FruitRVAdapter(this, arrayListFruits, mBinder.rvCategories);
            mBinder.rvCategories.setAdapter(mFruitsRVAdapter);

            mBinder.rvCategories.setItemAnimator(new DefaultItemAnimator());


        } else if (Integer.parseInt(categoryId) == 3) {
            //for grains item
            setUpToolbarWithBackArrow(mBinder.toolbar.toolbar, "Grains", false, this);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                arrayListGrains = (List<ModelGrains>) extras.getSerializable("grains");
            }

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            gridLayoutManager.setStackFromEnd(false);
            gridLayoutManager.setReverseLayout(false);
            mBinder.rvCategories.setLayoutManager(gridLayoutManager);
            mGrainsRVAdapter = new GrainsRVAdapter(this, arrayListGrains);
            mBinder.rvCategories.setAdapter(mGrainsRVAdapter);

            mBinder.rvCategories.setItemAnimator(new DefaultItemAnimator());


        }

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

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

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

                startActivity(new Intent(CategoriesActivity.this, CartActivity.class));

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        ArrayList<CartRVModel> cartModels = sessionManager.getAddToCartList(context);

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

    void dialog() {

        dialog = new Dialog(CategoriesActivity.this);
        dialog.setContentView(R.layout.list_filter);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
        dialog.setCanceledOnTouchOutside(true);

        ImageView ivCancle = dialog.findViewById(R.id.ivClose);
        Button btnCancle = dialog.findViewById(R.id.btnCancle);
        Button btnApply = dialog.findViewById(R.id.btnApplyNow);

        dialog.show();

        ivCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


    }


}

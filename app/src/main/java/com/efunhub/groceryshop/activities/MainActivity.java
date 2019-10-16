package com.efunhub.groceryshop.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.databinding.ActivityMainBinding;
import com.efunhub.groceryshop.fragments.AboutUsFragment;
import com.efunhub.groceryshop.fragments.GrokisanConceptFragment;
import com.efunhub.groceryshop.fragments.HomeFragment;
import com.efunhub.groceryshop.fragments.OffersFragment;
import com.efunhub.groceryshop.fragments.OrdersFragment;
import com.efunhub.groceryshop.fragments.ProfileFragment;
import com.efunhub.groceryshop.fragments.TermsAndConditionsFragment;
import com.efunhub.groceryshop.interfaces.AddToCartListener;
import com.efunhub.groceryshop.model.CartRVModel;
import com.efunhub.groceryshop.utility.SessionManager;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener, AddToCartListener {


    private ActivityMainBinding mBinder;

    static boolean active = false;


    MenuItem cartItem;
    Menu mMenu;
    Context context;
    SessionManager sessionManager;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_CART = "mycart";
    private static final String TAG_MYORDERS = "myorder";
    private static final String TAG_MYACCOUNT = "myaccount";
    private static final String TAG_WISHLIST = "wishlist";
    private static final String TAG_TERMS = "terms";
    private static final String TAG_About_Us = "aboutus";
    private static final String TAG_LOGOUT = "logout";

    TextView textCartItemCount;
    MenuItem menuItem;

    private AlertDialog alertDialog;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setUpUi();

        sessionManager = new SessionManager(this);

        if (getIntent().hasExtra("id")) {

            if (getIntent().getStringExtra("id").equals("1")) {
                pushFragment(new OrdersFragment(), true);
            }

        } else {
            pushFragment(new HomeFragment(), true);
        }


        mBinder.llHome.setOnClickListener(this);
        mBinder.llAboutUs.setOnClickListener(this);
        mBinder.llAccount.setOnClickListener(this);
        mBinder.llMyCart.setOnClickListener(this);
        mBinder.llGroKisanConcept.setOnClickListener(this);
        mBinder.llLogut.setOnClickListener(this);
        mBinder.llSetting.setOnClickListener(this);
        mBinder.llShare.setOnClickListener(this);
        mBinder.rlProfile.setOnClickListener(this);
        mBinder.llOrderHistory.setOnClickListener(this);
        mBinder.llTermsCon.setOnClickListener(this);
        mBinder.llOffer.setOnClickListener(this);

    }

    private void setUpUi() {

        handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
               // showDescriptionDialog();
            }
        };

        handler.postDelayed(runnable, 20000);

        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorTextBlack));
    }

    public void pushFragment(Fragment fragment, boolean clearBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        if (clearBackStack && manager.getBackStackEntryCount() > 0) {
            try {
                manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String backStateName = fragment.getClass().getName();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.Container, fragment, backStateName);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

   /* private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.Container, fragment)
                //.addToBackStack(String.valueOf(fragmentManager))
                .commit();
    }*/

    public void toggleDrawer() {
        if (mBinder.DrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinder.DrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mBinder.DrawerLayout.openDrawer(GravityCompat.START);
        }

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_profile:
                navItemIndex = 1;
                toggleDrawer();
                menuItem.setVisible(false);
                if (sessionManager.isLoggedIn()) {
                    pushFragment(ProfileFragment.newInstance(), true);
                } else {
                    Toast.makeText(getApplicationContext(), "Please log in first", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.llHome:
                navItemIndex = 0;
                toggleDrawer();
                //loadFragment(new HomeFragment());
                pushFragment(HomeFragment.newInstance(), true);
                break;

            case R.id.llOffer:
                navItemIndex = 3;
                toggleDrawer();
                pushFragment(OffersFragment.newInstance(), true);
                break;

            case R.id.llOrderHistory:
                navItemIndex = 4;
                toggleDrawer();
                //loadFragment(new OrdersFragment());
                pushFragment(OrdersFragment.newInstance(), true);
                break;

            case R.id.llMyCart:
                navItemIndex = 5;
                toggleDrawer();
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                break;

            case R.id.llAccount:
                navItemIndex = 6;
                toggleDrawer();
                menuItem.setVisible(false);
                if (sessionManager.isLoggedIn()) {
                    pushFragment(ProfileFragment.newInstance(), true);
                }

                break;

            case R.id.llSetting:
                navItemIndex = 7;
                toggleDrawer();
                break;

            case R.id.llShare:
                navItemIndex = 8;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "GroKisan App";
                String shareSub = "Gro Kisan";
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                startActivity(Intent.createChooser(intent, "Share using"));
                break;

            case R.id.llAboutUs:
                navItemIndex = 9;
                toggleDrawer();
                pushFragment(AboutUsFragment.newInstance(), true);
                break;

            case R.id.llTermsCon:
                navItemIndex = 10;
                toggleDrawer();
                pushFragment(TermsAndConditionsFragment.newInstance(), true);
                break;

            case R.id.llLogut:
                navItemIndex = 11;
                toggleDrawer();
                sessionManager.logoutUser();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;


        }

    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.invalidateOptionsMenu();

        if (!sessionManager.isLoggedIn()) {
            mBinder.llLogut.setVisibility(View.GONE);
            mBinder.llAccount.setVisibility(View.GONE);
            mBinder.llOrderHistory.setVisibility(View.GONE);
        } else {
            mBinder.llLogut.setVisibility(View.VISIBLE);
            mBinder.llAccount.setVisibility(View.VISIBLE);
            mBinder.llOrderHistory.setVisibility(View.VISIBLE);
        }
        setupBadge();
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

            case R.id.action_cart:
                //Do something

                startActivity(new Intent(MainActivity.this, CartActivity.class));
                break;
            case R.id.action_wallet:
                //Do something

                startActivity(new Intent(MainActivity.this, WalletActivity.class));

                return true;


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


    @Override
    public void onBackPressed() {
        if (mBinder.DrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinder.DrawerLayout.closeDrawers();
            finish();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                //CURRENT_TAG = TAG_HOME;
                pushFragment(HomeFragment.newInstance(), true);
                //pushFragment(HomeFragment.newInstance());
                return;
            }
        } else {
            finish();
        }

        super.onBackPressed();

    }

    private void showDescriptionDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater
                = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = layoutInflater.inflate(R.layout.gro_kisan_concept_dialog, null);
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        ImageView ivExpImage = alertDialog.findViewById(R.id.ivExpImage);
        TextView tvExpDesc = alertDialog.findViewById(R.id.tvExpDesc);
        Button close = alertDialog.findViewById(R.id.btnClose);

        Button btnViewMore = alertDialog.findViewById(R.id.btnViewMore);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (active == true) {
                    pushFragment(GrokisanConceptFragment.newInstance(), true);
                    alertDialog.dismiss();
                }
            }
        });

    }

}

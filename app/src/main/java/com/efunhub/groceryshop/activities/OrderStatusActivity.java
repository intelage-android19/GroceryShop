package com.efunhub.groceryshop.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.fragments.OrderErrorFragment;
import com.efunhub.groceryshop.fragments.OrderSuccessFragment;

/**
 * Created by Admin on 12-03-2018.
 */

public class OrderStatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private int status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_status);
        init();

        if (getIntent() != null) {
            status = getIntent().getExtras().getInt("status");
        }

        mToolbar.setTitle("Order Status");
        setSupportActionBar(mToolbar);

        if (status == 1) {
            Fragment fragment = new OrderSuccessFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.add(R.id.flOrder, fragment);
            fragmentTransaction.commit();
        } else {
            Fragment fragment = new OrderErrorFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.add(R.id.flOrder, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();

        Intent intent = new Intent(this, MainActivity.class);
        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void init() {
        mToolbar = findViewById(R.id.toolbar);
    }
}

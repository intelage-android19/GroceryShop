package com.efunhub.groceryshop.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.databinding.ActivitySplashScreenBinding;
import com.efunhub.groceryshop.utility.SessionManager;

public class SplashScreenActivity extends AppCompatActivity {

    private ActivitySplashScreenBinding mBinder;

    private ImageView ivLogo;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);

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


        ivLogo = mBinder.ivLogo;
        sessionManager = new SessionManager(this);




        Thread t = new Thread() {
            public void run() {
                try {
                    fade(ivLogo);
                    sleep(3000);
                    //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    sessionManager.checkLogin();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private void fade(View view) {

        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.logo_anim);
        ivLogo.startAnimation(animation1);
    }
}

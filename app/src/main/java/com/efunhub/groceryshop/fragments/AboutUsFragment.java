package com.efunhub.groceryshop.fragments;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.databinding.FragmentAboutUsBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends BaseFragment {

    private FragmentAboutUsBinding mBinder;

    private String url = "http://grokisan.com/testgrokisan/about-us";

    public AboutUsFragment() {
        // Required empty public constructor
    }

    public static AboutUsFragment newInstance() {

        AboutUsFragment aboutUsFragment = new AboutUsFragment();
        return aboutUsFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinder = DataBindingUtil.inflate(inflater,R.layout.fragment_about_us,container,false);

        setUpToolbarWithoutTitle(mBinder.toolbar.toolbar, true);

        new MyAsynTask().execute();

        return mBinder.getRoot();
    }

    private void init(){


    }

    private class MyAsynTask extends AsyncTask<Void, Void, Document> {
        @Override
        protected Document doInBackground(Void... voids) {

            Document document = null;
            try {
                document = Jsoup.connect(url).get();
                document.getElementById("footer").remove();
                document.getElementById("header").remove();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);

            mBinder.shimmerViewContainer.stopShimmer();
            mBinder.shimmerViewContainer.setVisibility(View.GONE);

            mBinder.wvAboutUs.loadDataWithBaseURL(url, document.toString(), "text/html", "utf-8", "");
            mBinder.wvAboutUs.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            mBinder.wvAboutUs.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, request);
                }
            });
        }
    }


}

package com.efunhub.groceryshop.fragments;

import androidx.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.databinding.FragmentTermsAndConditionsBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


public class TermsAndConditionsFragment extends BaseFragment {

    private FragmentTermsAndConditionsBinding mBinder;

    private String termsUrl = "http://grokisan.com/testgrokisan/terms-conditions";
    private String returnPolicy = "http://grokisan.com/testgrokisan/return-policy";
    private String privacyPlolicy = "http://grokisan.com/testgrokisan/privacy-policy";


    public static TermsAndConditionsFragment newInstance() {
        TermsAndConditionsFragment fragment = new TermsAndConditionsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinder = DataBindingUtil.inflate(inflater, R.layout.fragment_terms_and_conditions, container, false);

        init();

        setUpToolbarWithoutTitle(mBinder.toolbar.toolbar, true);


        return mBinder.getRoot();
    }

    private void init() {

        mBinder.shimmerViewContainer.startShimmer();

        mBinder.wvTerms.getSettings().setJavaScriptEnabled(true);
        new MyAsynTask().execute();


        mBinder.wvReturnPolicy.getSettings().setJavaScriptEnabled(true);
        new ReturnPolicyAsynTask().execute();

        mBinder.wvPrivacyPolicy.getSettings().setJavaScriptEnabled(true);
        new PolicyAsynTask().execute();

    }

    private class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class MyAsynTask extends AsyncTask<Void, Void, Document> {
        @Override
        protected Document doInBackground(Void... voids) {

            Document document = null;
            try {
                document = Jsoup.connect(termsUrl).get();
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


            mBinder.wvTerms.loadDataWithBaseURL(termsUrl, document.toString(), "text/html", "utf-8", "");
            mBinder.wvTerms.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            mBinder.wvTerms.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(termsUrl);
                    return super.shouldOverrideUrlLoading(view, request);
                }
            });
        }
    }

    private class ReturnPolicyAsynTask extends AsyncTask<Void, Void, Document> {
        @Override
        protected Document doInBackground(Void... voids) {

            Document document = null;
            try {
                document = Jsoup.connect(returnPolicy).get();
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


            mBinder.wvReturnPolicy.loadDataWithBaseURL(returnPolicy, document.toString(), "text/html", "utf-8", "");
            mBinder.wvReturnPolicy.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            mBinder.wvReturnPolicy.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(returnPolicy);
                    return super.shouldOverrideUrlLoading(view, request);
                }
            });
        }
    }

    private class PolicyAsynTask extends AsyncTask<Void, Void, Document> {
        @Override
        protected Document doInBackground(Void... voids) {

            Document document = null;
            try {
                document = Jsoup.connect(privacyPlolicy).get();
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

            mBinder.wvPrivacyPolicy.loadDataWithBaseURL(privacyPlolicy, document.toString(), "text/html", "utf-8", "");
            mBinder.wvPrivacyPolicy.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            mBinder.wvPrivacyPolicy.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(privacyPlolicy);
                    return super.shouldOverrideUrlLoading(view, request);
                }
            });
        }
    }


}

package com.efunhub.groceryshop.fragments;

import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.databinding.FragmentGrokisanConceptBinding;
import com.efunhub.groceryshop.interfaces.IResult;
import com.efunhub.groceryshop.utility.VolleyService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class GrokisanConceptFragment extends BaseFragment {

    private FragmentGrokisanConceptBinding mBinder;

    private IResult mResultCallback;
    private VolleyService mVollyService;
    private String userRegistrationURL = "registration_customer.php";

    //http://grokisan.com/testgrokisan/grokisan-concept

    private String url = "http://grokisan.com/testgrokisan/grokisan-concept";


    public static GrokisanConceptFragment newInstance() {
        GrokisanConceptFragment fragment = new GrokisanConceptFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinder = DataBindingUtil.inflate(inflater, R.layout.fragment_grokisan_concept, container, false);

        setHasOptionsMenu(true);

        setUpToolbarWithoutTitle(mBinder.toolbar.toolbar, true);

        //getGroconceptImage();

        //init();

        new MyAsynTask().execute();

        return mBinder.getRoot();
    }

    private void init() {

        mBinder.shimmerViewContainer.startShimmer();

        mBinder.wvGroConcept.getSettings().setJavaScriptEnabled(true);
        mBinder.wvGroConcept.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {
              /*  mBinder.wvGroConcept.loadUrl("javascript:(window.onload = function() { " +
                        "(elem1 = document.getElementById('footer')); elem1.parentNode.removeChild(elem1); " +
                        "(elem2 = document.getElementById('header')); elem2.parentNode.removeChild(elem2); " +
                        "})()");*/

                mBinder.wvGroConcept.loadUrl("javascript:(function() { " +
                        "document.getElementById('footer').style.display='none';" +
                        "document.getElementById('header').style.display='none';" +
                        "})()");
                mBinder.shimmerViewContainer.stopShimmer();
                mBinder.shimmerViewContainer.setVisibility(View.GONE);
                mBinder.scrollView.setVisibility(View.VISIBLE);


            }

        });
        mBinder.wvGroConcept.loadUrl("http://grokisan.com/testgrokisan/grokisan-concept");

       /* Picasso.with(getActivity())
                .load("http://grokisan.com/testgrokisan/templates-assets/FrontEndStyle/images/aboutus.png")
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(mBinder.ivGroConcept);*/


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_cart);
        if (item != null)
            item.setVisible(false);
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
            mBinder.scrollView.setVisibility(View.VISIBLE);

            mBinder.wvGroConcept.loadDataWithBaseURL(url, document.toString(), "text/html", "utf-8", "");
            mBinder.wvGroConcept.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            mBinder.wvGroConcept.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, request);
                }
            });
        }
    }

   /* private void getGroconceptImage() {

        initVolleyCallback();

        *//*mBinder.shimmerViewContainer.startShimmer();
        mBinder.ivGroConcept.setVisibility(View.GONE);*//*

        mVollyService = new VolleyService(mResultCallback, getActivity());


        mVollyService.postDataVolley(GRO_KISAN_CONCEP,
                this.getResources().getString(R.string.base_url) + userRegistrationURL);
    }


    private void initVolleyCallback() {
        try {
            mResultCallback = new IResult() {
                @Override
                public void notifySuccess(int requestId, String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        int status = jsonObject.getInt("status");
                        if (status == 1) {

                            mBinder.ivGroConcept.setVisibility(View.VISIBLE);

                            JSONObject object = jsonObject.getJSONObject("image");


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                  *//*  mBinder.shimmerViewContainer.startShimmer();
                    mBinder.shimmerViewContainer.setVisibility(View.GONE);*//*
                }

                @Override
                public void notifyError(int requestId, VolleyError error) {
                    Log.v("Volley requestid ", String.valueOf(requestId));
                    Log.v("Volley Error", String.valueOf(error));
                }
            };
        } catch (Exception ex) {

            Log.d("GROKISAN Fragment", "initVolleyCallback: " + ex);
        }

    }*/

}

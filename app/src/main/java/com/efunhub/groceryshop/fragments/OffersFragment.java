package com.efunhub.groceryshop.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.adapters.AllOffersRVAdapter;
import com.efunhub.groceryshop.databinding.FragmentOffersBinding;
import com.efunhub.groceryshop.interfaces.IResult;
import com.efunhub.groceryshop.model.AllOffersBaseModel;
import com.efunhub.groceryshop.model.ModelAllOffers;
import com.efunhub.groceryshop.utility.VolleyService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.efunhub.groceryshop.utility.ConstantVariables.RETRIVE_ALL_OFFERS;


public class OffersFragment extends BaseFragment {

    private FragmentOffersBinding mBinder;

    private String offersuRL = "Show-Offers";
    private IResult mResultCallback;
    private VolleyService mVolleyService;

    private AllOffersBaseModel allOffersBaseModel;
    private AllOffersRVAdapter allOffersRVAdapter;
    private List<ModelAllOffers> allOffersList;

    public static OffersFragment newInstance() {
        OffersFragment fragment = new OffersFragment();
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

        mBinder = DataBindingUtil.inflate(inflater, R.layout.fragment_offers, container, false);

        setUpToolbarWithoutTitle(mBinder.toolbar.toolbar, true);

        getAllCategory();

        return mBinder.getRoot();
    }

    private void getAllCategory() {

        mBinder.shimmerViewContainer.startShimmer();

        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallback, getContext());
        mVolleyService.postDataVolley(RETRIVE_ALL_OFFERS,
                this.getResources().getString(R.string.base_url) + offersuRL);
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {

                switch (requestId) {

                    case RETRIVE_ALL_OFFERS:

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 1) {

                                Gson gson = new Gson();

                                allOffersBaseModel = gson.fromJson(
                                        response, AllOffersBaseModel.class);
                                allOffersList = allOffersBaseModel.getAlloffers();

                                setUpAllOffersUi();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mBinder.rvOffers.setVisibility(View.VISIBLE);
                        mBinder.shimmerViewContainer.stopShimmer();
                        mBinder.shimmerViewContainer.setVisibility(View.GONE);
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

    private void setUpAllOffersUi() {

        @SuppressLint("WrongConstant")
        LinearLayoutManager linearLayoutManagerVegetables = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        linearLayoutManagerVegetables.setStackFromEnd(true);
        linearLayoutManagerVegetables.setReverseLayout(true);
        mBinder.rvOffers.setLayoutManager(linearLayoutManagerVegetables);
        allOffersRVAdapter = new AllOffersRVAdapter(getActivity(), allOffersList, mBinder.rvOffers);
        mBinder.rvOffers.setAdapter(allOffersRVAdapter);
    }


}

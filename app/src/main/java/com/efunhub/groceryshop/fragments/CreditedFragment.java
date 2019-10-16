package com.efunhub.groceryshop.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.adapters.CreditedHistoryAdapter;
import com.efunhub.groceryshop.databinding.CreditedrecyclerfragmentBinding;
import com.efunhub.groceryshop.model.CreditedTransactionHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class CreditedFragment extends Fragment {

    private static CreditedrecyclerfragmentBinding mBinder;

    private static CreditedHistoryAdapter creditedHistoryAdapter;

    static Activity context;

    private static List<CreditedTransactionHistoryModel> creditedTransactionHistoryModels = new ArrayList<>();


    public static CreditedFragment newInstance(List<CreditedTransactionHistoryModel> creditedTransactionHistoryModelsList) {

        CreditedFragment fragment = new CreditedFragment();

        creditedTransactionHistoryModels = creditedTransactionHistoryModelsList;

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBinder = DataBindingUtil.inflate(inflater, R.layout.creditedrecyclerfragment, container, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1, GridLayoutManager.VERTICAL, false);
        mBinder.rvcreditedRecycler.setLayoutManager(gridLayoutManager);
        creditedHistoryAdapter = new CreditedHistoryAdapter(getActivity(), creditedTransactionHistoryModels);
        mBinder.rvcreditedRecycler.setAdapter(creditedHistoryAdapter);
        return mBinder.getRoot();

    }
}


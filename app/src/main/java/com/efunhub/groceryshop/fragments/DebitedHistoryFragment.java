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
import com.efunhub.groceryshop.adapters.DebitedHistoryAdapter;
import com.efunhub.groceryshop.databinding.CreditedrecyclerfragmentBinding;
import com.efunhub.groceryshop.model.CreditedTransactionHistoryModel;
import com.efunhub.groceryshop.model.DebitedTransactionHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class DebitedHistoryFragment extends Fragment {



    private static CreditedrecyclerfragmentBinding mBinder;

    private static DebitedHistoryAdapter creditedHistoryAdapter;

    static RecyclerView rvCreditedTransaction;

    static Activity context;

    private static List<DebitedTransactionHistoryModel> debitedTransactionHistoryModelList = new ArrayList<>();


    public static DebitedHistoryFragment newInstance(List<DebitedTransactionHistoryModel> debitedTransactionHistoryModels) {

        DebitedHistoryFragment fragment = new DebitedHistoryFragment();

        debitedTransactionHistoryModelList = debitedTransactionHistoryModels;

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBinder = DataBindingUtil.inflate(inflater, R.layout.creditedrecyclerfragment, container, false);

        rvCreditedTransaction = mBinder.rvcreditedRecycler;

        // if (!creditedTransactionHistoryModels.isEmpty()) {
        creditedHistoryAdapter = new DebitedHistoryAdapter(getActivity(), debitedTransactionHistoryModelList);
     GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        rvCreditedTransaction.setLayoutManager(gridLayoutManager);
        rvCreditedTransaction.setAdapter(creditedHistoryAdapter);
        //   }
        //  else {
        //      mBinder.imgNodata.setVisibility(View.VISIBLE);
        //  }
        return mBinder.getRoot();

    }

}




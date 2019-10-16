package com.efunhub.groceryshop.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.efunhub.groceryshop.R;

/**
 * Created by Admin on 12-03-2018.
 */

public class OrderErrorFragment extends Fragment {

    private View view;
    private Button btnTryAgain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_order_error, container, false);

        init();

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return view;
    }

    private void init() {
        btnTryAgain = view.findViewById(R.id.btnTryAgain);
    }
}

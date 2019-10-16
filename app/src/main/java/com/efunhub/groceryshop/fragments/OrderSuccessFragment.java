package com.efunhub.groceryshop.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.activities.MainActivity;

/**
 * Created by Admin on 12-03-2018.
 */

public class OrderSuccessFragment extends Fragment {

    private View view;
    private Button btnContinueOrder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_order_success, container, false);

        init();

        btnContinueOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finishAffinity();

                Intent intent = new Intent(getContext(), MainActivity.class);

                intent.putExtra("id", "1");
                // Closing all the Activities
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });

        return view;
    }

    private void init() {
        btnContinueOrder = view.findViewById(R.id.btnContinueOrder);
    }
}

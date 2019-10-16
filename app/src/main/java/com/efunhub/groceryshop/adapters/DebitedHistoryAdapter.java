package com.efunhub.groceryshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.model.CreditedTransactionHistoryModel;
import com.efunhub.groceryshop.model.DebitedTransactionHistoryModel;

import java.util.List;

public class DebitedHistoryAdapter extends RecyclerView.Adapter<DebitedHistoryAdapter.ViewHolder> {

    private Context context;
    private List<DebitedTransactionHistoryModel> debitedTransactionHistoryModelList;


    public DebitedHistoryAdapter(Context context, List<DebitedTransactionHistoryModel> debitedTransactionHistoryModels) {
        this.context = context;
        this.debitedTransactionHistoryModelList = debitedTransactionHistoryModels;

    }


    public DebitedHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.credited_history_list_adapter, null);
        return new DebitedHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DebitedHistoryAdapter.ViewHolder viewHolder, int pos) {

        DebitedTransactionHistoryModel debitedTransactionHistoryModel = debitedTransactionHistoryModelList.get(pos);

        viewHolder.tvTransferdNumber.setText(debitedTransactionHistoryModel.getTransferContact());
        viewHolder.tvTransferdAmount.setText(debitedTransactionHistoryModel.getTransferAmount());
        viewHolder.tvTransferedDate.setText(debitedTransactionHistoryModel.getCreatedAt());


    }


    @Override
    public int getItemCount() {
        return debitedTransactionHistoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTransferdNumber, tvTransferdAmount, tvTransferedDate;


        public ViewHolder(View itemView) {
            super(itemView);

            tvTransferdNumber = itemView.findViewById(R.id.txtContact);
            tvTransferdAmount = itemView.findViewById(R.id.txtAmount);
            tvTransferedDate = itemView.findViewById(R.id.txtDate);



        }
    }
}





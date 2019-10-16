package com.efunhub.groceryshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.model.CreditedTransactionHistoryModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreditedHistoryAdapter extends RecyclerView.Adapter<CreditedHistoryAdapter.ViewHolder> {

    private Context context;
    private List<CreditedTransactionHistoryModel> creditedTransactionHistoryModels;


    public CreditedHistoryAdapter(Context context, List<CreditedTransactionHistoryModel> creditedTransactionHistoryModels) {
        this.context = context;
        this.creditedTransactionHistoryModels = creditedTransactionHistoryModels;

    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.credited_history_list_adapter, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {

        CreditedTransactionHistoryModel creditedTransactionHistoryModel = creditedTransactionHistoryModels.get(pos);


        viewHolder.tvTransferdNumber.setText(creditedTransactionHistoryModel.getTransferContact());
        viewHolder.tvTransferdAmount.setText(creditedTransactionHistoryModel.getTransferAmount());

        String transfereddate  = creditedTransactionHistoryModel.getCreatedAt();


      /*  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateWithoutTime = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Date date = sdf.parse(transfereddate);

            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

*/
        viewHolder.tvTransferedDate.setText(creditedTransactionHistoryModel.getCreatedAt());


    }


    @Override
    public int getItemCount() {
        return creditedTransactionHistoryModels.size();
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





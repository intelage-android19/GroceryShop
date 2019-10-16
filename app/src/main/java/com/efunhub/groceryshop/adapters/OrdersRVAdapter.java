package com.efunhub.groceryshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.fragments.OrdersRVModel;
import com.efunhub.groceryshop.model.SpecificOrderRVModel;

import java.util.ArrayList;

/**
 * Created by Admin on 19-02-2018.
 */

public class OrdersRVAdapter extends RecyclerView.Adapter<OrdersRVAdapter.ItemViewHolder> {

    private Context mContext;
    private ArrayList<OrdersRVModel> arrayList;

    public OrdersRVAdapter(Context mContext, ArrayList<OrdersRVModel> ordersRVModelArrayList) {
        this.mContext = mContext;
        this.arrayList = ordersRVModelArrayList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder itemViewHolder;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_item_view, parent, false);
        itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        OrdersRVModel ordersRVModel = arrayList.get(position);

        ArrayList<SpecificOrderRVModel> specificOrderModelArrayList = ordersRVModel.getSpecificOrderRVModelArrayList();

       /* OrderSpecificRVAdapter sellerOrderSpecificRVAdapter = new OrderSpecificRVAdapter(mContext, specificOrderModelArrayList);
        holder.rvSpecificOrder.setHasFixedSize(true);
        holder.rvSpecificOrder.setLayoutManager(new GridLayoutManager(mContext, 1));
        holder.rvSpecificOrder.setAdapter(sellerOrderSpecificRVAdapter);*/

        holder.tvOrderDate.setText(ordersRVModel.getOdate());
        holder.tvOrderID.setText(ordersRVModel.getOid());
        holder.tvOrderTotalPrice.setText(mContext.getResources().getString(R.string.currency) + String.valueOf(ordersRVModel.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rvSpecificOrder;
        TextView tvOrderDate, tvOrderID, tvOrderTotalPrice;

        ItemViewHolder(View itemView) {
            super(itemView);

            rvSpecificOrder = itemView.findViewById(R.id.rvSpecificOrder);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderID = itemView.findViewById(R.id.tvOrderID);
            tvOrderTotalPrice = itemView.findViewById(R.id.tvOrderTotalPrice);
        }
    }
}

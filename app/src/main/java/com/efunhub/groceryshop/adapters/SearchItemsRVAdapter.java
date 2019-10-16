package com.efunhub.groceryshop.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.interfaces.AddToCartListener;
import com.efunhub.groceryshop.model.CartRVModel;
import com.efunhub.groceryshop.model.ModelAllSearchItems;
import com.efunhub.groceryshop.utility.SessionManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchItemsRVAdapter extends RecyclerView.Adapter<SearchItemsRVAdapter.MyViewHolder> {

    private Context context;

    private List<ModelAllSearchItems> modelAllSearchItems = new ArrayList<>();
    private View rootview;
    private final int ROW_ITEM = 0, ROW_PROG = 2;
    private SessionManager sessionManager;
    AddToCartListener addToCartListener;
    String user_id;

    public SearchItemsRVAdapter(Context applicationContext, List<ModelAllSearchItems> modelAllSearchItems) {

        this.modelAllSearchItems = modelAllSearchItems;
        this.context = applicationContext;

        this.sessionManager = new SessionManager(context);

        HashMap<String, String> userWishList = sessionManager.getUserDetails();
        user_id = userWishList.get(sessionManager.KEY_ID);

        try {
            this.addToCartListener = ((AddToCartListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ROW_ITEM) {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_grain_items, parent, false);
            return new MyViewHolder(rootview);
        } else {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_loading, parent, false);
            return new MyViewHolder(rootview);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final ModelAllSearchItems modelAllSearchItem = modelAllSearchItems.get(position);

        int itemViewType = getItemViewType(position);
        if (itemViewType == ROW_ITEM) {


            if (Integer.parseInt(modelAllSearchItem.getOfferPrice()) == 0) {
                holder.cvGrainsOfferPercentage.setVisibility(View.GONE);
                holder.tvGrainsPrice.setVisibility(View.GONE);
            } else {
                holder.tvGrainsOfferPer.setText(modelAllSearchItem.getOfferPrice() + "%" + " OFF");
                holder.tvGrainsPrice.setText(context.getResources().getString(R.string.currency) + " " + modelAllSearchItem.getProductPrice());
                holder.tvGrainsPrice.setPaintFlags(holder.tvGrainsPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            holder.tvGrainsName.setText(modelAllSearchItem.getProductEname());

            holder.tvGrainsPrice.setText(context.getResources().getString(R.string.currency) + " " + modelAllSearchItem.getProductPrice());
            holder.tvGrainsPrice.setPaintFlags(holder.tvGrainsPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.tvGrainsQty.setText(modelAllSearchItem.getQty() + " Kg");
            holder.tvGrainOfferPrice.setText(context.getResources().getString(R.string.currency) + " " + modelAllSearchItem.getFinalPrice());

            if (!modelAllSearchItem.getProductImg().equalsIgnoreCase("")) {
                Picasso.with(context)
                        .load(context.getResources().getString(R.string.images_base_url) + modelAllSearchItem.getProductImg())
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.ivGrainImage);


            }

        }

    }

    @Override
    public int getItemViewType(int position) {
//        return ROW_ITEM;
        if (modelAllSearchItems.get(position) != null) return ROW_ITEM;
        else return ROW_PROG;

    }

    @Override
    public int getItemCount() {
        return modelAllSearchItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvGrainsName, tvGrainsOfferPer, tvGrainsPrice, tvGrainsQty, tvGrainOfferPrice;
        LinearLayout llItem;
        ImageView ivGrainImage;
        Button btnAdd;
        CardView cvGrainsOfferPercentage;

        public MyViewHolder(View itemView) {
            super(itemView);

            llItem = itemView.findViewById(R.id.llItem);
            ivGrainImage = itemView.findViewById(R.id.ivGrainImage);
            tvGrainsOfferPer = itemView.findViewById(R.id.tvGrainsOfferPercentage);
            tvGrainsName = itemView.findViewById(R.id.tvGrainName);
            tvGrainsPrice = itemView.findViewById(R.id.tvGrainPrice);
            tvGrainOfferPrice = itemView.findViewById(R.id.tvGrainOfferPrice);
            tvGrainsQty = itemView.findViewById(R.id.tvGrainsQty);
            btnAdd = itemView.findViewById(R.id.btnAddGrains);
            cvGrainsOfferPercentage = itemView.findViewById(R.id.cvGrainsOfferPercentage);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelAllSearchItems current = modelAllSearchItems.get(getAdapterPosition());

                    CartRVModel cartProduct = new CartRVModel();

                    cartProduct.setProdPrice(current.getProductPrice());
                    cartProduct.setImg(current.getProductImg());
                    cartProduct.setProdName(current.getProductEname());
                    cartProduct.setProductID(current.getQty());
                    cartProduct.setProdQty(1);
                    sessionManager.addToCart(context, cartProduct);
                    addToCartListener.addToCart();

                }
            });

        }
    }


}

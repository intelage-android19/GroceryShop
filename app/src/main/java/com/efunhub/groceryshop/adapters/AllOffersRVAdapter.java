package com.efunhub.groceryshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.interfaces.AddToCartListener;
import com.efunhub.groceryshop.model.ModelAllOffers;
import com.efunhub.groceryshop.utility.SessionManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AllOffersRVAdapter extends RecyclerView.Adapter<AllOffersRVAdapter.MyViewHolder> {

    private Context context;
    private List<ModelAllOffers> allOffersList;
    private View rootview;
    private final int ROW_ITEM = 0, ROW_PROG = 2;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    String s1 = null;
    String s2 = "null";
    int[] a = {1, 2, 3};
    int[][] b = {{1, 2, 4,}, {2, 3}};
    //GlideLoader glideLoader;
    private SessionManager sessionManager;
    AddToCartListener addToCartListener;
    String user_id;

    String offersURL  = "https://www.efunhub.in/AppBuilder/groceryshop/images/offers/";

    public AllOffersRVAdapter(Context context, List<ModelAllOffers> allOffersList, RecyclerView recyclerView) {
        this.context = context;
        this.allOffersList = allOffersList;

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
            rootview = LayoutInflater.from(context).inflate(R.layout.row_offers_items, parent, false);
            return new MyViewHolder(rootview);
        } else {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_loading, parent, false);
            return new MyViewHolder(rootview);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final ModelAllOffers modelAllOffers = allOffersList.get(position);

        int itemViewType = getItemViewType(position);
        if (itemViewType == ROW_ITEM) {

            if (!modelAllOffers.getLogo().equalsIgnoreCase("")) {
                Picasso.with(context)
                        .load(offersURL+modelAllOffers.getLogo())
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.ivOfferImage);

            }
        }

    }

    @Override
    public int getItemViewType(int position) {
//        return ROW_ITEM;
        if (allOffersList.get(position) != null) return ROW_ITEM;
        else return ROW_PROG;
    }


    @Override
    public int getItemCount() {
        return allOffersList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivOfferImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivOfferImage = (ImageView) itemView.findViewById(R.id.ivOfferImage);

        }
    }
}

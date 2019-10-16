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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.interfaces.AddToCartListener;
import com.efunhub.groceryshop.model.CartRVModel;
import com.efunhub.groceryshop.model.ModelVegetables;
import com.efunhub.groceryshop.utility.SessionManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VegetablesRVAdapter extends RecyclerView.Adapter<VegetablesRVAdapter.MyViewHolder> {
    private Context context;
    private List<ModelVegetables> arrayCategories = new ArrayList<>();
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


    public VegetablesRVAdapter(Context context, List<ModelVegetables> arrayCategories, RecyclerView rvVegetableItems) {
        this.context = context;
        this.arrayCategories = arrayCategories;
        this.sessionManager = new SessionManager(context);
        //this.addToCartListener = (AddToCartListener) context;

        HashMap<String, String> userWishList = sessionManager.getUserDetails();
        user_id = userWishList.get(sessionManager.KEY_ID);

        try {
            this.addToCartListener = ((AddToCartListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }


    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == ROW_ITEM) {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_vegetables_items, parent, false);
            return new MyViewHolder(rootview);
        } else {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_loading, parent, false);
            return new MyViewHolder(rootview);
        }
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final ModelVegetables modelVegetables = arrayCategories.get(position);


        int itemViewType = getItemViewType(position);

        if (itemViewType == ROW_ITEM) {

            if(Integer.parseInt(modelVegetables.getOfferPrice())==0){
                holder.cvVegOfferPercentage.setVisibility(View.GONE);
                holder.tvVegPrice.setVisibility(View.GONE);
            }else
            {
                holder.tvvegOfferPer.setText(modelVegetables.getOfferPrice()+"%"+" OFF");
                holder.tvVegPrice.setText(context.getResources().getString(R.string.currency) + " " + modelVegetables.getProductPrice());
                holder.tvVegPrice.setPaintFlags(holder.tvVegPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }


            holder.tvVegName.setText(modelVegetables.getProductEname());

            holder.tvVegQty.setText(modelVegetables.getQty()+" "+modelVegetables.getProductWeight());
            holder.tvOfferPrice.setText(context.getResources().getString(R.string.currency) + " " +modelVegetables.getFinalPrice());


            if (!modelVegetables.getProductImg().equalsIgnoreCase("")) {
                Picasso.with(context)
                        .load(context.getResources().getString(R.string.images_base_url) + modelVegetables.getProductImg())
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.ivVegImage);


            }

            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }


    }


    @Override
    public int getItemViewType(int position) {
//        return ROW_ITEM;
        if (arrayCategories.get(position) != null) return ROW_ITEM;
        else return ROW_PROG;

    }

    @Override
    public int getItemCount() {
//        return 10;
        return arrayCategories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvVegName, tvvegOfferPer,tvVegPrice, tvVegQty, tvOfferPrice;
        LinearLayout llItem;
        ImageView ivVegImage;
        Button btnAdd;
        CardView cvVegOfferPercentage;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvVegName = (TextView) itemView.findViewById(R.id.tvVegetableName);
            tvVegPrice = (TextView) itemView.findViewById(R.id.tvVegetablePrice);
            tvVegQty = (TextView) itemView.findViewById(R.id.tvVegetableQty);
            llItem = (LinearLayout) itemView.findViewById(R.id.llItem);
            ivVegImage = (ImageView) itemView.findViewById(R.id.ivVegetableImage);
            tvvegOfferPer = (TextView) itemView.findViewById(R.id.tvVegOfferPercentage);
            tvOfferPrice = (TextView) itemView.findViewById(R.id.tvVegetableOfferPrice);
            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
            cvVegOfferPercentage =  itemView.findViewById(R.id.cvVegOfferPercentage);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelVegetables modelVegetables = arrayCategories.get(getAdapterPosition());

                    CartRVModel cartProduct = new CartRVModel();

                    cartProduct.setProdPrice(modelVegetables.getProductPrice());
                    cartProduct.setImg(modelVegetables.getProductImg());
                    cartProduct.setProdName(modelVegetables.getProductEname());
                    cartProduct.setProductID(modelVegetables.getProductId());
                    cartProduct.setProductEName(modelVegetables.getProductEname());
                    cartProduct.setProductMName(modelVegetables.getProductMname());
                    cartProduct.setpWeight(modelVegetables.getProductWeight());
                    cartProduct.setProdQty(1);
                    sessionManager.addToCart(context, cartProduct);
                    addToCartListener.addToCart();

                }
            });

        }
    }


}

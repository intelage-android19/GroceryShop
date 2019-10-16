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
import com.efunhub.groceryshop.model.ModelFruits;
import com.efunhub.groceryshop.utility.SessionManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class FruitRVAdapter extends RecyclerView.Adapter<FruitRVAdapter.MyViewHolder> {

    private Context context;
    private List<ModelFruits> arrayCategories;
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


    public FruitRVAdapter(Context context, List<ModelFruits> arrayCategories, RecyclerView recyclerView) {
        this.context = context;
        this.arrayCategories = arrayCategories;

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
            rootview = LayoutInflater.from(context).inflate(R.layout.row_fruit_items, parent, false);
            return new FruitRVAdapter.MyViewHolder(rootview);
        } else {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_loading, parent, false);
            return new FruitRVAdapter.MyViewHolder(rootview);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final ModelFruits modelFruits = arrayCategories.get(position);

        int itemViewType = getItemViewType(position);
        if (itemViewType == ROW_ITEM) {

            holder.tvVegName.setText(modelFruits.getProductEname());

            if (Integer.parseInt(modelFruits.getOfferPrice()) == 0) {
                holder.cvFruitsPercentage.setVisibility(View.GONE);
                holder.tvVegPrice.setVisibility(View.GONE);
            } else {
                holder.tvFriutsOfferPer.setText(modelFruits.getOfferPrice() + "%" + " OFF");
                holder.tvVegPrice.setText(context.getResources().getString(R.string.currency) + " " + modelFruits.getFinalPrice());
                holder.tvVegPrice.setPaintFlags(holder.tvVegPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            holder.tvVegQty.setText(modelFruits.getQty() + " "+modelFruits.getProductWeight());

            holder.tvOfferPrice.setText(context.getResources().getString(R.string.currency) + " " + modelFruits.getFinalPrice());

            if (!modelFruits.getProductImg().equalsIgnoreCase("")) {
                Picasso.with(context)
                        .load(context.getResources().getString(R.string.images_base_url) + modelFruits.getProductImg())
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
        return arrayCategories.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvVegName, tvFriutsOfferPer, tvVegPrice, tvVegQty, tvOfferPrice;
        LinearLayout llItem;
        ImageView ivVegImage;
        Button btnAdd;
        CardView cvFruitsPercentage;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvVegName = (TextView) itemView.findViewById(R.id.tvVegetableName);
            tvVegPrice = (TextView) itemView.findViewById(R.id.tvVegetablePrice);
            tvVegQty = (TextView) itemView.findViewById(R.id.tvVegetableQty);
            tvFriutsOfferPer = (TextView) itemView.findViewById(R.id.tvFruitsOfferPercentage);
            llItem = (LinearLayout) itemView.findViewById(R.id.llItem);
            ivVegImage = (ImageView) itemView.findViewById(R.id.ivVegetableImage);
            tvOfferPrice = (TextView) itemView.findViewById(R.id.tvVegetableOfferPrice);
            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
            cvFruitsPercentage = itemView.findViewById(R.id.cvFruitsPercentage);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelFruits current = arrayCategories.get(getAdapterPosition());

                    CartRVModel cartProduct = new CartRVModel();

                    cartProduct.setProdPrice(current.getProductPrice());
                    cartProduct.setImg(current.getProductImg());
                    cartProduct.setProdName(current.getProductEname());
                    cartProduct.setProductID(current.getProductId());
                    cartProduct.setProductEName(current.getProductEname());
                    cartProduct.setProductMName(current.getProductMname());
                    cartProduct.setpWeight(current.getProductWeight());
                    cartProduct.setProdQty(1);
                    sessionManager.addToCart(context, cartProduct);
                    addToCartListener.addToCart();

                }
            });


        }
    }
}

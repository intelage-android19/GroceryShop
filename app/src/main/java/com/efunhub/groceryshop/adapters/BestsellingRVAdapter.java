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
import com.efunhub.groceryshop.model.ModelBestselling;
import com.efunhub.groceryshop.utility.SessionManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class BestsellingRVAdapter extends RecyclerView.Adapter<BestsellingRVAdapter.MyViewHolder> {

    private Context context;
    private List<ModelBestselling> arrayCategories;
    private View rootview;
    private final int ROW_ITEM = 0, ROW_PROG = 2;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    String s1 = null;
    String s2 = "null";
    int[] a = {1, 2, 3};
    int[][] b = {{1, 2, 4,}, {2, 3}};
    private SessionManager sessionManager;
    AddToCartListener addToCartListener;
    String user_id;

    //GlideLoader glideLoader;

    public BestsellingRVAdapter(Context context, List<ModelBestselling> arrayCategories, RecyclerView recyclerView) {
        this.context = context;
        this.arrayCategories = arrayCategories;
        this.sessionManager = new SessionManager(context);
        this.addToCartListener = (AddToCartListener) context;


        HashMap<String, String> userWishList = sessionManager.getUserDetails();
        user_id = userWishList.get(sessionManager.KEY_ID);

        try {
            this.addToCartListener = ((AddToCartListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvBestSellingIgtemName, tvBestSellingItemPrice, tvBestSellingItemQty, tvBestSellingItemOfferPrice, tvBestSellingOfferPercentage;
        LinearLayout llItem;
        ImageView ivBestSellingItemImage;
        Button btnAdd;
        CardView cvBestSellingOfferPercent;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvBestSellingOfferPercentage = (TextView) itemView.findViewById(R.id.tvBestSellingOfferPercent);
            tvBestSellingIgtemName = (TextView) itemView.findViewById(R.id.tvBestSellingItemName);
            tvBestSellingItemPrice = (TextView) itemView.findViewById(R.id.tvBestSellingItemPrice);
            tvBestSellingItemQty = (TextView) itemView.findViewById(R.id.tvBestSellingItemQty);
            llItem = (LinearLayout) itemView.findViewById(R.id.llItem);
            ivBestSellingItemImage = (ImageView) itemView.findViewById(R.id.ivBestSellingItemImage);
            tvBestSellingItemOfferPrice = (TextView) itemView.findViewById(R.id.tvBestSellingItemOfferPrice);
            btnAdd = (Button) itemView.findViewById(R.id.btnBestSellingItemAdd);
            cvBestSellingOfferPercent = itemView.findViewById(R.id.cvBestSellingOfferPercent);


            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelBestselling current = arrayCategories.get(getAdapterPosition());

                    CartRVModel cartProduct = new CartRVModel();

                    cartProduct.setProdPrice(current.getProductPrice());
                    //cartProduct.setSubTotal(current.getProductPrice());
                    cartProduct.setProductID(current.getProductId());
                    cartProduct.setImg(current.getProductImg());
                    cartProduct.setProdName(current.getProductEname());
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

    @NonNull
    @Override
    public BestsellingRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ROW_ITEM) {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_bestselling_items, parent, false);
            return new BestsellingRVAdapter.MyViewHolder(rootview);
        } else {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_loading, parent, false);
            return new BestsellingRVAdapter.MyViewHolder(rootview);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final ModelBestselling modelBestselling = arrayCategories.get(position);


        int itemViewType = getItemViewType(position);
        if (itemViewType == ROW_ITEM) {

            if (Integer.parseInt(modelBestselling.getOfferPrice()) == 0) {
                holder.cvBestSellingOfferPercent.setVisibility(View.GONE);
                holder.tvBestSellingItemPrice.setVisibility(View.GONE);
            } else {
                holder.tvBestSellingItemOfferPrice.setText(modelBestselling.getOfferPrice() + "%" + " Off");
                holder.tvBestSellingItemPrice.setText(context.getResources().getString(R.string.currency) + " " + modelBestselling.getProductPrice());
                holder.tvBestSellingItemPrice.setPaintFlags(holder.tvBestSellingItemPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            holder.tvBestSellingIgtemName.setText(modelBestselling.getProductEname());

            holder.tvBestSellingItemQty.setText(modelBestselling.getQty() + " " + modelBestselling.getProductWeight());
            holder.tvBestSellingItemOfferPrice.setText(context.getResources().getString(R.string.currency) + " " + modelBestselling.getFinalPrice());

            if (!modelBestselling.getProductImg().equalsIgnoreCase("")) {
                Picasso.with(context)
                        .load(context.getResources().getString(R.string.images_base_url) + modelBestselling.getProductImg())
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.ivBestSellingItemImage);


            }

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


}

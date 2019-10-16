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
import com.efunhub.groceryshop.model.ModelAllSubCategroy;
import com.efunhub.groceryshop.utility.SessionManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubCategoryItemsRVAdapter extends RecyclerView.Adapter<SubCategoryItemsRVAdapter.MyViewHolder> {


    private Context context;
    private List<ModelAllSubCategroy> arrayCategories;
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

    private AddToCartListener addToCartListener;

    public SubCategoryItemsRVAdapter(Context context, List<ModelAllSubCategroy> arrayCategories, RecyclerView recyclerView) {
        this.context = context;
        this.arrayCategories = arrayCategories;
        this.sessionManager = new SessionManager(context);
        addToCartListener = (AddToCartListener) context;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ROW_ITEM) {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_subcategory_items_list, parent, false);
            return new MyViewHolder(rootview);
        } else {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_loading, parent, false);
            return new MyViewHolder(rootview);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final ModelAllSubCategroy modelSubCategory = arrayCategories.get(position);

        int itemViewType = getItemViewType(position);
        if (itemViewType == ROW_ITEM) {

            if (Integer.parseInt(modelSubCategory.getOfferPrice()) == 0) {
                holder.tvSubCatOfferPercentage.setVisibility(View.GONE);
                holder.tvSubCatPrice.setVisibility(View.GONE);
            } else {
                holder.tvSubCatOfferPercentage.setText(modelSubCategory.getOfferPrice() + "%" + " OFF");
                holder.tvSubCatPrice.setText(context.getResources().getString(R.string.currency) + " " + modelSubCategory.getProductPrice());
                holder.tvSubCatPrice.setPaintFlags(holder.tvSubCatPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            holder.tvSubCatName.setText(modelSubCategory.getProductEname());

            holder.tvSubCatOfferPrice.setText(context.getResources().getString(R.string.currency) + " " + modelSubCategory.getFinalPrice());

            holder.tvSubCatQty.setText(modelSubCategory.getQty() + " " + modelSubCategory.getProductWeight());

            if (!modelSubCategory.getProductImg().equalsIgnoreCase("")) {
                Picasso.with(context)
                        .load(context.getResources().getString(R.string.images_base_url) + modelSubCategory.getProductImg())
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.ivSubCatImage);


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


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tvSubCatName, tvSubCatPrice, tvSubCatQty, tvSubCatOfferPrice, tvSubCatOfferPercentage;
        CardView cvSubCatOfferPercentage;
        LinearLayout llItem;
        ImageView ivSubCatImage;
        Button btnAdd;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvSubCatName = (TextView) rootview.findViewById(R.id.tvSubCatName);
            tvSubCatPrice = (TextView) rootview.findViewById(R.id.tvSubCatPrice);
            tvSubCatQty = (TextView) rootview.findViewById(R.id.tvSubCatQty);
            tvSubCatOfferPrice = (TextView) rootview.findViewById(R.id.tvSubCatOfferPrice);
            tvSubCatOfferPercentage = (TextView) rootview.findViewById(R.id.tvSubCatOfferPercentage);
            tvSubCatOfferPercentage = (TextView) rootview.findViewById(R.id.tvSubCatOfferPercentage);
            cvSubCatOfferPercentage = rootview.findViewById(R.id.cvSubCatOfferPercentage);

            btnAdd = rootview.findViewById(R.id.btnAdd);

            ivSubCatImage = (ImageView) rootview.findViewById(R.id.ivSubCatImage);
            //llItem = (LinearLayout) rootview.findViewById(R.id.llItem);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelAllSubCategroy current = arrayCategories.get(getAdapterPosition());

                    CartRVModel cartProduct = new CartRVModel();

                    cartProduct.setProdPrice(current.getProductPrice());
                    cartProduct.setImg(current.getProductImg());
                    cartProduct.setProdName(current.getProductEname());
                    cartProduct.setProductID(current.getQty());
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


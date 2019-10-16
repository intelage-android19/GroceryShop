package com.efunhub.groceryshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.activities.SubCategoryListActivity;
import com.efunhub.groceryshop.model.ModelSubCategory;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubCategoriesRVAdapter extends RecyclerView.Adapter<SubCategoriesRVAdapter.MyViewHolder>{


    private Context context;
    private List<ModelSubCategory> arrayCategories;
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

    public SubCategoriesRVAdapter(Context context, List<ModelSubCategory> arrayCategories, RecyclerView recyclerView) {
        this.context = context;
        this.arrayCategories = arrayCategories;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ROW_ITEM) {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_subcategories_items, parent, false);
            return new MyViewHolder(rootview);
        } else {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_loading, parent, false);
            return new MyViewHolder(rootview);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final ModelSubCategory modelSubCategory = arrayCategories.get(position);

        int itemViewType = getItemViewType(position);
        if (itemViewType == ROW_ITEM) {
            holder.tvMechName.setText(modelSubCategory.getSubCategoryEname());

            if(!modelSubCategory.getSubCategoryImage().equalsIgnoreCase("")){
                Picasso.with(context)
                        .load(context.getResources().getString(R.string.sub_cat_images_base_url)+ modelSubCategory.getSubCategoryImage())
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(holder.ivMechImage);


            }

            holder.ivMechImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelSubCategory modelSubCategory = arrayCategories.get(position);

                    Intent intent = new Intent(context, SubCategoryListActivity.class);
                    intent.putExtra("psid",modelSubCategory.getPsid());
                    intent.putExtra("name",modelSubCategory.getSubCategoryEname());
                    context.startActivity(intent);
                }
            });

           // holder.ivMechImage.setImageResource(modelMechanism.getSubImage());

            //glideLoader.loadImageSimple(modelCategories.getCategoryIcon(), holder.ivCatImage);
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
        TextView tvMechName,tvMechDetails;
        LinearLayout llItem;
        ImageView ivMechImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvMechName = (TextView) rootview.findViewById(R.id.tvSubTitle);
            ivMechImage = (ImageView) rootview.findViewById(R.id.ivSubImage);
            //llItem = (LinearLayout) rootview.findViewById(R.id.llItem);

        }
    }

}

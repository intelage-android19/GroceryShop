package com.efunhub.groceryshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.model.ModelCategories;

import java.util.ArrayList;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ModelCategories> arrayCategories;
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

    public CategoryRVAdapter(Context context, ArrayList<ModelCategories> arrayCategories, RecyclerView recyclerView) {
        this.context = context;
        this.arrayCategories = arrayCategories;
        recyclerView.scrollToPosition(arrayCategories.size() - 1);
        //this.glideLoader = glideLoader;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ROW_ITEM) {
            rootview = LayoutInflater.from(context).inflate(R.layout.raw_categories, parent, false);
            return new MyViewHolder(rootview);
        } else {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_loading, parent, false);
            return new MyViewHolder(rootview);
        }

    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final ModelCategories modelCategories = arrayCategories.get(position);

        int itemViewType = getItemViewType(position);
        if (itemViewType == ROW_ITEM) {
/*
            holder.tvCatName.setText(modelCategories.getCategoryName());

            holder.tvCatName.setText(modelCategories.getCategoryName());
*/

            //glideLoader.loadImageSimple(modelCategories.getCategoryIcon(), holder.ivCatImage);

            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent i = new Intent(context, SportsActivity.class);
//                    i.putExtra("getPos", modelCategories);
//                    context.startActivity(i);
//                    context.startActivity(new Intent(context, SportsActivity.class));
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
        TextView tvCatName;
        LinearLayout llItem;
        ImageView ivCatImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvCatName = (TextView) rootview.findViewById(R.id.tvCatName);
            llItem = (LinearLayout) rootview.findViewById(R.id.llItem);
            //ivCatImage = (ImageView) rootview.findViewById(R.id.ivCategoryImage);


        }
    }
}

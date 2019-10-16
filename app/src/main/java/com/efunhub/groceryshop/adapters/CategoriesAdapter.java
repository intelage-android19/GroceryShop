package com.efunhub.groceryshop.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.model.ModelCategories;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {

    private Context context;

    private List<ModelCategories> modelCategoriesList = new ArrayList<>();
    private View rootview;
    private final int ROW_ITEM = 0, ROW_PROG = 2;

    public CategoriesAdapter(Context applicationContext, List<ModelCategories> modelCategoriesList) {

        this.modelCategoriesList = modelCategoriesList;
        this.context = applicationContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ROW_ITEM) {
            rootview = LayoutInflater.from(context).inflate(R.layout.list_categories, parent, false);
            return new MyViewHolder(rootview);
        } else {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_loading, parent, false);
            return new MyViewHolder(rootview);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final ModelCategories modelCategories = modelCategoriesList.get(position);

        long itemViewType1 = getItemId(position);
        if (itemViewType1 == 0){

            holder.tvVegName.setText(modelCategories.getVegitableName());
            holder.tvVegQty.setText(modelCategories.getVegitableQuantity());
            holder.tvVegPrice.setText(modelCategories.getVegitablePrice());
            holder.tvOfferPrice.setText(modelCategories.getVegitableOfferPrice());

            holder.tvOfferPrice.setPaintFlags(holder.tvOfferPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }
        else if (itemViewType1 == 1){

            holder.tvFruitName.setText(modelCategories.getFruitName());
            holder.tvFruitQty.setText(modelCategories.getFruitQuantity());
            holder.tvFruitPrice.setText(modelCategories.getFruitPrice());
            holder.tvFruitOfferPrice.setText(modelCategories.getFruitOfferPrice());

            holder.tvFruitOfferPrice.setPaintFlags(holder.tvFruitOfferPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }
        else if (itemViewType1 == 2){

            holder.tvGrainsName.setText(modelCategories.getGrainName());
            holder.tvGrainsQty.setText(modelCategories.getGrainQuantity());
            holder.tvGrainsPrice.setText(modelCategories.getGrainPrice());
            holder.tvGrainsOfferPrice.setText(modelCategories.getGrainOfferPrice());

            holder.tvGrainsOfferPrice.setPaintFlags(holder.tvGrainsOfferPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }



        /*int itemViewType = getItemViewType(position);
        if (itemViewType == ROW_ITEM) {
            holder.tvVegName.setText(modelCategories.getFruitName());

            holder.tvVegPrice.setText(modelCategories.getFruitPrice());
            holder.tvVegQty.setText(modelCategories.getFruitQuantity());
            holder.ivVegImage.setImageResource(modelCategories.getFruitImage());
            holder.tvOfferPrice.setText(modelCategories.getFruitOfferPrice());

            holder.tvOfferPrice.setPaintFlags(holder.tvOfferPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
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
*/
    }

    @Override
    public int getItemViewType(int position) {
//        return ROW_ITEM;
        if (modelCategoriesList.get(position) != null) return ROW_ITEM;
        else return ROW_PROG;

    }


    @Override
    public int getItemCount() {
        return modelCategoriesList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvVegName, tvVegPrice, tvVegQty, tvOfferPrice,tvFruitName,tvFruitPrice,tvFruitQty,tvFruitOfferPrice,
                tvGrainsName,tvGrainsPrice,tvGrainsQty,tvGrainsOfferPrice;
        LinearLayout llItem;
        ImageView ivVegImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            llItem = itemView.findViewById(R.id.llItem);

            ivVegImage = itemView.findViewById(R.id.ivVegetableImage);
            tvVegName = itemView.findViewById(R.id.tvVegetableName);
            tvVegPrice = itemView.findViewById(R.id.tvVegetablePrice);
            tvOfferPrice = itemView.findViewById(R.id.tvVegetableOfferPrice);
            tvVegQty = itemView.findViewById(R.id.tvVegetableQty);

            tvFruitName = itemView.findViewById(R.id.tvVegetableName);
            tvFruitPrice = itemView.findViewById(R.id.tvVegetablePrice);
            tvFruitOfferPrice = itemView.findViewById(R.id.tvVegetableOfferPrice);
            tvFruitQty = itemView.findViewById(R.id.tvVegetableQty);

            tvGrainsName = itemView.findViewById(R.id.tvVegetableName);
            tvGrainsPrice = itemView.findViewById(R.id.tvVegetablePrice);
            tvGrainsOfferPrice = itemView.findViewById(R.id.tvVegetableOfferPrice);
            tvGrainsQty = itemView.findViewById(R.id.tvVegetableQty);


        }

    }
}
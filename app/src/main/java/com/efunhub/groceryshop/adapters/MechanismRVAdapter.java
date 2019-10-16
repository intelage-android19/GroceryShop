package com.efunhub.groceryshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.model.ModelMechanism;

import java.util.ArrayList;

public class MechanismRVAdapter extends RecyclerView.Adapter<MechanismRVAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<ModelMechanism> arrayCategories;
    private View rootview;
    private final int ROW_ITEM = 0, ROW_PROG = 2;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    String s1 = null;
    String s2 = "null";
    int[] a = {1, 2, 3};
    int[][] b = {{1, 2, 4,}, {2, 3}};

    private AlertDialog alertDialog;
    //GlideLoader glideLoader;

    public MechanismRVAdapter(Context context, ArrayList<ModelMechanism> arrayCategories, RecyclerView recyclerView) {
        this.context = context;
        this.arrayCategories = arrayCategories;
        recyclerView.scrollToPosition(arrayCategories.size() - 1);
        //this.glideLoader = glideLoader;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ROW_ITEM) {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_mechanism_items, parent, false);
            return new MechanismRVAdapter.MyViewHolder(rootview);
        } else {
            rootview = LayoutInflater.from(context).inflate(R.layout.row_loading, parent, false);
            return new MechanismRVAdapter.MyViewHolder(rootview);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final ModelMechanism modelMechanism = arrayCategories.get(position);

        int itemViewType = getItemViewType(position);
        if (itemViewType == ROW_ITEM) {
            holder.tvMechName.setText(modelMechanism.getMechanismTitle());
            holder.ivMechImage.setImageResource(modelMechanism.getMechanismImage());

            //glideLoader.loadImageSimple(modelCategories.getCategoryIcon(), holder.ivCatImage);

            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ModelMechanism modelMech = arrayCategories.get(position);

                    showDescriptionDialog(modelMech);

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
        TextView tvMechName, tvMechDetails;
        LinearLayout llItem;
        ImageView ivMechImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvMechName = (TextView) rootview.findViewById(R.id.tvMehanismTitle);
            ivMechImage = (ImageView) rootview.findViewById(R.id.ivMechanismImage);
            llItem = (LinearLayout) rootview.findViewById(R.id.llItem);


        }
    }


    private void showDescriptionDialog(ModelMechanism modelMechanism) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater
                = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = layoutInflater.inflate(R.layout.grokisan_experience_dialog, null);
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        ImageView ivExpImage = alertDialog.findViewById(R.id.ivExpImage);
        TextView tvExpName = alertDialog.findViewById(R.id.tvExpName);
        TextView tvExpDesc = alertDialog.findViewById(R.id.tvExpDesc);
        Button close = alertDialog.findViewById(R.id.btnClose);

        tvExpName.setText(modelMechanism.getMechanismTitle().replace("\n", " "));

        tvExpDesc.setText(modelMechanism.getMechanismDescription());

        ivExpImage.setImageResource(modelMechanism.getMechanismImage());


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

}

package com.efunhub.groceryshop.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.interfaces.AddToCartListener;
import com.efunhub.groceryshop.interfaces.RemoveCart;
import com.efunhub.groceryshop.interfaces.UpdateItemPrice;
import com.efunhub.groceryshop.model.CartRVModel;
import com.efunhub.groceryshop.utility.SessionManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private List<CartRVModel> cartProdList;
    private Context mContext;
    private SessionManager sessionManager;
    RemoveCart removeCart;
    AddToCartListener addToCartListener;

    UpdateItemPrice updateItemPrice;

    public CartAdapter(Context mContext, List<CartRVModel> cartProd) {
        this.cartProdList = cartProd;
        this.mContext = mContext;
        this.removeCart = (RemoveCart) mContext;
        this.addToCartListener = (AddToCartListener) mContext;
        this.updateItemPrice = (UpdateItemPrice) mContext;

        sessionManager = new SessionManager(mContext);
    }

    public void setOnDataChangeListener(UpdateItemPrice updateItemPrice) {
        updateItemPrice = updateItemPrice;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View item_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items, viewGroup, false);
        return new MyViewHolder(item_view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int pos) {

        CartRVModel cartProductsArray = cartProdList.get(pos);

        myViewHolder.tvCartProductName.setText(cartProductsArray.getProdName());
        myViewHolder.tvCartProductPrice.setText(mContext.getResources().getString(R.string.currency) + " " + cartProductsArray.getProdPrice());

        String qty = String.valueOf(cartProductsArray.getProdQty());

        myViewHolder.tvCartQty.setText(qty);

        Picasso.with(mContext)
                .load(mContext.getResources().getString(R.string.images_base_url) + cartProductsArray.getImg())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(myViewHolder.ivCartProduct);
        //myViewHolder.ivCartProduct.setImageResource(cartProductsArray.getImg());

    }

    @Override
    public int getItemCount() {
        return cartProdList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCartProduct, ivCartFav;
        TextView tvCartProductName, tvCartProductPrice, tvCartQty, tvSubTotal;
        Button btnDecrease, btnIncrease, btnDeleteProd;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            ivCartProduct = itemView.findViewById(R.id.ivcartProd);
            tvCartProductName = itemView.findViewById(R.id.cartProdName);
            tvCartProductPrice = itemView.findViewById(R.id.cartProdPrice);
            tvCartQty = itemView.findViewById(R.id.tvCartQty);
            btnDeleteProd = itemView.findViewById(R.id.btnDelete);
            btnDecrease = itemView.findViewById(R.id.btnCartDecrease);
            btnIncrease = itemView.findViewById(R.id.btnCartIncrease);

            btnDeleteProd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //CartRVModel cartRVModel = arrayList.get(getAdapterPosition());

                    new AlertDialog.Builder(mContext)
                            .setTitle(mContext.getResources().getString(R.string.app_name))
                            .setMessage("Do you want to remove from cart?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    int pos = getAdapterPosition();
                                    sessionManager.removeProductFromCart(mContext, pos);
                                    cartProdList.remove(pos);
                                    notifyItemRemoved(pos);
                                    notifyItemRangeChanged(pos, cartProdList.size());
                                    notifyDataSetChanged();
                                    removeCart.removeCart();

                                }
                            }).create().show();

                }
            });

            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = getAdapterPosition();
                    CartRVModel cartRVModel = cartProdList.get(pos);
                    sessionManager.updateCart(mContext, 1, cartRVModel);
                    cartRVModel.setProdQty(cartRVModel.getProdQty() + 1);
                    cartRVModel.setSubTotal(String.valueOf(Integer.parseInt(cartRVModel.getProdPrice()) + 1));
                    notifyItemRangeChanged(pos, cartProdList.size());
                    addToCartListener.addToCart();
                    buttonClickActions();

                }
            });

            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    CartRVModel cartRVModel = cartProdList.get(pos);
                    if (cartRVModel.getProdQty() > 0) {
                        sessionManager.updateCart(mContext, 0, cartRVModel);
                        cartRVModel.setProdQty(cartRVModel.getProdQty() - 1);
                        notifyItemRangeChanged(pos, cartProdList.size());
                        addToCartListener.addToCart();
                        buttonClickActions();
                    }

                }
            });

        }

    }


    private void buttonClickActions() {

        sessionManager.getSubTotal(mContext);

        if (updateItemPrice != null) {
            updateItemPrice.onItemCountChanged(cartProdList.size(), sessionManager.getSubTotal(mContext));
        }
    }
}

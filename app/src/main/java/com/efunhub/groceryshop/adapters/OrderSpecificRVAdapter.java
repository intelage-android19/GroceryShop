package com.efunhub.groceryshop.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.interfaces.IResult;
import com.efunhub.groceryshop.model.SpecificOrderRVModel;
import com.efunhub.groceryshop.utility.SessionManager;
import com.efunhub.groceryshop.utility.ToastClass;
import com.efunhub.groceryshop.utility.VolleyService;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.efunhub.groceryshop.utility.SessionManager.KEY_ID;

/**
 * Created by Admin on 24-04-2018.
 */

public class OrderSpecificRVAdapter extends RecyclerView.Adapter<OrderSpecificRVAdapter.ItemViewHolder> {

    private Context mContext;
    private ArrayList<SpecificOrderRVModel> arrayList;

    private AlertDialog alertDialog;

    private VolleyService mVolleyService;
    private IResult mResultCallback;
    private String CancelOrderURl = "cancle_order.php";

    private String uid;
    private SessionManager sessionManager;

    private ToastClass toastClass;

    //get cust id from sharepreference
    private HashMap<String, String> custOrdrsList;

    public OrderSpecificRVAdapter(Context context, ArrayList<SpecificOrderRVModel> specificOrderModelArrayList) {
        this.mContext = context;
        this.arrayList = specificOrderModelArrayList;

        sessionManager = new SessionManager(mContext);
        custOrdrsList = sessionManager.getUserDetails();
        uid = custOrdrsList.get(KEY_ID);
        toastClass = new ToastClass();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.specific_order_list_item, null);

        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final SpecificOrderRVModel specificOrderRVModel = arrayList.get(position);

        holder.tvProductName.setText(specificOrderRVModel.getProductName());
        holder.tvOrderPrice.setText(mContext.getResources().getString(R.string.currency) + specificOrderRVModel.getProductPrice());
        holder.tvOrderQty.setText(specificOrderRVModel.getProductQty());

        Picasso.with(mContext).load(specificOrderRVModel.getImageUrl())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.ivOrderProduct);

        if (specificOrderRVModel.getOrderstatus().equalsIgnoreCase("Delivered")) {
            holder.tvDeliveryDate.setText(specificOrderRVModel.getDeliveryDate());
            holder.llDeliveryStatus.setVisibility(View.VISIBLE);
            holder.btnCancelOrder.setVisibility(View.GONE);
        }

        holder.btnTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater layoutInflater
                        = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = layoutInflater.inflate(R.layout.track_order_dialog, null);
                dialogBuilder.setView(dialogView);

                alertDialog = dialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

                ImageView close = alertDialog.findViewById(R.id.iv_close);
                LinearLayout llOrderStep2, llOrderStep3;
                View viewStep1, viewStep2;
                TextView tvOrderStep2, tvOrderStep3, tvOrderDispatched, tvOrderDelivered;

                llOrderStep2 = alertDialog.findViewById(R.id.llOrderStep2);
                llOrderStep3 = alertDialog.findViewById(R.id.llOrderStep3);

                viewStep1 = alertDialog.findViewById(R.id.viewStep1);
                viewStep2 = alertDialog.findViewById(R.id.viewStep2);

                tvOrderStep2 = alertDialog.findViewById(R.id.tvOrderStep2);
                tvOrderStep3 = alertDialog.findViewById(R.id.tvOrderStep3);

                tvOrderDispatched = alertDialog.findViewById(R.id.tvOrderDispatched);
                tvOrderDelivered = alertDialog.findViewById(R.id.tvOrderDelivered);


                if (specificOrderRVModel.getOrderstatus().equalsIgnoreCase("Dispatched")) {

                    viewStep1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    llOrderStep2.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_text_selected));
                    tvOrderStep2.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite));
                    tvOrderDispatched.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));

                } else if (specificOrderRVModel.getOrderstatus().equalsIgnoreCase("Delivered")) {

                    viewStep2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    llOrderStep3.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_text_selected));
                    tvOrderStep3.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite));
                    tvOrderDelivered.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));

                    viewStep1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    llOrderStep2.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_text_selected));
                    tvOrderStep2.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite));
                    tvOrderDispatched.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                }

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ivOrderProduct;
        TextView tvShopName, tvProductName, tvOrderPrice, tvDeliveryDate, tvOrderQty;
        LinearLayout llDeliveryStatus;
        TextView btnTrackOrder, btnCancelOrder;

        ItemViewHolder(View itemView) {
            super(itemView);

            llDeliveryStatus = itemView.findViewById(R.id.llDeliveryStatus);
            ivOrderProduct = itemView.findViewById(R.id.ivOrderProduct);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            tvOrderQty = itemView.findViewById(R.id.tvOrderQty);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvOrderPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvDeliveryDate = itemView.findViewById(R.id.tvDeliveryDate);
            btnTrackOrder = itemView.findViewById(R.id.btnTrackOrder);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);

            btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(mContext)
                            .setTitle(mContext.getResources().getString(R.string.app_name))
                            .setMessage("Do you want to cancel the order?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    cancelOrder();
                                }
                            }).create().show();
                }
            });
        }

        private void cancelOrder() {
            initVolleyCallback();
            mVolleyService = new VolleyService(mResultCallback, mContext);
            SpecificOrderRVModel specificOrderRVModel = arrayList.get(getAdapterPosition());

            String now = new SimpleDateFormat("hh:mm:ss aa").format(new java.util.Date().getTime());
            SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm:ss aa");
            SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm:ss");
            String time24 = null;
            try {
                time24 = outFormat.format(inFormat.parse(now));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Map<String, String> params = new HashMap<>();
            params.put("oid", specificOrderRVModel.getoId());
            params.put("pid", specificOrderRVModel.getpId());
            params.put("uid", uid);
            params.put("ctime", time24);

            mVolleyService.postDataVolleyParameters(101, mContext.getString(R.string.base_url) + CancelOrderURl, params);
        }

        private void initVolleyCallback() {
            mResultCallback = new IResult() {
                @Override
                public void notifySuccess(int requestId, String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");

                        if (Integer.parseInt(status) == 1) {
                            toastClass.makeToast(mContext, "Successfully order cancelled");

                            int pos = getAdapterPosition();

                            arrayList.remove(pos);
                            notifyItemRemoved(pos);
                            notifyItemRangeChanged(pos, arrayList.size());
                            notifyDataSetChanged();

                            Intent intent = new Intent();
                            intent.setAction("refresh_order_list");
                            mContext.sendBroadcast(intent);

                        } else if (Integer.parseInt(status) == 2) {
                            toastClass.makeToast(mContext, "Can't cancel order");
                        } else {
                            toastClass.makeToast(mContext, "Something went wrong try again");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(int requestId, VolleyError error) {
                    Log.v("Volley requester ", String.valueOf(requestId));
                    Log.v("Volley JSON post", String.valueOf(error));
                }
            };
        }
    }
}

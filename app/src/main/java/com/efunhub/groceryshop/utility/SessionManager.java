package com.efunhub.groceryshop.utility;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.efunhub.groceryshop.activities.LoginActivity;
import com.efunhub.groceryshop.activities.MainActivity;
import com.efunhub.groceryshop.model.CartRVModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 30-10-2017.
 */

public class SessionManager {

    private static final String ADD_TO_CART = "AddToCart";
    private static final String ADD_TO_WISHLIST = "AddToWishlist";

    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Context
    private Context mContext;

    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "LearnEarnPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    //FCM Token
    public static final String KEY_FCM_TOKEN = "token";

    // ID (make variable public to access from outside)
    public static final String KEY_ID = "uid";


    public static final String KEY_POSTAL_CODE = "postal_code";

    //Referrer ID
    public static final String REFERRER_ID = "referrer_id";

    // Constructor
    public SessionManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String id) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing ID in pref
        editor.putString(KEY_ID, id);

        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        // user ID
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        return user;
    }


    /**
     * Create postal code session
     */
    public void createPostalCodeSession(String postalCode) {

          // Storing ID in pref
        editor.putString(KEY_POSTAL_CODE, postalCode);

        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserPostal() {
        HashMap<String, String> userPostalCode = new HashMap<String, String>();

        // user userPostalCode
        userPostalCode.put(KEY_POSTAL_CODE, pref.getString(KEY_POSTAL_CODE, null));
        return userPostalCode;
    }

    public void storeRegIdInPref(String token) {
        editor.putString(KEY_FCM_TOKEN, token);
        editor.commit();
    }

    private void saveAddToCart(Context context, List<CartRVModel> favorites) {


        Gson gson = new Gson();

        String jsonFavorites = gson.toJson(favorites);

        editor.putString(ADD_TO_CART, jsonFavorites);

        editor.commit();
    }

/*
    private void saveToWishlist(Context context, List<WishlistRVModel> favorites) {


        Gson gson = new Gson();

        String jsonFavorites = gson.toJson(favorites);

        editor.putString(ADD_TO_WISHLIST, jsonFavorites);

        editor.commit();
    }
*/


    // Add items to cart
    public void addToCart(Context context, CartRVModel cartRVModel) {

        boolean isValid = false;

        ArrayList<CartRVModel> cartProdArray = getAddToCartList(context);
        if (cartProdArray == null)
            cartProdArray = new ArrayList<CartRVModel>();

        for (int i = 0; i < cartProdArray.size(); i++) {

            CartRVModel rvModel = cartProdArray.get(i);

            if (rvModel.getProductID() != null && rvModel.getProductID().equals(cartRVModel.getProductID())) {
                Toast.makeText(context, "Already added in cart", Toast.LENGTH_SHORT).show();
                isValid = true;
            } else {
                Toast.makeText(context, "Added in cart", Toast.LENGTH_SHORT).show();

            }
        }
        if (!isValid) {

            cartProdArray.add(cartRVModel);
            saveAddToCart(context, cartProdArray);
        }
    }


    //Remove items from cart
    public void removeProductFromCart(Context context, int position) {
        ArrayList<CartRVModel> cartProdArray = getAddToCartList(context);

        if (cartProdArray != null) {
            cartProdArray.remove(position);
            saveAddToCart(context, cartProdArray);
        }
    }


    //update cart product quantity
    public void updateCart(Context context, int value, CartRVModel cartRVModel) {

        ArrayList<CartRVModel> cartRVModelArrayList = getAddToCartList(context);

        for (int i = 0; i < cartRVModelArrayList.size(); i++) {

            CartRVModel rvModel = cartRVModelArrayList.get(i);
            if (value == 1) {
                if (rvModel.getProductID() != null && rvModel.getProductID().equals(cartRVModel.getProductID())) {
                    //String val = String.valueOf(rvModel.getProdQty());

                    int val = rvModel.getProdQty();

                    rvModel.setProdQty(val + 1);

                    String price = rvModel.getSubTotal();

                    // rvModel.setSubTotal(String.valueOf(Integer.parseInt(price) * Integer.parseInt(val)));

                }
            } else {
                if (rvModel.getProductID() != null && rvModel.getProductID().equals(cartRVModel.getProductID())) {
                    int val = rvModel.getProdQty();
                    int total;

                    rvModel.setProdQty(val - 1);
                    String price = rvModel.getSubTotal();
                    //rvModel.setSubTotal(String.valueOf(Integer.parseInt(price) - 1));
                }
            }
        }

        saveAddToCart(context, cartRVModelArrayList);

    }

    //get Subtotal amount
    public Integer getSubTotal(Context context) {
        int subtotal = 0;
        ArrayList<CartRVModel> cartRVModelArrayList = getAddToCartList(context);

        if (cartRVModelArrayList != null) {
            for (int i = 0; i < cartRVModelArrayList.size(); i++) {
                CartRVModel rvModel = cartRVModelArrayList.get(i);
                subtotal += rvModel.getProdQty() * Integer.parseInt(rvModel.getProdPrice());
            }
        }
        return subtotal;
    }

    //delete cart list after making giving order
    public void deleteCartList() {
        pref.edit().remove(ADD_TO_CART).commit();
    }

    public void deleteWishList() {
        pref.edit().remove(ADD_TO_WISHLIST).commit();
    }


    //get cart product list
    public ArrayList<CartRVModel> getAddToCartList(Context context) {
        List<CartRVModel> cartModelList;

        if (pref.contains(ADD_TO_CART)) {
            String jsonAddToCart = pref.getString(ADD_TO_CART, null);
            Gson gson = new Gson();
            CartRVModel[] cartProdArray = gson.fromJson(jsonAddToCart, CartRVModel[].class);

            cartModelList = Arrays.asList(cartProdArray);
            cartModelList = new ArrayList<CartRVModel>(cartModelList);
        } else
            return null;

        return (ArrayList<CartRVModel>) cartModelList;
    }


    public HashMap<String, String> getRegIdPref() {
        HashMap<String, String> token = new HashMap<String, String>();

        // token
        token.put(KEY_FCM_TOKEN, pref.getString(KEY_FCM_TOKEN, null));

        return token;
    }

    /*To store referrer id*/
    public void storeReferrerIdInPref(String refId) {
        editor.putString(REFERRER_ID, refId);
        editor.commit();
    }

    /*To get referrer id while registering new user*/
    public HashMap<String, String> getReferIdPref() {
        HashMap<String, String> referrId = new HashMap<String, String>();

        // referrId
        referrId.put(REFERRER_ID, pref.getString(REFERRER_ID, null));

        return referrId;
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (this.isLoggedIn()) {

            // user is logged in redirect him to Main Activity
            Intent i = new Intent(mContext, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            mContext.startActivity(i);

        } else {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(mContext, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            mContext.startActivity(i);
        }

    }



    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.putBoolean(IS_LOGIN, false);
        editor.remove(KEY_ID);
        editor.commit();

        /*

        // After logout redirect user to Login Activity
        Intent i = new Intent(mContext, RegistrationActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        // Staring Login Activity
        mContext.startActivity(i);*/
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}

package com.efunhub.groceryshop.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.activities.MainActivity;
import com.efunhub.groceryshop.utility.CustomProgressDialog;
import com.efunhub.groceryshop.utility.SessionManager;
import com.efunhub.groceryshop.widget.CustomTextView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


/**
 * Each Fragment must extends BaseFragment.
 * Also each fragment must used the mainView already declared in BaseFragment for Container View.
 */
public class BaseFragment extends Fragment {

    private ImageView iv_edit;
    protected Context mContext;
    //protected GlideLoader glideLoader;
    private CustomProgressDialog progressDialog;
    public Activity mActivity;
    private CustomTextView title;

    // Preference Handling
    protected SessionManager session;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        //glideLoader = new GlideLoader(mContext);
        if (context instanceof Activity) {
            mActivity = (Activity) context;

        }
        session = new SessionManager(mContext);
    }


    /**
     * Show Progress Dailog without any message
     */
    public void showProgress() {
        showProgress(null);
    }

    /**
     * Show Progress dialog with Message
     *
     * @param message Message to show under Progress
     */
    public void showProgress(String message) {
        if (progressDialog != null) {
            progressDialog.show(message);
        } else {
            progressDialog = new CustomProgressDialog(mContext, message);
            progressDialog.show();
        }
    }


    /**
     * Stop Progress if running and dismiss progress Dialog
     */
    public void stopProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * To check weather the progress is running
     *
     * @return True if Progress is running false else.
     */
    protected boolean isProgressShowing() {
        return progressDialog != null && progressDialog.isShowing();
    }


    public void showToastShort(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(mActivity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(mActivity.findViewById(android.R.id.content).getContext(), R.color.colorPrimaryDark));
        snackbar.show();
    }

    public void showErrorSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(mActivity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(mActivity.findViewById(android.R.id.content).getContext(), R.color.colorPrimaryDark));
        snackbar.show();
    }

    public void errorHandleFromApi(final Context mContext, final ArrayList<String> messages) {
        if (messages.size() == 0) {
            showErrorSnackBar(messages.get(0));
        } else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
            //Set title
//            dialogBuilder.setTitle(R.string.errors);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice);
            arrayAdapter.addAll(messages);

            dialogBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position) {
                    String strName = messages.get(position);
                    AlertDialog.Builder dialogBuilderInner = new AlertDialog.Builder(mContext);
                    dialogBuilderInner.setMessage(strName);
                    dialogBuilderInner.setPositiveButton(R.string.str_Ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialogBuilderInner.show();
                }
            });
            dialogBuilder.show();
        }
    }

    public void setupToolBarWithMenu(Toolbar toolbar, @Nullable String Title) {
        ((MainActivity) mActivity).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity) mActivity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        ImageView ivMenu = (ImageView) toolbar.findViewById(R.id.icMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mActivity).toggleDrawer();
            }
        });
        title = toolbar.findViewById(R.id.title);
        title.setText(Title != null ? Title : "");
    }

  /*  public void setupToolBarWithBackArrow(Toolbar toolbar, @Nullable String Title) {
        ((MainActivity) mActivity).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity) mActivity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.onBackPressed();
            }
        });
        title = toolbar.findViewById(R.id.title);
        title.setText(Title != null ? Title : "");
    }
  */  public void setUpToolbarWithBackArrow(Toolbar toolbar, String strTitle, boolean isBackArrow, final Context context) {
        ((MainActivity) mActivity).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity) mActivity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(isBackArrow);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mActivity.onBackPressed();

                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
            ImageView ivBack = (ImageView) toolbar.findViewById(R.id.icMenu);
            ivBack.setImageResource(R.drawable.ic_back_arrow);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager().popBackStack();

                    //getActivity().onBackPressed();

                }
            });

            title = toolbar.findViewById(R.id.title);
            title.setText(strTitle);
        }
    }



    public void setUpToolbarWithoutTitle(Toolbar toolbar, boolean status) {

        ((MainActivity) mActivity).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity) mActivity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        ImageView ivMenu = (ImageView) toolbar.findViewById(R.id.icMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mActivity).toggleDrawer();
            }
        });

    }

}


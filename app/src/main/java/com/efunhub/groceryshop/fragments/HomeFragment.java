package com.efunhub.groceryshop.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.activities.CategoriesActivity;
import com.efunhub.groceryshop.activities.SearchItemsActivity;
import com.efunhub.groceryshop.adapters.BestsellingRVAdapter;
import com.efunhub.groceryshop.adapters.FruitRVAdapter;
import com.efunhub.groceryshop.adapters.GrainsRVAdapter;
import com.efunhub.groceryshop.adapters.MechanismRVAdapter;
import com.efunhub.groceryshop.adapters.SearchAdapter;
import com.efunhub.groceryshop.adapters.SlidingImagesAdapter;
import com.efunhub.groceryshop.adapters.SubCategoriesRVAdapter;
import com.efunhub.groceryshop.adapters.VegetablesRVAdapter;
import com.efunhub.groceryshop.databinding.FragmentHomeBinding;
import com.efunhub.groceryshop.interfaces.IResult;
import com.efunhub.groceryshop.model.AllDataBaseModelClass;
import com.efunhub.groceryshop.model.AllImageBanerbaseClass;
import com.efunhub.groceryshop.model.AllMainCategoryBaseClass;
import com.efunhub.groceryshop.model.AllSearchItemsModelBaseClass;
import com.efunhub.groceryshop.model.AllSubCategoryBaseClass;
import com.efunhub.groceryshop.model.ModelAllSearchItem;
import com.efunhub.groceryshop.model.ModelBestselling;
import com.efunhub.groceryshop.model.ModelFruits;
import com.efunhub.groceryshop.model.ModelGrains;
import com.efunhub.groceryshop.model.ModelImageSlider;
import com.efunhub.groceryshop.model.ModelMainCategory;
import com.efunhub.groceryshop.model.ModelMechanism;
import com.efunhub.groceryshop.model.ModelSubCategory;
import com.efunhub.groceryshop.model.ModelVegetables;
import com.efunhub.groceryshop.utility.SessionManager;
import com.efunhub.groceryshop.utility.SingletoneLocationProvider;
import com.efunhub.groceryshop.utility.VolleyService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.efunhub.groceryshop.utility.ConstantVariables.BANER_IMAGE;
import static com.efunhub.groceryshop.utility.ConstantVariables.RETRIVE_ALL_CATEGORY;
import static com.efunhub.groceryshop.utility.ConstantVariables.RETRIVE_ALL_SEARCH_DATA;
import static com.efunhub.groceryshop.utility.ConstantVariables.RETRIVE_ALL_SUB_CATEGORY;
import static com.efunhub.groceryshop.utility.ConstantVariables.RETRIVE_All_HOME_PAGE_DATA;
import static com.efunhub.groceryshop.utility.ConstantVariables.RETRIVE_SPLASH_IMAGEY;


public class HomeFragment extends BaseFragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    RecyclerView rvCategories;
    private FragmentHomeBinding mBinder;

    private String banerUrl = "Slider";
    private String homePagaeDataUrl = "homepage.php";
    private String allCategoriesUrl = "Show-Main-Category";
    private String allSubCategory = "subcategory.php";
    private String homeScreenSplashImgUrl = "splashs.php";
    private String allDataSearchUrl = "search.php";

    private IResult mResultCallback;
    private VolleyService mVolleyService;

    private AllImageBanerbaseClass allImageBanerbaseClass;
    List<ModelImageSlider> allImagesList = new ArrayList<>();

    private AllDataBaseModelClass allDataBaseModelClass;
    private AllMainCategoryBaseClass allMainCategoryBaseClass;
    private AllSubCategoryBaseClass allSubCategoryBaseClass;

    private SubCategoriesRVAdapter mSubRVAdapter;
    private List<ModelSubCategory> subCategoriesArrayList = new ArrayList<>();

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ModelImageSlider> imageModelArrayList;


    private static String TAG = "Home Fragment";


    private VegetablesRVAdapter mVegetablesRVAdapter;
    private List<ModelVegetables> modelVegetablesList = new ArrayList<>();

    private BestsellingRVAdapter mBestsellingRVAdapter;
    private List<ModelBestselling> modelBestsellingList = new ArrayList<>();

    private FruitRVAdapter mFruitsRVAdapter;
    private List<ModelFruits> modelFruitsList = new ArrayList<>();

    private List<ModelGrains> modelGrainsList = new ArrayList<>();
    GrainsRVAdapter grainsRVAdapter;

    private MechanismRVAdapter mMechanismRVAdapter;
    private ArrayList<ModelMechanism> arrayListmechanism = new ArrayList<>();


    private List<String> spnCategoriesList = new ArrayList<>();
    private AllSearchItemsModelBaseClass allSearchItemBaseModelClass;
    private String[] mechTitle = new String[]{"Lowest Cost\n Than Market", "All Year-round\n Free Delivery", "100 % Assured\nQuality", "All Year-round\n Food\n recommendation\n engine"};
    private int[] mechImage = new int[]{R.drawable.ic_low_cost, R.drawable.ic_free_deliver, R.drawable.ic_qc_assurance, R.drawable.ic_rec_engine};

    private String[] mechDescription;

    protected double latitude, longitude;
    private SearchAdapter searchAdapter;

    ArrayList<Geofence> mGeofenceList;
    private GoogleApiClient googleApiClient;
    private String userCurrentLocation = "Waiting for Location";


    private SessionManager sessionManager;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


    private FusedLocationProviderClient fusedLocationClient;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {

        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment

        mBinder = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        setUpToolbarWithoutTitle(mBinder.toolbar.toolbar, true);

        init();

        getBanerImages();
        getAllCategory();

/*
        getAllCategory();
        getAllSubCategory();
        getAllData();
        getSplashImage();
        mechanismUi();
        getAllSearchData();*/

        mBinder.btnViewMoreBestSellingItem.setOnClickListener(this);
        mBinder.btnViewMoreVegItems.setOnClickListener(this);
        mBinder.btnViewMoreFruitsItems.setOnClickListener(this);
        mBinder.btnViewMoreGrainsItems.setOnClickListener(this);

        mBinder.etSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ModelAllSearchItem modelAllSearchItem = (ModelAllSearchItem) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), SearchItemsActivity.class);
                intent.putExtra("pmid", modelAllSearchItem.getPmid());
                getActivity().startActivity(intent);

            }
        });

        return mBinder.getRoot();

    }

    private void init() {

        sessionManager = new SessionManager(getActivity());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        //Initializing googleApiClient
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (googleApiClient != null) {

            googleApiClient.connect();
        }

        mBinder.nsHomePage.setVisibility(View.GONE);
       mBinder.shimmerViewContainer.startShimmer();

        imageModelArrayList = new ArrayList<>();
        //imageModelArrayList = populateList();

        mechDescription = new String[]{this.getResources().getString(R.string.str_fresh_mech),
                this.getResources().getString(R.string.str_free_delivery),
                this.getResources().getString(R.string.str_qc_assu),
                this.getResources().getString(R.string.str_rec_engine)};


        mBinder.spnCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Vegetables")) {
                    Intent intent = new Intent(getContext(), CategoriesActivity.class);
                    intent.putExtra("catId", "1");
                    intent.putExtra("vegetables", (Serializable) modelVegetablesList);
                    startActivity(intent);


                    // do your stuff
                } else if (selectedItem.equals("Fruits")) {
                    Intent intent = new Intent(getContext(), CategoriesActivity.class);
                    intent.putExtra("catId", "2");
                    intent.putExtra("fruits", (Serializable) modelFruitsList);
                    startActivity(intent);

                } else if (selectedItem.equals("Grains")) {
                    Intent intent = new Intent(getContext(), CategoriesActivity.class);
                    intent.putExtra("catId", "3");
                    intent.putExtra("grains", (Serializable) modelGrainsList);
                    startActivity(intent);

                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void getAllCategory() {
        mBinder.homePgBar.setVisibility(View.GONE);

        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, getContext());
        mVolleyService.postDataVolley(RETRIVE_ALL_CATEGORY,
                this.getResources().getString(R.string.base_url) + allCategoriesUrl);
    }

    private void getAllSubCategory() {
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, getContext());
        mVolleyService.postDataVolley(RETRIVE_ALL_SUB_CATEGORY,
                this.getResources().getString(R.string.base_url) + allSubCategory);
    }

    private void getBanerImages() {

        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, getContext());
        mVolleyService.getDataVolley(BANER_IMAGE,
                this.getResources().getString(R.string.base_url) + banerUrl);
    }

    private void getAllData() {
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, getContext());
        mVolleyService.postDataVolley(RETRIVE_All_HOME_PAGE_DATA,
                this.getResources().getString(R.string.base_url) + homePagaeDataUrl);
    }

    private void getSplashImage() {
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, getContext());
        mVolleyService.postDataVolley(RETRIVE_SPLASH_IMAGEY,
                this.getResources().getString(R.string.base_url) + homeScreenSplashImgUrl);
    }

    private void getAllSearchData() {
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback, getContext());
        mVolleyService.postDataVolley(RETRIVE_ALL_SEARCH_DATA,
                this.getResources().getString(R.string.base_url) + allDataSearchUrl);
    }


    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {

                switch (requestId) {

                    case BANER_IMAGE:

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 1) {


                                JSONArray jsonArray  = jsonObject.getJSONArray("allslider");

                                Gson gson = new Gson();

                                allImageBanerbaseClass = gson.fromJson(
                                        response, AllImageBanerbaseClass.class);

                                allImagesList = allImageBanerbaseClass.getAllslider();
                                setUpImageSlider();

                            }


                        } catch (JSONException e) {
                            System.out.println(e);
                            e.printStackTrace();
                        }
                        mBinder.shimmerViewContainer.stopShimmer();
                        mBinder.shimmerViewContainer.setVisibility(View.GONE);
                        mBinder.homePgBar.setVisibility(View.GONE);
                        mBinder.nsHomePage.setVisibility(View.VISIBLE);

                        break;


                    case RETRIVE_All_HOME_PAGE_DATA:

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 1) {

                                //JSONObject image = jsonObject.getJSONObject("allscreenshortimages");
                                Gson gson = new Gson();
                                allDataBaseModelClass = gson.fromJson(
                                        response, AllDataBaseModelClass.class);

                                modelBestsellingList = allDataBaseModelClass.getAllbestitems();
                                modelVegetablesList = allDataBaseModelClass.getAllvegetables();
                                modelFruitsList = allDataBaseModelClass.getAllfruits();
                                modelGrainsList = allDataBaseModelClass.getAllgrains();

                                bestsellingUi();
                                vegetableUi();
                                fruitsUi();
                                grainsUi();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        break;

                    case RETRIVE_ALL_CATEGORY:

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 1) {

                                JSONArray jsonArray = new JSONArray("allmaincategory");

                                JSONObject object = jsonArray.getJSONObject(getId());

                                Gson gson = new Gson();

                                allMainCategoryBaseClass = gson.fromJson(
                                        response, AllMainCategoryBaseClass.class);

                                List<ModelMainCategory> modelMainCategoryList = allMainCategoryBaseClass.getAllmaincategory();

                                //String[] catname = new String[]{"Categories", "Vegetables", "Fruits", "Grains"};

                                spnCategoriesList.add("Category");

                                for (ModelMainCategory mainCategory : modelMainCategoryList) {

                                    spnCategoriesList.add(mainCategory.getMainCategoryEname());

                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.categories_spinner_items, spnCategoriesList);

                                mBinder.spnCategories.setAdapter(adapter);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;


                    case RETRIVE_ALL_SUB_CATEGORY:

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 1) {

                                //JSONObject image = jsonObject.getJSONObject("allscreenshortimages");
                                Gson gson = new Gson();
                                allSubCategoryBaseClass = gson.fromJson(
                                        response, AllSubCategoryBaseClass.class);

                                subCategoriesArrayList = allSubCategoryBaseClass.getAllsubcategory();
                                setUpSubCategoryUi();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                      /*  mBinder.shimmerViewContainer.stopShimmer();
                        mBinder.shimmerViewContainer.setVisibility(View.GONE);*/
                        mBinder.homePgBar.setVisibility(View.GONE);
                        mBinder.nsHomePage.setVisibility(View.VISIBLE);

                        break;


                    case RETRIVE_SPLASH_IMAGEY:

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 1) {

                                JSONObject object = jsonObject.getJSONObject("images");

                                Picasso.with(getActivity())
                                        .load(object.getString("logo"))//getResources().getString(R.string.base_url) +
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .networkPolicy(NetworkPolicy.NO_CACHE)
                                        .into(mBinder.ivBaner);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;


                    case RETRIVE_ALL_SEARCH_DATA:

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 1) {

                                Gson gson = new Gson();
                                allSearchItemBaseModelClass = gson.fromJson(
                                        response, AllSearchItemsModelBaseClass.class);

                                List<ModelAllSearchItem> modelAllSearchItemList = allSearchItemBaseModelClass.getAllitems();

                                List<String> lstItems = new ArrayList<>();


                                for (ModelAllSearchItem modelAllSearchItem : modelAllSearchItemList) {

                                    lstItems.add(modelAllSearchItem.getMainCategoryEname());
                                }

                                searchAdapter = new SearchAdapter(getActivity(), R.layout.categories_spinner_items, modelAllSearchItemList);
                                mBinder.etSearch.setAdapter(searchAdapter);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                }


            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                Log.v("Volley requester ", String.valueOf(requestId));
                Log.v("Volley JSON post", String.valueOf(error));
            }
        };
    }

    private void setUpImageSlider() {

        mBinder.pager.setAdapter(new SlidingImagesAdapter(getActivity(), allImagesList));

        mBinder.indicator.setViewPager(mBinder.pager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        mBinder.indicator.setRadius(5 * density);

        NUM_PAGES = allImagesList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();

        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mBinder.pager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);

        // Pager listener over indicator
        mBinder.indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private void bestsellingUi() {

        LinearLayoutManager linearLayoutManagerVegetables = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);

        linearLayoutManagerVegetables.setStackFromEnd(true);
        linearLayoutManagerVegetables.setReverseLayout(true);
        mBinder.rvBestsellingItems.setLayoutManager(linearLayoutManagerVegetables);
        mBinder.rvBestsellingItems.setItemViewCacheSize(20);
        mBinder.rvBestsellingItems.setDrawingCacheEnabled(true);
        mBinder.rvBestsellingItems.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mBestsellingRVAdapter = new BestsellingRVAdapter(getActivity(), modelBestsellingList,
                mBinder.rvBestsellingItems);
        mBinder.rvBestsellingItems.setAdapter(mBestsellingRVAdapter);

    }

    private void vegetableUi() {


        LinearLayoutManager linearLayoutManagerVegetables = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerVegetables.setStackFromEnd(true);
        linearLayoutManagerVegetables.setReverseLayout(true);
        mBinder.rvVegetableItems.setLayoutManager(linearLayoutManagerVegetables);
        mBinder.rvVegetableItems.setItemViewCacheSize(20);
        mBinder.rvVegetableItems.setDrawingCacheEnabled(true);
        mBinder.rvVegetableItems.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mVegetablesRVAdapter = new VegetablesRVAdapter(getActivity(), modelVegetablesList, mBinder.rvVegetableItems);
        mBinder.rvVegetableItems.setAdapter(mVegetablesRVAdapter);
    }

    private void fruitsUi() {

        LinearLayoutManager linearLayoutManagerVegetables = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);

        linearLayoutManagerVegetables.setStackFromEnd(true);
        linearLayoutManagerVegetables.setReverseLayout(true);
        mBinder.rvFruitItems.setLayoutManager(linearLayoutManagerVegetables);
        mBinder.rvFruitItems.setItemViewCacheSize(20);
        mBinder.rvFruitItems.setDrawingCacheEnabled(true);
        mBinder.rvFruitItems.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //mBinder.rvBestsellingItems.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL));
        mFruitsRVAdapter = new FruitRVAdapter(getActivity(), modelFruitsList,
                mBinder.rvFruitItems);
        mBinder.rvFruitItems.setAdapter(mFruitsRVAdapter);

    }


    private void grainsUi() {

        LinearLayoutManager linearLayoutManagerVegetables = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerVegetables.setStackFromEnd(true);
        linearLayoutManagerVegetables.setReverseLayout(true);

        mBinder.rvGrainsItems.setItemViewCacheSize(20);
        mBinder.rvGrainsItems.setDrawingCacheEnabled(true);
        mBinder.rvGrainsItems.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mBinder.rvGrainsItems.setLayoutManager(linearLayoutManagerVegetables);
        //mVegetablesRVAdapter = new VegetablesRVAdapter(getActivity(), arrayListvegetables, mBinder.rvVegetableItems);
        grainsRVAdapter = new GrainsRVAdapter(getActivity(), modelGrainsList);
        mBinder.rvGrainsItems.setAdapter(grainsRVAdapter);


    }

    private void setUpSubCategoryUi() {


        LinearLayoutManager linearLayoutManagerVegetables = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerVegetables.setStackFromEnd(true);
        linearLayoutManagerVegetables.setReverseLayout(true);

        mBinder.rvExotic.setLayoutManager(linearLayoutManagerVegetables);
        mBinder.rvExotic.setItemViewCacheSize(20);
        mBinder.rvExotic.setDrawingCacheEnabled(true);
        mBinder.rvExotic.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mSubRVAdapter = new SubCategoriesRVAdapter(getActivity(), subCategoriesArrayList,
                mBinder.rvExotic);
        mBinder.rvExotic.setAdapter(mSubRVAdapter);
    }


/*
    private void mechanismUi() {

        for (int i = 0; i < mechTitle.length; i++) {
            ModelMechanism modelMechanism = new ModelMechanism();

            modelMechanism.setMechanismImage(mechImage[i]);
            modelMechanism.setMechanismTitle(mechTitle[i]);
            modelMechanism.setMechanismDescription(mechDescription[i]);

            arrayListmechanism.add(modelMechanism);

        }
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1,
                GridLayoutManager.HORIZONTAL, false);

        mBinder.rvMechanismItems.setLayoutManager(mLayoutManager);
        mBinder.rvMechanismItems.setItemViewCacheSize(20);
        mBinder.rvMechanismItems.setDrawingCacheEnabled(true);
        mBinder.rvMechanismItems.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mMechanismRVAdapter = new MechanismRVAdapter(getActivity(), arrayListmechanism,
                mBinder.rvMechanismItems);
        mBinder.rvMechanismItems.setAdapter(mMechanismRVAdapter);

    }
*/

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnViewMoreBestSellingItem:
                Intent intentBestSellItems = new Intent(getContext(), CategoriesActivity.class);
                intentBestSellItems.putExtra("catId", "0");
                intentBestSellItems.putExtra("bestsellitems", (Serializable) modelBestsellingList);
                startActivity(intentBestSellItems);
                break;

            case R.id.btnViewMoreVegItems:
                Intent intentVeg = new Intent(getContext(), CategoriesActivity.class);
                intentVeg.putExtra("catId", "1");
                intentVeg.putExtra("vegetables", (Serializable) modelVegetablesList);
                startActivity(intentVeg);

                break;

            case R.id.btnViewMoreFruitsItems:
                Intent intentFruits = new Intent(getContext(), CategoriesActivity.class);
                intentFruits.putExtra("catId", "2");
                intentFruits.putExtra("fruits", (Serializable) modelFruitsList);
                startActivity(intentFruits);

                break;

            case R.id.btnViewMoreGrainsItems:
                Intent intentGrains = new Intent(getContext(), CategoriesActivity.class);
                intentGrains.putExtra("catId", "3");
                intentGrains.putExtra("grains", (Serializable) modelGrainsList);
                startActivity(intentGrains);
                break;


        }

    }

    //Getting current location
    private void getCurrentLocation() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (location != null) {
                //Getting longitude and latitude
                longitude = location.getLongitude();

                latitude = location.getLatitude();

                try {

                    Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());

                    List<Address> addresses = geo.getFromLocation(longitude, latitude, 1);

                    if (addresses.isEmpty()) {

                        userCurrentLocation = "Waiting for Location";

                    } else {


                        if (addresses.size() > 0) {

                            userCurrentLocation = addresses.get(0).getFeatureName() + ", " +
                                    addresses.get(0).getLocality() + ", "
                                    + addresses.get(0).getAdminArea();

                            mBinder.toolbar.tvLocation.setText(userCurrentLocation);

                            sessionManager.createPostalCodeSession(addresses.get(0).getPostalCode());

                        }

                    }
                } catch (Exception ex) {


                    Log.e("MainActivity", "onLocationChanged: ", ex);

                }

            }
        } else {

            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showEnableLocationDialog();
            } else {
                SingletoneLocationProvider.requestSingleUpdate(getActivity(),
                        new SingletoneLocationProvider.LocationCallback() {
                            @Override
                            public void onNewLocationAvailable(SingletoneLocationProvider.GPSCoordinates location) {
                                latitude = location.latitude;
                                longitude = location.longitude;

                                Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());

                                List<Address> addresses = null;
                                try {
                                    addresses = geo.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (addresses.isEmpty()) {
                                    userCurrentLocation = "Waiting for Location";

                                } else {

                                    if (addresses.size() > 0) {
                                        userCurrentLocation = addresses.get(0).getFeatureName() + ", " +
                                                addresses.get(0).getLocality() + ", "
                                                + addresses.get(0).getAdminArea();

                                        mBinder.toolbar.tvLocation.setText(userCurrentLocation);

                                        sessionManager.createPostalCodeSession(addresses.get(0).getPostalCode());
                                    }
                                }
                            }
                        });
            }


        }
    }

    public void showEnableLocationDialog() {
        final android.app.AlertDialog.Builder builderDialog = new android.app.AlertDialog.Builder(getActivity());
        builderDialog.setTitle("Enable Location");
        builderDialog.setMessage("Please enable location for near by search");
        builderDialog.setPositiveButton("Enable",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });
        builderDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //fetchImages();
                    }
                });
        android.app.AlertDialog alert = builderDialog.create();
        alert.show();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

      //  getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /*LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerNetwork);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerGps);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };*/
}

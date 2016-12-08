package com.codeandcoder.finalguide.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;

import com.codeandcoder.finalguide.R;
import com.codeandcoder.finalguide.extra.AllConstants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;



public class Category extends Activity implements View.OnClickListener {

    private Context con;
    private GoogleMap googleMap;


    // LogCat tag
  //  private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
  //  private static int UPDATE_INTERVAL = 10000; // 10 sec
  //  private static int FATEST_INTERVAL = 5000; // 5 sec
  //  private static int DISPLACEMENT = 10; // 10 meters

  //  private TextView lblLocation;
    double latitude;
    double longitude;

    AdView adView;
    AdRequest bannerRequest, fullScreenAdRequest;
    InterstitialAd fullScreenAdd;
    private LinearLayout atms, banks, bookstores, busstations, cafes, carwash,
            dentist, doctor, food, gasstation, grocery, gym;

    private ShareActionProvider myShareActionProvider;
    LocationFound updateLoc;
    
  
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        con = this;
       // locUI();
        iUI();
       // enableAd();
//        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
//        }else{
//            showGPSDisabledAlertToUser();
//        }
        
    	//handleIntent(getIntent());
    	//listView = (ListView) findViewById(R.id.listview);
	  //  myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataArray);
	 //   listView.setAdapter(myAdapter);
	  //  listView.setTextFilterEnabled(true);
    }

    private void enableAd() {
        // TODO Auto-generated method stub

        // adding banner add
//        adView = (AdView) findViewById(R.id.adView);
//        bannerRequest = new AdRequest.Builder().build();
//        adView.loadAd(bannerRequest);

        // adding full screen add
        fullScreenAdd = new InterstitialAd(this);
        fullScreenAdd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        fullScreenAdRequest = new AdRequest.Builder().build();
        fullScreenAdd.loadAd(fullScreenAdRequest);

        fullScreenAdd.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {

                Log.i("FullScreenAdd", "Loaded successfully");
                fullScreenAdd.show();

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

                Log.i("FullScreenAdd", "failed to Load");
            }
        });

        // TODO Auto-generated method stub

    }




   /* private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Turn ON your GPS to get better RESULTS.")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }*/
    /**
     * function to load map. If map is not created it will create it for you
     * */
//    private void initilizeMap() {
//
//        if (googleMap == null) {
//            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
//                    R.id.ggmap)).getMap();
//
//            // check if map is created successfully or not
//            if (googleMap == null) {
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//    }




   /* public void locUI() {
//        lblLocation = (TextView) findViewById(R.id.textViewM);
//        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
//        btnStartLocationUpdates = (Button)findViewById(R.id.btnLocationUpdates);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

//        displayLocation();


        // Show location button click listener
//        btnShowLocation.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                displayLocation();
//            }
//        });

        // Toggling the periodic location updates
//        btnStartLocationUpdates.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                togglePeriodicLocationUpdates();
//            }
//        });
        // Toggling the periodic location updates
    }*/





//////// Location //////
  /*   @Override
   public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        initilizeMap();
        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }*/


    /**
     * Method to display the location on UI
     * */
  /*  public void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude= mLastLocation.getLongitude();
//            lblLocation.setText(latitude + ", " + longitude);

            AllConstants.UPlat=Double.toString(latitude);
            AllConstants.UPlng=Double.toString(longitude);

//            lblLocation.setText(latitude + ", " + longitude);

            PrintLog.myLog("LatLong Found: LATT", +latitude + ", " + longitude);

        } else {

//            lblLocation
//                    .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }


    }*/

//    private Menu menu;
//    MenuItem btnStartLocationUpdates = menu.findItem(R.id.action_check_updates);

    /**
     * Method to toggle periodic location updates
     * */
   /* private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text

//            btnStartLocationUpdates.setTitle(getString(R.string.btn_stop_location_updates));

            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

           // Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text
//            btnStartLocationUpdates
//                    .setTitle(getString(R.string.btn_start_location_updates));

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

          //  Log.d(TAG, "Periodic location updates stopped!");
        }
    }*/

    /**
     * Creating google api client object
     * */
/*    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    *//**
     * Creating location request object
     * *//*
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    *//**
     * Method to verify google play services on the device
     * *//*
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    *//**
     * Starting the location updates
     * *//*
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    *//**
     * Stopping location updates
     *//*
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    *//**
     * Google api callback methods
     *//*
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }*/



    /////////
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.testmenu, menu);
     //   inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        myShareActionProvider = (ShareActionProvider) item.getActionProvider();
        myShareActionProvider.setShareHistoryFileName(
                ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        myShareActionProvider.setShareIntent(createShareIntent());
        
        
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() 
        {
            @Override
            public boolean onQueryTextChange(String newText) 
            {
                // this is your adapter that will be filtered
                myAdapter.getFilter().filter(newText);
                System.out.println("on text chnge text: "+newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) 
            {
                // this is your adapter that will be filtered
                myAdapter.getFilter().filter(query);
                System.out.println("on query submit: "+query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
        
        return super.onCreateOptionsMenu(menu);

    }*/
  /*  @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
        }
    }*/
    

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        MenuItem item = menu.findItem(R.id.menu_item_share);
//        myShareActionProvider = (ShareActionProvider)item.getActionProvider();
//        myShareActionProvider.setShareHistoryFileName(
//                ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
//        myShareActionProvider.setShareIntent(createShareIntent());
//        return true;
//    }
//


   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {


            case R.id.idloc_update:
                // refresh
                togglePeriodicLocationUpdates();

                return true;

            case R.id.idshow_me:
                // help action

                ShowonMap();
                return true;
            case R.id.idfind_us:
                // help action

                FindUsActivity();
                return true;
            case R.id.idabout_us:
                // check for updates action
                AboutUs();
                return true;
                
            case R.id.menu_search:
                // check for updates action
               Toast.makeText(con, "inside the search", Toast.LENGTH_LONG).show();
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    /****************
     * Launching new activity
     * */

/*    private void ShowonMap() {
        Intent i = new Intent(Category.this, MapsActivity.class);
        startActivity(i);
    }
    private void AboutUs() {
        Intent i = new Intent(Category.this, AboutUsActivity.class);
        startActivity(i);
    }
    private void FindUsActivity() {
        Intent i = new Intent(Category.this, FindUsActivity.class);
        startActivity(i);
    }

    private void ListActivity() {
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
    }*/









    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=com.codeandcoder.finalguide");
        return shareIntent;
    }


    /////////////////Main Menu
    private void iUI() {

        atms = (LinearLayout) findViewById(R.id.atms);
        atms.setOnClickListener(this);

        banks = (LinearLayout) findViewById(R.id.banks);
        banks.setOnClickListener(this);

        bookstores = (LinearLayout) findViewById(R.id.bookstores);
        bookstores.setOnClickListener(this);

        busstations = (LinearLayout) findViewById(R.id.busstations);
        busstations.setOnClickListener(this);

        cafes = (LinearLayout) findViewById(R.id.cafes);
        cafes.setOnClickListener(this);

        carwash = (LinearLayout) findViewById(R.id.carwash);
        carwash.setOnClickListener(this);

        dentist = (LinearLayout) findViewById(R.id.dentist);
        dentist.setOnClickListener(this);

        doctor = (LinearLayout) findViewById(R.id.doctor);
        doctor.setOnClickListener(this);

        food = (LinearLayout) findViewById(R.id.food);
        food.setOnClickListener(this);

        gasstation = (LinearLayout) findViewById(R.id.gasstation);
        gasstation.setOnClickListener(this);

        grocery = (LinearLayout) findViewById(R.id.grocery);
        grocery.setOnClickListener(this);

        gym = (LinearLayout) findViewById(R.id.gym);
        gym.setOnClickListener(this);

       

    }
    @Override
    public void onClick(View v) {

       // togglePeriodicLocationUpdates();

        switch (v.getId()) {

            case R.id.atms:
                AllConstants.topTitle = "Handbags";
              
                final Intent atm = new Intent(this, RetailActivity.class);
                atm.putExtra("Text", "Handbags");
                atm.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(atm);

                break;

            case R.id.banks:
                AllConstants.topTitle = "Shoes";
             
                final Intent bank = new Intent(this, RetailActivity.class);
                bank.putExtra("Text", "Shoes");
                bank.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(bank);

                break;

            case R.id.bookstores:
                AllConstants.topTitle = "Watches";
             
                final Intent book_store = new Intent(this, RetailActivity.class);
                book_store.putExtra("Text", "Watches");
                book_store.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(book_store);

                break;
            case R.id.busstations:
                AllConstants.topTitle = "Jewelry";
              
                final Intent bus_station = new Intent(this, RetailActivity.class);
                bus_station.putExtra("Text", "Jewelry");
                bus_station.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(bus_station);

                break;
            case R.id.cafes:
                AllConstants.topTitle = "Home";
           
                final Intent cafe = new Intent(this, RetailActivity.class);
                cafe.putExtra("Text", "Home");
                cafe.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(cafe);

                break;

            case R.id.carwash:
                AllConstants.topTitle = "Accessories";
             
                final Intent car_wash = new Intent(this, RetailActivity.class);
                car_wash.putExtra("Text", "Accessories");
                car_wash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(car_wash);

                break;

            case R.id.dentist:
                AllConstants.topTitle = "Beauty";
              
                final Intent dentist = new Intent(this, RetailActivity.class);
                dentist.putExtra("Text", "Beauty");
                dentist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(dentist);

                break;
            case R.id.doctor:
                AllConstants.topTitle = "Bed";
         
                final Intent doctor = new Intent(this, RetailActivity.class);
                doctor.putExtra("Text", "Bed");
                doctor.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(doctor);

                break;
            case R.id.food:
                AllConstants.topTitle = "Bath";
             
                final Intent food = new Intent(this, RetailActivity.class);
                food.putExtra("Text", "Bath");
                food.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(food);

                break;

            case R.id.gasstation:
                AllConstants.topTitle = "Kitchen";
             
                final Intent gas_station = new Intent(this, RetailActivity.class);
                gas_station.putExtra("Text", "Kitchen");
                gas_station.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gas_station);

                break;

            case R.id.grocery:
                AllConstants.topTitle = "Furniture";
              
                final Intent grocery_or_supermarket = new Intent(this,
                        RetailActivity.class);
                grocery_or_supermarket.putExtra("Text", "Furniture");
                grocery_or_supermarket.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(grocery_or_supermarket);

                break;
            case R.id.gym:
                AllConstants.topTitle = "Electonics";
               
                final Intent gym = new Intent(this, RetailActivity.class);
                gym.putExtra("Text", "Electonics");
                gym.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gym);

                break;
          

                
                
                

        }

    }








}
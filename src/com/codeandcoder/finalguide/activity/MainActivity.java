package com.codeandcoder.finalguide.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.codeandcoder.finalguide.R;
import com.codeandcoder.finalguide.extra.AllConstants;
import com.codeandcoder.finalguide.extra.PrintLog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;



public class MainActivity extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener,View.OnClickListener, OnKeyListener {

    private Context con;
    private GoogleMap googleMap;
    EditText keyword_search;
   

    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 1000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private TextView lblLocation;
    double latitude;
    double longitude;

    AdView adView;
    AdRequest bannerRequest, fullScreenAdRequest;
    InterstitialAd fullScreenAdd;
    private LinearLayout atms, banks, bookstores, busstations, cafes, carwash,
            dentist, doctor, food, gasstation, grocery, gym, hospitals,
            mosques, theater, park, pharmacy, police, restaurant, school, mall,
            spa, store, university, atms1, banks1, bookstores1, busstations1, cafes1, carwash1,
            dentist1, doctor1, food1, gasstation1, grocery1, gym1;

    private ShareActionProvider myShareActionProvider;
    LocationFound updateLoc;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
        	Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION   };
    
  //  ArrayAdapter<String> myAdapter;
  //  ListView listView;
  //  String[] dataArray = new String[] {"India","Androidhub4you", "Pakistan", "Srilanka", "Nepal", "Japan"};
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        con = this;
        
        VerifyStoragePermissions(this);
        locUI();
        iUI();
       // enableAd();
        
        
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
        	
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{
        	
            showGPSDisabledAlertToUser();
        }
        
        
        keyword_search = (EditText) findViewById(R.id.editText1);
        keyword_search.setOnKeyListener(this);
    	//handleIntent(getIntent());
    	//listView = (ListView) findViewById(R.id.listview);
	  //  myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataArray);
	 //   listView.setAdapter(myAdapter);
	  //  listView.setTextFilterEnabled(true);
        
       
    }
    public static void VerifyStoragePermissions(Activity activity){
    
    	int writePermissions = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
    	int readPermissions = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
    
    if(writePermissions != PackageManager.PERMISSION_GRANTED || readPermissions != PackageManager.PERMISSION_GRANTED){
    ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);	
    }
    
    }
    
    public void VerifyPermissions(){
    	  VerifyStoragePermissions(this);
    }
    
    
    @Override
    public void onRequestPermissionsResult(int requestCode,
            String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                	  
                       
                       
                      
                	

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                	 if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                         showDialogOK("Location Services Permission required for this app",
                                 new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         switch (which) {
                                             case DialogInterface.BUTTON_POSITIVE:
                                            	 VerifyPermissions();
                                                 break;
                                             case DialogInterface.BUTTON_NEGATIVE:
                                                 // proceed with logic by disabling the related features or quit the app.
                                                 break;
                                         }
                                     }
                                 });
                     }
                	
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    
    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
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




    private void showGPSDisabledAlertToUser(){
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
    }
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




    public void locUI() {
//        lblLocation = (TextView) findViewById(R.id.textViewM);
//        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
//        btnStartLocationUpdates = (Button)findViewById(R.id.btnLocationUpdates);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        displayLocation();


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
    }





//////// Location //////
    @Override
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
      //  if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
        if (mGoogleApiClient.isConnected()) {
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
        if (mGoogleApiClient.isConnected()) {
        stopLocationUpdates();
        }
    }


    /**
     * Method to display the location on UI
     * */
    public void displayLocation() {

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


    }

//    private Menu menu;
//    MenuItem btnStartLocationUpdates = menu.findItem(R.id.action_check_updates);

    /**
     * Method to toggle periodic location updates
     * */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text

//            btnStartLocationUpdates.setTitle(getString(R.string.btn_stop_location_updates));

            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text
//            btnStartLocationUpdates
//                    .setTitle(getString(R.string.btn_start_location_updates));

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     * */
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

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {
    	
    	 VerifyStoragePermissions(this);
    	
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
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
    }



    /////////
    @Override
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
        
        
      /*  SearchManager searchManager =
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
        searchView.setOnQueryTextListener(textChangeListener);*/
        
        return super.onCreateOptionsMenu(menu);

    }
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


    @Override
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
                
          /*  case R.id.menu_search:
                // check for updates action
               Toast.makeText(con, "inside the search", Toast.LENGTH_LONG).show();
                return true;*/
                
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /****************
     * Launching new activity
     * */

    private void ShowonMap() {
        Intent i = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(i);
    }
    private void AboutUs() {
        Intent i = new Intent(MainActivity.this, AboutUsActivity.class);
        startActivity(i);
    }
    private void FindUsActivity() {
        Intent i = new Intent(MainActivity.this, FindUsActivity.class);
        startActivity(i);
    }

    private void ListActivity() {
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
    }









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

        hospitals = (LinearLayout) findViewById(R.id.hospitals);
        hospitals.setOnClickListener(this);

        mosques = (LinearLayout) findViewById(R.id.mosques);
        mosques.setOnClickListener(this);

        theater = (LinearLayout) findViewById(R.id.theater);
        theater.setOnClickListener(this);

        park = (LinearLayout) findViewById(R.id.park);
        park.setOnClickListener(this);

        pharmacy = (LinearLayout) findViewById(R.id.pharmacy);
        pharmacy.setOnClickListener(this);

        police = (LinearLayout) findViewById(R.id.police);
        police.setOnClickListener(this);

        restaurant = (LinearLayout) findViewById(R.id.restaurant);
        restaurant.setOnClickListener(this);

        school = (LinearLayout) findViewById(R.id.school);
        school.setOnClickListener(this);

        mall = (LinearLayout) findViewById(R.id.mall);
        mall.setOnClickListener(this);

        spa = (LinearLayout) findViewById(R.id.spa);
        spa.setOnClickListener(this);

        store = (LinearLayout) findViewById(R.id.store);
        store.setOnClickListener(this);

        university = (LinearLayout) findViewById(R.id.university);
        university.setOnClickListener(this);
        
        atms1 = (LinearLayout) findViewById(R.id.atms1);
        atms1.setOnClickListener(this);

        banks1 = (LinearLayout) findViewById(R.id.banks1);
        banks1.setOnClickListener(this);

        bookstores1 = (LinearLayout) findViewById(R.id.bookstores1);
        bookstores1.setOnClickListener(this);

        busstations1 = (LinearLayout) findViewById(R.id.busstations1);
        busstations1.setOnClickListener(this);

        cafes1 = (LinearLayout) findViewById(R.id.cafes1);
        cafes1.setOnClickListener(this);

        carwash1 = (LinearLayout) findViewById(R.id.carwash1);
        carwash1.setOnClickListener(this);

        dentist1 = (LinearLayout) findViewById(R.id.dentist1);
        dentist1.setOnClickListener(this);

        doctor1 = (LinearLayout) findViewById(R.id.doctor1);
        doctor1.setOnClickListener(this);

        food1 = (LinearLayout) findViewById(R.id.food1);
        food1.setOnClickListener(this);

        gasstation1 = (LinearLayout) findViewById(R.id.gasstation1);
        gasstation1.setOnClickListener(this);

        grocery1 = (LinearLayout) findViewById(R.id.grocery1);
        grocery1.setOnClickListener(this);

        gym1 = (LinearLayout) findViewById(R.id.gym1);
        gym1.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {

        togglePeriodicLocationUpdates();
        
        
        try {
			
		

        switch (v.getId()) {

            case R.id.atms:
                AllConstants.topTitle = "ATMS LIST";
                AllConstants.query = "atm";
                final Intent atm = new Intent(this, ListActivity.class);
                atm.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(atm);

                break;

            case R.id.banks:
                AllConstants.topTitle = "BANKS LIST";
                AllConstants.query = "bank";
                final Intent bank = new Intent(this, ListActivity.class);
                bank.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(bank);

                break;

            case R.id.bookstores:
                AllConstants.topTitle = "BOOK STORES LIST";
                AllConstants.query = "book_store";
                final Intent book_store = new Intent(this, ListActivity.class);
                book_store.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(book_store);

                break;
            case R.id.busstations:
                AllConstants.topTitle = "BUS STATION LIST";
                AllConstants.query = "bus_station";
                final Intent bus_station = new Intent(this, ListActivity.class);
                bus_station.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(bus_station);

                break;
            case R.id.cafes:
                AllConstants.topTitle = "CAFES LIST";
                AllConstants.query = "cafe";
                final Intent cafe = new Intent(this, ListActivity.class);
                cafe.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(cafe);

                break;

            case R.id.carwash:
                AllConstants.topTitle = "CAR WASH LIST";
                AllConstants.query = "car_wash";
                final Intent car_wash = new Intent(this, ListActivity.class);
                car_wash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(car_wash);

                break;

            case R.id.dentist:
                AllConstants.topTitle = "DENTIST LIST";
                AllConstants.query = "dentist";
                final Intent dentist = new Intent(this, ListActivity.class);
                dentist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(dentist);

                break;
            case R.id.doctor:
                AllConstants.topTitle = "DOCTOR LIST";
                AllConstants.query = "doctor";
                final Intent doctor = new Intent(this, ListActivity.class);
                doctor.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(doctor);

                break;
            case R.id.food:
                AllConstants.topTitle = "FOOD LIST";
                AllConstants.query = "food";
                final Intent food = new Intent(this, ListActivity.class);
                food.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(food);

                break;

            case R.id.gasstation:
                AllConstants.topTitle = "GAS STATION LIST";
                AllConstants.query = "gas_station";
                final Intent gas_station = new Intent(this, ListActivity.class);
                gas_station.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gas_station);

                break;

            case R.id.grocery:
                AllConstants.topTitle = "GROCERY LIST";
                AllConstants.query = "grocery_or_supermarket";
                final Intent grocery_or_supermarket = new Intent(this,
                        ListActivity.class);
                grocery_or_supermarket.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(grocery_or_supermarket);

                break;
            case R.id.gym:
                AllConstants.topTitle = "GYM LIST";
                AllConstants.query = "gym";
                final Intent gym = new Intent(this, ListActivity.class);
                gym.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gym);

                break;
            case R.id.hospitals:
                AllConstants.topTitle = "HOSPITALS LIST";
                AllConstants.query = "hospital";
                final Intent hospital = new Intent(this, ListActivity.class);
                hospital.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(hospital);

                break;

            case R.id.mosques:
                AllConstants.topTitle = "MOSQUES LIST";
                AllConstants.query = "mosque";
                final Intent mosque = new Intent(this, ListActivity.class);
                mosque.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mosque);

                break;

            case R.id.theater:
                AllConstants.topTitle = "THEATER LIST";
                AllConstants.query = "movie_theater";
                final Intent movie_theater = new Intent(this, ListActivity.class);
                movie_theater.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(movie_theater);

                break;
            case R.id.park:
                AllConstants.topTitle = "PARK LIST";
                AllConstants.query = "rv_park";
                final Intent rv_park = new Intent(this, ListActivity.class);
                rv_park.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(rv_park);

                break;
            case R.id.pharmacy:
                AllConstants.topTitle = "PHARMACY LIST";
                AllConstants.query = "pharmacy";
                final Intent pharmacy = new Intent(this, ListActivity.class);
                pharmacy.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(pharmacy);

                break;

            case R.id.police:
                AllConstants.topTitle = "POLICE LIST";
                AllConstants.query = "police";
                final Intent police = new Intent(this, ListActivity.class);
                police.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(police);

                break;

            case R.id.restaurant:
                AllConstants.topTitle = "RESTAURANT LIST";
                AllConstants.query = "restaurant";
                final Intent restaurant = new Intent(this, ListActivity.class);
                restaurant.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(restaurant);

                break;
            case R.id.school:
                AllConstants.topTitle = "SCHOOL LIST";
                AllConstants.query = "school";
                final Intent school = new Intent(this, ListActivity.class);
                school.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(school);

                break;
            case R.id.mall:
                AllConstants.topTitle = "SHOPPING MALL LIST";
                AllConstants.query = "shopping_mall";
                final Intent shopping_mall = new Intent(this, ListActivity.class);
                shopping_mall.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(shopping_mall);

                break;

            case R.id.spa:
                AllConstants.topTitle = "SPA LIST";
                AllConstants.query = "spa";
                final Intent spa = new Intent(this, ListActivity.class);
                spa.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(spa);

                break;

            case R.id.store:
                AllConstants.topTitle = "STORE LIST";
                AllConstants.query = "store";
                final Intent store = new Intent(this, ListActivity.class);
                store.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(store);

                break;

            case R.id.university:
                AllConstants.topTitle = "UNIVERSITY LIST";
                AllConstants.query = "university";
                final Intent university = new Intent(this, ListActivity.class);
                university.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(university);
                
                break;
                
            case R.id.atms1:
                AllConstants.topTitle = "Handbags";
              
                final Intent atm1 = new Intent(this, RetailActivity.class);
                atm1.putExtra("Text", "Handbags");
                atm1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(atm1);

                break;

            case R.id.banks1:
                AllConstants.topTitle = "Shoes";
             
                final Intent bank1 = new Intent(this, RetailActivity.class);
                bank1.putExtra("Text", "Shoes");
                bank1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(bank1);

                break;

            case R.id.bookstores1:
                AllConstants.topTitle = "Watches";
             
                final Intent book_store1 = new Intent(this, RetailActivity.class);
                book_store1.putExtra("Text", "Watches");
                book_store1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(book_store1);

                break;
            case R.id.busstations1:
                AllConstants.topTitle = "Jewelry";
              
                final Intent bus_station1 = new Intent(this, RetailActivity.class);
                bus_station1.putExtra("Text", "Jewelry");
                bus_station1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(bus_station1);

                break;
            case R.id.cafes1:
                AllConstants.topTitle = "Home";
           
                final Intent cafe1 = new Intent(this, RetailActivity.class);
                cafe1.putExtra("Text", "Home");
                cafe1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(cafe1);

                break;

            case R.id.carwash1:
                AllConstants.topTitle = "Accessories";
             
                final Intent car_wash1 = new Intent(this, RetailActivity.class);
                car_wash1.putExtra("Text", "Accessories");
                car_wash1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(car_wash1);

                break;

            case R.id.dentist1:
                AllConstants.topTitle = "Beauty";
              
                final Intent dentist1 = new Intent(this, RetailActivity.class);
                dentist1.putExtra("Text", "Beauty");
                dentist1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(dentist1);

                break;
            case R.id.doctor1:
                AllConstants.topTitle = "Bed";
         
                final Intent doctor1 = new Intent(this, RetailActivity.class);
                doctor1.putExtra("Text", "Bed");
                doctor1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(doctor1);

                break;
            case R.id.food1:
                AllConstants.topTitle = "Bath";
             
                final Intent food1 = new Intent(this, RetailActivity.class);
                food1.putExtra("Text", "Bath");
                food1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(food1);

                break;

            case R.id.gasstation1:
                AllConstants.topTitle = "Kitchen";
             
                final Intent gas_station1 = new Intent(this, RetailActivity.class);
                gas_station1.putExtra("Text", "Kitchen");
                gas_station1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gas_station1);

                break;

            case R.id.grocery1:
                AllConstants.topTitle = "Furniture";
              
                final Intent grocery_or_supermarket1 = new Intent(this,
                        RetailActivity.class);
                grocery_or_supermarket1.putExtra("Text", "Furniture");
                grocery_or_supermarket1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(grocery_or_supermarket1);

                break;
            case R.id.gym1:
                AllConstants.topTitle = "Electonics";
               
                final Intent gym1 = new Intent(this, RetailActivity.class);
                gym1.putExtra("Text", "Electonics");
                gym1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gym1);

                break;

/*AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
       	        MainActivity.this);

       	alertDialog2.setTitle("City Guide");

       	alertDialog2.setMessage("Enter keyword to be search!");

       	//alertDialog2.setIcon(R.drawable.ic_launcher);
       	final EditText input = new EditText(this);
       	alertDialog2.setView(input);
       	alertDialog2.setCancelable(false);
       	alertDialog2.setPositiveButton("Done",
       	        new DialogInterface.OnClickListener() {
       	            public void onClick(DialogInterface dialog, int which) {
       	    
       	            	if(!(TextUtils.isEmpty(input.getText().toString())))
       	            	{
                        // Toast.makeText(getApplicationContext(),input.getText().toString(), Toast.LENGTH_LONG).show();
                            Intent myIntent1 = new Intent(MainActivity.this, RetailActivity.class);
                         
                             myIntent1.putExtra("Text",input.getText().toString());
                             startActivity(myIntent1);
                        
       	            	}
       	            	
       	            	else{
                                Toast.makeText(getApplicationContext(),"Enter something to search!", Toast.LENGTH_LONG).show();

       	            	}
       	            	
       	            	//finish();
       	            }
       	        });
       	
       	alertDialog2.setNegativeButton("Cancel",
       	        new DialogInterface.OnClickListener() {
       	            public void onClick(DialogInterface dialog, int which) {
       	       
       	                dialog.cancel();
       	            }
       	        });
       	
       	alertDialog2.show();*/

                
                
                

        }
        
        } catch (Exception e) {
			// TODO: handle exception
        
		}

    }

	 @Override
	 public boolean onKey(View view, int keyCode, KeyEvent event) {
	  
	 
	  
	  if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
	   keyCode == EditorInfo.IME_ACTION_DONE ||
	   event.getAction() == KeyEvent.ACTION_DOWN &&
	   event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
	   
	   if (!event.isShiftPressed()) {
	    Log.v("AndroidEnterKeyActivity","Enter Key Pressed!");
	    switch (view.getId()) {
	    case R.id.editText1:
	    	String str =keyword_search.getText().toString();
	    	if(str.equals(""))
	    	{
	    		 Toast.makeText(getApplicationContext(),"Enter something to search!", Toast.LENGTH_LONG).show();
	    	}
	    	else
	    	{
	    		
	    	
			Intent intent = new Intent(MainActivity.this,RetailActivity.class);
			intent.putExtra("Text", str);
			startActivity(intent);
	     break;
	    	}
	    }
	    return true; 
	   }                
	  
	  }
	  return false; // pass on to other listeners. 

	 }

    public void goforsearch(View a)

	{ 
    	
    	
    	String str =keyword_search.getText().toString();
    	if(str.equals(""))
    	{
    		 Toast.makeText(getApplicationContext(),"Enter something to search!", Toast.LENGTH_LONG).show();
    	}
    	else
    	{
    		
		Intent intent = new Intent(MainActivity.this,RetailActivity.class);
		intent.putExtra("Text", str);
		startActivity(intent);
    	}
	}





}
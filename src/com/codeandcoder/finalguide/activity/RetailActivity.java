package com.codeandcoder.finalguide.activity;


import com.codeandcoder.finalguide.activity.JSONParser;

import com.codeandcoder.finalguide.activity.RetailActivity.ViewHolder;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.StrictMode;
import com.androidquery.AQuery;
import com.codeandcoder.finalguide.R;
import com.codeandcoder.finalguide.extra.AlertMessage;
import com.codeandcoder.finalguide.extra.AllConstants;
import com.codeandcoder.finalguide.extra.AllURL;
import com.codeandcoder.finalguide.extra.CacheImageDownloader;
import com.codeandcoder.finalguide.extra.PrintLog;
import com.codeandcoder.finalguide.extra.SharedPreferencesHelper;
import com.codeandcoder.finalguide.holder.AllCityMenu;
import com.codeandcoder.finalguide.model.CityMenuList;
import com.codeandcoder.finalguide.parser.CityMenuParser;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import com.codeandcoder.finalguide.R;
import com.codeandcoder.finalguide.extra.AllConstants;
import com.codeandcoder.finalguide.extra.PrintLog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.maps.GoogleMap;
import com.squareup.picasso.Picasso;

public class RetailActivity extends Activity implements OnKeyListener{
	/** Called when the activity is first created. */

	private ListView list;
	private Context con;
	private Bitmap defaultBit;
	private RestaurantAdapter adapter;
	private ProgressDialog pDialog;
	private CacheImageDownloader downloader;
	private String apikey = "rku-O7xBYK9nQWrxx_aE4GFVM5vRVGDX";
	private String requestorid = "13cda9a1dc2c69e5";
	Intent i;
	public String result = "";
	JSONObject data;
	public String id_send;
	
	/*TextView name,product,distance;
	TextView address;
	ImageView icon,icon1;
    RatingBar listRatings;*/
	 EditText keyword_search;

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;


	AdView adView;
	AdRequest bannerRequest, fullScreenAdRequest;
	InterstitialAd fullScreenAdd;
	
	
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.listlayout1);
	    i = getIntent(); 
		// get the action bar
		setTheme(R.style.HomeTheme);
		aBar();
		con = this;
		initUI();
		//enableAd();
		
		 keyword_search = (EditText) findViewById(R.id.editText1);
	     keyword_search.setOnKeyListener(this);

	 }


	private void aBar() {

		// get the action bar
		ActionBar actionBar = getActionBar();

		// Enabling Back navigation on Action Bar icon
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		getActionBar().setTitle("AROUND ME");

	}





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.testmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {


			case android.R.id.home:
				this.finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	private void enableAd() {
		// TODO Auto-generated method stub

		// adding banner add
		adView = (AdView) findViewById(R.id.adView);
		bannerRequest = new AdRequest.Builder().build();
		adView.loadAd(bannerRequest);

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

	private void initUI() {
		// TODO Auto-generated method stub

		list = (ListView) findViewById(R.id.menuListView);
		downloader = new CacheImageDownloader();
//		defaultBit = BitmapFactory.decodeResource(getResources(),
//				R.drawable.logo);

		// parseQuery(AllConstants.query);
		parseQuery();

		PrintLog.myLog("Query in activity : ", AllConstants.query);

	}

	private void parseQuery() {
		// TODO Auto-generated method stub
		if (!SharedPreferencesHelper.isOnline(con)) {
			AlertMessage.showMessage(con, "Error", "No internet connection");
			return;
		}

		pDialog = ProgressDialog.show(this, "", "Loading..", false, false);

		

			
				// TODO Auto-generated method stub
				try {
					//String s1 = AllConstants.UPlat.toString()+','+AllConstants.UPlng.toString();
					
					
					
					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("apikey",apikey.toString()));
					params.add(new BasicNameValuePair("requestorid",requestorid.toString()));
					params.add(new BasicNameValuePair("userlocation",AllConstants.UPlat+','+AllConstants.UPlng));
					params.add(new BasicNameValuePair("keywords",i.getExtras().getString("Text")));
					 result = new JSONParser().openConnection("products", "GET", params );
					// Log.d("jjjjjjjjjjjjjjjjjjjjjjj", result.toString());
					
					

				} catch (final Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				runOnUiThread(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						if (pDialog != null) {
							pDialog.cancel();
						}
						if (result.length() == 0) {

						} else {
							

							adapter = new RestaurantAdapter(RetailActivity.this, result);
							list.setAdapter(adapter);
							adapter.notifyDataSetChanged();
							
						}

					}
				});

	
	}
	
	public static class ViewHolder
	{
		public  TextView address,name,product,distance; 
		public ImageView icon,icon1; 
		public RatingBar listRatings;
		
		
	}

	class RestaurantAdapter extends ArrayAdapter<CityMenuList> {
		private final Context con;
		AQuery aq;
		JSONArray it,imageinfo;
		JSONObject da, first, sec, third,fourth,fifth,dlat,distance1,product1,image,image1;
		String ss,cname,cadd1,cadd2,cadd3,cadd4,cadd5,cadd6,cadd7,phone,dlat1,dlng1,logo,city;
		String product_send,store,desc,price,url,imgname,id;

		public RestaurantAdapter(Context context, String result) {
			super(context, R.layout.rowlist1);
			con = context;
			// TODO Auto-generated constructor stub
			
			try {
				  JSONObject sa=new JSONObject(result);
					da=sa.getJSONObject("RetailigenceSearchResult");
					ss = da.getString("count").toString();
					
						
					
				} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		
		@Override
		public int getCount() {
			
				
				return Integer.parseInt(ss);
			
			
			
			
		}

		

		public long getItemId(int position) {
			
			return position;
		}

		
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
		
			
			View v = convertView;
			ViewHolder  holder = new ViewHolder();
			
			 			
			if (v == null) {
				final LayoutInflater vi = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.rowlist1, null);
				
				holder.address = (TextView) v
						.findViewById(R.id.rowAddress);
				holder.icon = (ImageView) v
						.findViewById(R.id.rowImageView);
				holder.listRatings = (RatingBar) v
						.findViewById(R.id.ratingBarList);
				holder.name = (TextView) v.findViewById(R.id.rowName);
				
				holder.icon1 = (ImageView) v
						.findViewById(R.id.logo);
				 holder.product = (TextView) v
						.findViewById(R.id.prodname);
				 holder.distance = (TextView) v
						.findViewById(R.id.distance);
				
				v.setTag(holder);
			} else{
	    		  holder= (ViewHolder) v.getTag();
   	     }
			
			try {
				 
				it=da.getJSONArray("results");
					
				} catch (JSONException e) {
				e.printStackTrace();
			}
			

			if (position < it.length()) {
				//final CityMenuList CM = AllCityMenu.getCityMenuList(position);

//				Typeface title = Typeface.createFromAsset(getAssets(),
//						"fonts/ROBOTO-REGULAR.TTF");
//				Typeface add = Typeface.createFromAsset(getAssets(),
//						"fonts/ROBOTO-LIGHT.TTF");

				// ----Address----
//				mLastLocation = LocationServices.FusedLocationApi
//						.getLastLocation(mGoogleApiClient);
//
//
//				double	latitudee = ;
//				double	longitudee= ;
//				PrintLog
//						.myLog("ListLog", latitudee+ ""+longitudee);
//				double distance;
//				Location locationA = new Location("");
//				locationA.setLatitude(latitudee);
//				locationA.setLongitude(longitudee);
//				Location locationB = new Location("");
//				locationB.setLatitude(latB);
//				LocationB.setLongitude(lngB);
//
//                //distance = locationA.distanceTo(locationB);   //in meters
//
//				distance = locationA.distanceTo(locationB)/1000;
				

				
				
				try {
				 data=it.getJSONObject(position);
				 
				 sec= data.getJSONObject("SearchResult");
				 third= sec.getJSONObject("location");
				 product1= sec.getJSONObject("product");
				 fourth= third.getJSONObject("address");
				 fifth= third.getJSONObject("retailer");
				 dlat =third.getJSONObject("location");
				 distance1= sec.getJSONObject("distance");
			//	 logo = fifth.getString("logo").toString();
				 
				 imageinfo = product1.getJSONArray("images");
				 image = imageinfo.getJSONObject(1);
				 image1 = image.getJSONObject("ImageInfo");
				 imgname = image1.getString("link").toString();
				 id = product1.getString("id").toString();
				 
				
				 
			//	 phone = third.getString("phone").toString();
				 dlat1 = dlat.getString("latitude").toString(); 
				 dlng1 = dlat.getString("longitude").toString(); 
			//	 product_send = product1.getString("name").toString(); 
			//	 desc = product1.getString("descriptionShort").toString(); 
			//	 price = sec.getString("price").toString(); 
			//	 url = product1.getString("url").toString(); 
				 
				 
				 
				 
					cname = fifth.getString("name").toString();
					cadd1 = fourth.getString("address1").toString();
					
					cadd2 = fourth.getString("city").toString();
					cadd3 = fourth.getString("state").toString();
					cadd4 = fourth.getString("country").toString();
					cadd5 = fourth.getString("postal").toString();
					cadd6 = cadd1+','+' '+cadd2+','+' '+cadd3+' '+cadd5+','+' '+cadd4;
				 
					
					holder.name.setText(fifth.getString("name").toString());
					holder.address.setText(fourth.getString("address1").toString()+','+' '+cadd2.toString());
					//holder.icon.setImageResource(R.drawable.not_found_banner);
					
					Picasso.with(con).load(imgname).into(holder.icon);
					
					//aq.id(R.id.logo).image(logo.toString(),true,true, 0, 0, null, 0);
					//aq.id(R.id.logo).progress(R.id.progress_circular).image((logo.toString()),true,true, 0, 0, null, 0);
					// Log.d("ddddddddddddddddd",logo.toString());
					// Picasso.with(con).load(logo).into(icon1);
					
					runOnUiThread(new Runnable() {
					     @Override
					     public void run() {

					  
					    	// Picasso.with(con).load(logo).into(icon1);
					    	

					    }
					});
					
					holder.distance.setText(distance1.getString("distance").toString()+' '+distance1.getString("units").toString());
					holder.distance.setTag(id);
					
					holder.product.setText(product1.getString("name").toString());
					/*if(product1.getString("name").length()>30)
					{holder.product.setText(product1.getString("name").substring(0,30).toString()+"..");
			        }else 
					{holder.product.setText(product1.getString("name").toString());
				    }*/
					

				
					
					
					
			} catch (JSONException e) {
				Log.e("Error",e.toString());
			}
//
				

			
				

			/*	try {

					AllConstants.photoReferrence = CM.getPhotoReference()
							.toString().trim();
					PrintLog
							.myLog("PPRRRef", AllConstants.photoReferrence + "");
				} catch (Exception e) {
					// TODO: handle exception
				}*/

				// ---Image---

			/*	final ImageView icon = (ImageView) v
						.findViewById(R.id.rowImageView);
				
				try {

					AllConstants.iconUrl = CM.getIcon()
							.toString().trim();
					PrintLog
							.myLog("iconURL:", AllConstants.iconUrl + "");
				} catch (Exception e) {
					// TODO: handle exception
				}*/
				
			/*	try {

					String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=190&photoreference="
							+ AllConstants.photoReferrence
							+ "&sensor=true&key=" + AllConstants.apiKey;

					if (AllConstants.photoReferrence.length() != 0) {

//						downloader.download(AllConstants.iconUrl.trim(), icon);
						downloader.download(imgUrl.trim(), icon);

						AllConstants.cPhotoLink = imgUrl.replaceAll(" ", "%20");
					}

					else {
//						downloader.download(AllConstants.iconUrl.trim(), icon);
//						AllConstants.cPhotoLink = AllConstants.iconUrl.replaceAll(" ", "%20");

						icon.setImageResource(R.drawable.not_found_banner);


					}

				} catch (Exception e) {
					// TODO: handle exception
				}*/

				// ------Rating ---
			/*	final RatingBar listRatings = (RatingBar) v
						.findViewById(R.id.ratingBarList);

				String rating = CM.getRating();

				try {

					Float count = Float.parseFloat(rating);

					listRatings.setRating(count);

				} catch (Exception e) {

				}
*/
				// ----Name----

				/*final TextView name = (TextView) v.findViewById(R.id.rowName);
				try {
					name.setText(CM.getName().toString().trim());
//					name.setTypeface(title);
				} catch (Exception e) {
					// TODO: handle exception
				}*/
				v.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						
						
						
						try {
							AllConstants.Dlat = dlat1.toString();
							AllConstants.Dlng = dlng1.toString();


						} catch (Exception e) {
							// TODO: handle exception
						}
						
						try {
							
							TextView tv1 = (TextView) v.findViewById(R.id.rowAddress);
						
							
							TextView tv2= (TextView) v.findViewById(R.id.rowName);
							
						
							TextView tv3 = (TextView) v.findViewById(R.id.prodname);
							TextView tv4 = (TextView) v.findViewById(R.id.distance);
							
							
							id_send = tv4.getTag().toString();//Log.d("aaaaaaaaaaaaaaaaaaaaaaaa",tv2.getText().toString());
							
							AllConstants.NAME = tv2.getText().toString();
							AllConstants.ADD =  tv1.getText().toString();
						//	AllConstants.PHONE = phone.toString();
						//	AllConstants.DESC = desc.toString();
							AllConstants.PRODUCT = tv3.getText().toString();
						//	AllConstants.PRICE = price.toString();
						//	AllConstants.URL = url.toString();
						//	AllConstants.IMAGE = imgname.toString();
						


						} catch (Exception e) {
							// TODO: handle exception
						}
						/*AllConstants.referrence = CM.getReference().toString()
								.trim();
						try {
							AllConstants.photoReferrence = CM
									.getPhotoReference().toString().trim();
						} catch (Exception e) {
							// TODO: handle exception
						}

						
						PrintLog.myLog("DDDLatLng : ", CM
								.getdLat().toString().trim()+"  "+CM
								.getdLan().toString().trim());

						try {
							AllConstants.detailsiconUrl = CM
									.getIcon().toString().trim();
						} catch (Exception e) {
							// TODO: handle exception
						}*/
						
						
						
						final Intent iii = new Intent(con,ListDetailsActivity1.class);
					//	iii.putExtra("POSITION", position);
					    iii.putExtra("ID", id_send.toString());
					  //  Log.d("vvvvvvvvvvvvvvvvvvvv",i.getExtras().getString("Text"));
						iii.putExtra("KEYWORD", i.getExtras().getString("Text"));
					//	iii.putExtra("PHONE", phone.toString());
						//iii.putExtra("Dlat", dlat1.toString());
						//iii.putExtra("Dlng", dlng1.toString());
					//	iii.putExtra("PRODUCT", product_send.toString());
					//	iii.putExtra("DESC", desc.toString());
					//	iii.putExtra("PRICE", price.toString());
					//	iii.putExtra("URL", url.toString());
					//	iii.putExtra("IMAGE", imgname.toString());
						
						
						iii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(iii);

						// }

					}
				});

			}
			

			// TODO Auto-generated method stub
			return v;
		}

	}

	public void btnHome(View v) {

		Intent next = new Intent(con, MainActivity.class);
		next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(next);

	}

	public void btnBack(View v) {
		finish();

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
	    		
	    	
			Intent intent = new Intent(RetailActivity.this,RetailActivity.class);
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
    		
		Intent intent = new Intent(RetailActivity.this,RetailActivity.class);
		intent.putExtra("Text", str);
		startActivity(intent);
    	}
	}

}

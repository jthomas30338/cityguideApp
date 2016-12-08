package com.codeandcoder.finalguide.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.codeandcoder.finalguide.activity.JSONParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codeandcoder.finalguide.R;
import com.codeandcoder.finalguide.extra.AlertMessage;
import com.codeandcoder.finalguide.extra.AllConstants;
import com.codeandcoder.finalguide.extra.AllURL;
import com.codeandcoder.finalguide.extra.CacheImageDownloader;
import com.codeandcoder.finalguide.extra.PrintLog;
import com.codeandcoder.finalguide.extra.SharedPreferencesHelper;
import com.codeandcoder.finalguide.holder.AllCityDetails;
import com.codeandcoder.finalguide.holder.AllCityReview;
import com.codeandcoder.finalguide.holder.BiCyleDetails;
import com.codeandcoder.finalguide.holder.DrivingDetails;
import com.codeandcoder.finalguide.holder.WalkingDetails;
import com.codeandcoder.finalguide.model.BicyleTime;
import com.codeandcoder.finalguide.model.CityDetailsList;
import com.codeandcoder.finalguide.model.DrivingTime;
import com.codeandcoder.finalguide.model.ReviewList;
import com.codeandcoder.finalguide.model.WalkingTime;
import com.codeandcoder.finalguide.parser.BiCyleDetailsParser;
import com.codeandcoder.finalguide.parser.CityDetailsParser;
import com.codeandcoder.finalguide.parser.CityReviewParser;
import com.codeandcoder.finalguide.parser.DrivingDetailsParser;
import com.codeandcoder.finalguide.parser.WalkingDetailsParser;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.internal.pr;
import com.squareup.picasso.Picasso;


public class ListDetailsActivity1 extends Activity {
	/** Called when the activity is first created. */
	AdView adView;
	AdRequest bannerRequest, fullScreenAdRequest;
	InterstitialAd fullScreenAdd;
	private Context con;
	private String pos = "";
	private TextView cName, cAdd,bDetails,wDetails,dDetails, cPhone,textDis;
	private TextView prod_name1, desc1,price1,store1;
	private ImageView image1;
	private CacheImageDownloader downloader;
	private Bitmap defaultBit;
	private ProgressDialog pDialog;
	private CityDetailsList CD;
	private DrivingTime DT;
	private BicyleTime BT;
	private WalkingTime WT;
	private RatingBar detailsRat;
	private RestaurantAdapter adapter;
	private ListView list;
	private String apikey = "rku-O7xBYK9nQWrxx_aE4GFVM5vRVGDX";
	private String requestorid = "13cda9a1dc2c69e5";
	private String Demourl = "https://maps.googleapis.com/maps/api/place/details/json?reference=CnRkAAAAj2VWwXQIX-TFfx6XaexF9rN6Kc005BMP8h0V2pKj7IuyLPWUBCt7gnHr8q9RYWeIva06HuChuwhxsio4f7c9s5aLynGzzX19Oatq8Q9Oz8w2Zj54B8PUNgDNcQ6rHKuKmpAPJBXitOcAYugvPZshDBIQsYMRaNz0n5VfpHx6C2GCFRoUsCD2Zx0P_a-rqyxHN-GTC1QZz2U&sensor=true&key=AIzaSyC6zHflVgVCLKEMWBFMFm5qj0Jis-eoR4U";
    Intent i;
    String s1,s2,phone,lat,lng,prod_name,desc,price,cname,url,image,id,id1,result,ss,imgname;
	JSONObject da,sec,third,product1,fourth,fifth,dlat,distance1,data,image_obj,image1_obj;
	JSONArray it,imageinfo;
    AQuery aq;
    
    
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(policy);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newdetailslayout3);
		i = getIntent();
		
	//	s1 = i.getExtras().getString("NAME");
	//	s2 = i.getExtras().getString("ADD");
	//	phone = i.getExtras().getString("PHONE");
	//	lat = i.getExtras().getString("Dlat");
	//	lng = i.getExtras().getString("Dlng");
	//	prod_name = i.getExtras().getString("PRODUCT");
	//	desc = i.getExtras().getString("DESC");
	//	price = i.getExtras().getString("PRICE");
	//	url = i.getExtras().getString("URL");
		id = i.getExtras().getString("ID");
		
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("DETAILS");


		con = this;
		//enableAd();
		initUI();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu, menu);
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

			case R.id.share_update:
				ShareIntent();
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
		list = (ListView) findViewById(R.id.reviewListView);
		list.setOnTouchListener(new ListView.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						// Disallow ScrollView to intercept touch events.
						v.getParent().requestDisallowInterceptTouchEvent(true);
						break;

					case MotionEvent.ACTION_UP:
						// Allow ScrollView to intercept touch events.
						v.getParent().requestDisallowInterceptTouchEvent(false);
						break;
				}

				// Handle ListView touch events.
				v.onTouchEvent(event);
				return true;
			}
		});
		cName = (TextView) findViewById(R.id.cName);
		cAdd = (TextView) findViewById(R.id.cAddress);
		cPhone = (TextView) findViewById(R.id.cPhone);
		wDetails = (TextView) findViewById(R.id.walkD);
		bDetails = (TextView) findViewById(R.id.bycleD);
		dDetails = (TextView) findViewById(R.id.driveD);
		textDis = (TextView) findViewById(R.id.textD);
		detailsRat = (RatingBar) findViewById(R.id.detailsRating);
		
		prod_name1 =  (TextView) findViewById(R.id.productname);
		desc1 =  (TextView) findViewById(R.id.description);
		price1 =  (TextView) findViewById(R.id.price);
		store1 =  (TextView) findViewById(R.id.storename);
    	image1 =  (ImageView) findViewById(R.id.imageViewL);
		
		
		downloader = new CacheImageDownloader();
		defaultBit = BitmapFactory.decodeResource(getResources(),
				R.drawable.not_found_banner);

		updateUI();

	}

	/****
	 * 
	 * update wrestler info
	 * 
	 */

	private void updateUI() {
		if (!SharedPreferencesHelper.isOnline(con)) {
			AlertMessage.showMessage(con, "Error", "No internet connection");
			return;
		}

		pDialog = ProgressDialog.show(this, "", "Loading..", false, false);
		
		
		try {
			//String s1 = AllConstants.UPlat.toString()+','+AllConstants.UPlng.toString();
			
			
			
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("apikey",apikey.toString()));
			params.add(new BasicNameValuePair("requestorid",requestorid.toString()));
			params.add(new BasicNameValuePair("userlocation",AllConstants.UPlat+','+AllConstants.UPlng));
			params.add(new BasicNameValuePair("keywords",i.getExtras().getString("KEYWORD")));
			result = new JSONParser().openConnection("products", "GET", params );
			// Log.d("jjjjjjjjjjjjjjjjjjjjjjj", result.toString());
			
			

		} catch (final Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		final Thread d = new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub

				try {
					if (CityDetailsParser.connect(con, AllURL
							.cityGuideDetailsURL(AllConstants.referrence,
									AllConstants.apiKey))) {
					}

				} catch (final Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}






				try {
					
			
					
					if (DrivingDetailsParser.connect(con, AllURL
							.drivingURL(AllConstants.UPlat, AllConstants.UPlng, AllConstants.Dlat, AllConstants.Dlng,
									AllConstants.apiKey))) {
					}

				} catch (final Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}


				try {
					if (BiCyleDetailsParser.connect(con, AllURL
							.bicycleURL(AllConstants.UPlat, AllConstants.UPlng, AllConstants.Dlat, AllConstants.Dlng,
									AllConstants.apiKey))) {
					}

				} catch (final Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				try {
					if (WalkingDetailsParser.connect(con, AllURL
							.walkURL(AllConstants.UPlat, AllConstants.UPlng, AllConstants.Dlat, AllConstants.Dlng,
									AllConstants.apiKey))) {
					}

				} catch (final Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}




				try {
					if (CityReviewParser.connect(con, AllURL
							.cityGuideDetailsURL(AllConstants.referrence,
									AllConstants.apiKey))) {

						PrintLog.myLog("Size of City : ", AllCityReview
								.getAllCityReview().size()
								+ "");

					}

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
//						Typeface title = Typeface.createFromAsset(getAssets(),
//								"fonts/ROBOTO-REGULAR.TTF");
//						Typeface add = Typeface.createFromAsset(getAssets(),
//								"fonts/ROBOTO-LIGHT.TTF");
						
						
						try {
							  JSONObject sa=new JSONObject(result);
								da=sa.getJSONObject("RetailigenceSearchResult");
								ss = da.getString("count").toString();
								it=da.getJSONArray("results");
								for(int i = 0; i<Integer.parseInt(ss);i++){
									 data=it.getJSONObject(i);
									 
									 sec= data.getJSONObject("SearchResult");
									 third= sec.getJSONObject("location");
									 product1= sec.getJSONObject("product");
									 id1 = product1.getString("id").toString();
									   if(id.equals(id1))
									   {
									
									    imageinfo = product1.getJSONArray("images");
									    image_obj = imageinfo.getJSONObject(1);
									    image1_obj = image_obj.getJSONObject("ImageInfo");
									    imgname = image1_obj.getString("link").toString();
									 
									    phone = third.getString("phone").toString();
									    desc = product1.getString("descriptionShort").toString(); 
									    price = sec.getString("price").toString(); 
									    url = product1.getString("url").toString(); 
									   
									    Picasso.with(con).load(imgname).into(image1);
								   	    cPhone.setText(phone); 
									    desc1.setText(desc);
			                            price1.setText(price+' '+"$");
									   }
									 
								
								}
								
								
									
								
							} catch (JSONException e) {
							e.printStackTrace();
						}
						
						if(AllConstants.NAME != null && AllConstants.ADD != null){
							
							
							
							
							cName.setText(AllConstants.NAME);
							cAdd.setText(AllConstants.ADD);
							
							
							AllConstants.lat = AllConstants.Dlat;
                            AllConstants.lng = AllConstants.Dlng;
                            
                         
                            
                            prod_name1.setText(AllConstants.PRODUCT);
                           
                            store1.setText(AllConstants.NAME);
                            
                           /* try {
                            	
                                URL url = new URL(logo);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setDoInput(true);
                                connection.connect();
                                InputStream input = connection.getInputStream();
                                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                                logo1.setImageBitmap(myBitmap);
                                
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d("kkkkkkkkkkkkkk","kkkkkkkkkkkkkkkkkkkk");
                            }*/
                           
                        	//aq.id(R.id.logo).image(logo.toString(), true, true, 0, R.drawable.not_found_banner);
                            
                           
							
							
							
							try {

								DT = DrivingDetails.getAlldrivingdetails()
										.elementAt(0);

							//	dDetails.setText(DT.getTime().trim());


								// ------Rating ---


							} catch (Exception e) {
								// TODO: handle exception
							}
							try {

							//	textDis.setText(DT.getDistance().trim());


								// ------Rating ---


							} catch (Exception e) {
								// TODO: handle exception
							}




							try {

								BT = BiCyleDetails.getAllBicyledetails()
										.elementAt(0);

							//	bDetails.setText(BT.getTime().trim());


								// ------Rating ---


							} catch (Exception e) {
								// TODO: handle exception
							}	try {

								WT = WalkingDetails.getAllWalkingDetails()
										.elementAt(0);

							//	wDetails.setText(WT.getTime().trim());


								// ------Rating ---


							} catch (Exception e) {
								// TODO: handle exception
							}
							
						}else{						
						try {

							CD = AllCityDetails.getAllCityDetails()
									.elementAt(0);


							try {
//								getActionBar().setTitle("Custom Text");
							//	cName.setText(CD.getName().trim());
							
								//cName.setText(s1);
								
//								cName.setTypeface(title);
							} catch (Exception e) {
								// TODO: handle exception
							}
							try {

						//		cAdd.setText(CD.getFormatted_address().trim());
								
								//cAdd.setText(s2);
								
//								cAdd.setTypeface(add);
							} catch (Exception e) {
								// TODO: handle exception
							}
							try {

						//		cPhone.setText(CD.getFormatted_phone_number().trim());
//								cPhone.setTypeface(add);
							} catch (Exception e) {
								// TODO: handle exception
							}

							try {

								AllConstants.lat = CD.getLat().trim();

								AllConstants.lng = CD.getLng().trim();
								PrintLog.myLog("GEO", AllConstants.lat);
							} catch (Exception e) {
								// TODO: handle exception
							}



							PrintLog.myLog("DDDLatLng : ",AllConstants.UPlat+" "+AllConstants.UPlng+" "+AllConstants.lat+" "+AllConstants.lng+" "
							+AllConstants.Dlat+" "+AllConstants.Dlng);


							// ------Rating ---

							String rating = CD.getRating();

							try {

								Float count = Float.parseFloat(rating);
								// PrintLog.myLog("Rating as float", count +
								// "");
						//		detailsRat.setRating(count);

							} catch (Exception e) {

								PrintLog.myLog("error at rating", e.toString());
							}


                                  //	Distance

							try {

								DT = DrivingDetails.getAlldrivingdetails()
										.elementAt(0);

						//		dDetails.setText(DT.getTime().trim());


								// ------Rating ---


							} catch (Exception e) {
								// TODO: handle exception
							}
							try {

						//		textDis.setText(DT.getDistance().trim());


								// ------Rating ---


							} catch (Exception e) {
								// TODO: handle exception
							}




							try {

								BT = BiCyleDetails.getAllBicyledetails()
										.elementAt(0);

						//		bDetails.setText(BT.getTime().trim());


								// ------Rating ---


							} catch (Exception e) {
								// TODO: handle exception
							}	try {

								WT = WalkingDetails.getAllWalkingDetails()
										.elementAt(0);

							//	wDetails.setText(WT.getTime().trim());


								// ------Rating ---


							} catch (Exception e) {
								// TODO: handle exception
							}

							// ---Photo---
							try {

								String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
										+ AllConstants.photoReferrence
										+ "&sensor=true&key="
										+ AllConstants.apiKey;
								PrintLog.myLog("imgUrl", imgUrl);

								final ImageView lImage = (ImageView) findViewById(R.id.imageViewL);

								if (AllConstants.photoReferrence.length() != 0) {

									downloader.download(imgUrl.trim(), lImage);

								}

								else {
							//		lImage.setImageBitmap(defaultBit);

									// downloader.download(AllConstants.detailsiconUrl.trim(),
									// lImage);
								}

							} catch (Exception e) {
								// TODO: handle exception
							}

						} catch (Exception e) {
							// TODO: handle exception
						}
						}
						if (AllCityReview.getAllCityReview().size() == 0) {

						} else {

							adapter = new RestaurantAdapter(con);
							list.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}

					}
				});

			}
		});
		d.start();
	}

	class RestaurantAdapter extends ArrayAdapter<ReviewList> {
		private final Context con;

		public RestaurantAdapter(Context context) {
			super(context, R.layout.review, AllCityReview.getAllCityReview());
			con = context;
			// TODO Auto-generated constructor stub

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				final LayoutInflater vi = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.review, null);
			}

			if (position < AllCityReview.getAllCityReview().size()) {
				final ReviewList CM = AllCityReview.getReviewList(position);

//				Typeface add = Typeface.createFromAsset(getAssets(),
//						"fonts/ROBOTO-LIGHT.TTF");

				// ----Address----

				final TextView address = (TextView) v
						.findViewById(R.id.AuthorName);

				try {
					address.setText(CM.getAuthor_name().toString().trim());
//					address.setTypeface(add);
				} catch (Exception e) {
					// TODO: handle exception
				}

				// final RatingBar rRating = (RatingBar)
				// v.findViewById(R.id.rRatingBar);
				try {

					String reviewRating = CM.getAuthor_rating();

					try {

						Float rcount = Float.parseFloat(reviewRating);
						// PrintLog.myLog("Rating as float", count +
						// "");
						// rRating.setRating(rcount);

					} catch (Exception e) {

						PrintLog.myLog("rRating:", reviewRating);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				// ---Image---

				final TextView name = (TextView) v.findViewById(R.id.reView);
				try {
					name.setText(CM.getAuthor_text().toString().trim());
//					name.setTypeface(add);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			// TODO Auto-generated method stub
			return v;
		}

	}

	public void call(View v) {
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + phone + ""));
			startActivity(callIntent);
		} catch (ActivityNotFoundException activityException) {

		}
	}

/*	public void cPhone(View v) {
		if (CD.getFormatted_phone_number().length() != 0) { 
			try {

				call();

				AllConstants.cCell = CD.getFormatted_phone_number().trim();

				PrintLog.myLog("Tel::", AllConstants.cCell);

			} catch (Exception e) {
				// TODO: handle exception
			}

		} else {

			Toast.makeText(ListDetailsActivity1.this, "Sorry!No Phone Number Found.",
					Toast.LENGTH_LONG).show();
		}

	}*/

	public void webView(View v) {

		//if (CD.getWebsite().length() != 0) {
			try {

			//	AllConstants.webUrl = CD.getWebsite().trim();
				AllConstants.webUrl = url;
				Intent next = new Intent(con, DroidWebViewActivity.class);
				next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(next);
				//PrintLog.myLog("Website::", AllConstants.cWeb);
			} catch (Exception e) {
				// TODO: handle exception
			}

		//} else {

		//	Toast.makeText(ListDetailsActivity1.this, "Sorry!No URL Found.",Toast.LENGTH_LONG).show();
		//}

	}
	private void ShareIntent() {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to Share.");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}
	public void mapViewBtn(View v) {

		Intent next = new Intent(con, MapViewActivity.class);
		next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(next);

	}

	public void btnHome(View v) {

		Intent next = new Intent(con, MainActivity.class);
		next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(next);

	}

	public void btnBack(View v) {
		finish();

	}
	
/*	public void gotobuy(View v) {
		
		
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);

	}*/
	
}

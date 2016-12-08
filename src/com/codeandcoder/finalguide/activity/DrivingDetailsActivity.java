package com.codeandcoder.finalguide.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import com.codeandcoder.finalguide.holder.DrivingDetails;
import com.codeandcoder.finalguide.model.CityDetailsList;
import com.codeandcoder.finalguide.model.DrivingTime;
import com.codeandcoder.finalguide.model.ReviewList;
import com.codeandcoder.finalguide.parser.CityDetailsParser;
import com.codeandcoder.finalguide.parser.CityReviewParser;
import com.codeandcoder.finalguide.parser.DrivingDetailsParser;

public class DrivingDetailsActivity extends Activity {


    private Context con;
    private String pos = "";
    private TextView dName;

    private ProgressDialog pDialog;
    private DrivingTime DT;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_details);

        dName = (TextView) findViewById(R.id.driveView);

        updateUI();
    }

    private void updateUI() {


        pDialog = ProgressDialog.show(this, "", "Loading..", false, false);

        final Thread d = new Thread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub

                try {
                    if (DrivingDetailsParser.connect(con, AllURL
                            .drivingURL(AllConstants.UPlat, AllConstants.UPlng, AllConstants.Dlat, AllConstants.Dlng,
                                    AllConstants.apiKey))) {
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

                            DT = DrivingDetails.getAlldrivingdetails()
                                    .elementAt(0);

                            dName.setText(DT.getTime().trim());


                            // ------Rating ---


                        } catch (Exception e) {
                            // TODO: handle exception
                        }


                    }
                });

            }
        });
        d.start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.testmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.codeandcoder.finalguide.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.codeandcoder.finalguide.R;
import com.codeandcoder.finalguide.extra.AllConstants;

public class FindUsActivity extends Activity {
    private Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_us);
        con = this;
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("FIND US");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main2, menu);
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


    // .............Top Bar Details Change--------------//

    public void btnFacebook(View v) {
        AllConstants.webUrl = "";

        Intent next = new Intent(con, DroidWebViewActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(next);
    }

    public void btnYoutube(View v) {
        AllConstants.webUrl = "https://www.youtube.com/channel/UC6SbumSQhI2QJtsc6DdRmTw";

        Intent next = new Intent(con, DroidWebViewActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(next);
    }

    public void btnLinkedin(View v) {
        AllConstants.webUrl = "https://www.linkedin.com/company/inspired-by-brands-inc";

        Intent next = new Intent(con, DroidWebViewActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(next);
    }

    public void btnGoogleplus(View v) {
        AllConstants.webUrl = "https://plus.google.com/u/0/b/110254623430515407539/110254623430515407539?hl=en";

        Intent next = new Intent(con, DroidWebViewActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(next);
    }

    public void btnTwitter(View v) {
        AllConstants.webUrl = "https://twitter.com/inspiredbybrand";

        Intent next = new Intent(con, DroidWebViewActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(next);
    }

    public void btnPinterest(View v) {
        AllConstants.webUrl = "www.pininterest.com";

        Intent next = new Intent(con, DroidWebViewActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(next);
    }

}

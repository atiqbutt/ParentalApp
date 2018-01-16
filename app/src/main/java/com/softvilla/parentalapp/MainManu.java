package com.softvilla.parentalapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainManu extends AppCompatActivity {



    Button msg,call,showcurrentLocation;
    String titleName = null;
    private android.support.v7.widget.Toolbar mToolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manu);



        Dexter.withActivity(this)
                .withPermissions(

                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {  }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {  }
        }).check();


//        titleName = getIntent().getExtras().getString("name");

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Toast.makeText(this, titleName, Toast.LENGTH_SHORT).show();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        // Set a title for toolbar
        mToolbar.setTitle(preferences.getString("name",titleName));
        mToolbar.setTitleTextColor(Color.WHITE);

        // Set support actionbar with toolbar
        this.setSupportActionBar(mToolbar);

        // Change the toolbar background color
        mToolbar.setBackgroundColor(Color.parseColor("#FF68BFD1"));
       /* mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                startActivity(new Intent(MainManu.this,ChildrenList.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });*/

       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
       // mToolbar.setNavigationIcon(android.support.v7.appcompat.R.dra‌​wable.abc_ic_ab_back‌​_material);


        /*ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle(titleName);*/


        msg = (Button) findViewById(R.id.showsms);
        call = (Button) findViewById(R.id.showcalllogs);
        showcurrentLocation = (Button) findViewById(R.id.showcurrentLocation);
    }

    public void msgs(View view) {
        startActivity(new Intent(this,Messages.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void calllogs(View view) {
        startActivity(new Intent(this,CallLogs.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }


    public void currentLocation(View view) {
        startActivity(new Intent(this,CurrentLocation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
       // finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(this,ChildrenList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        //finish();
        return true;
    }

    public void BatteryStatus(View view) {
        startActivity(new Intent(this,BatteryInfo.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void AppStatus(View view) {
        startActivity(new Intent(this,AppInfo.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}

package com.softvilla.parentalapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

public class ApplyPin extends AppCompatActivity {

    PinLockView mPinLockView;
    IndicatorDots mIndicatorDots;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String packageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_pin);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            packageName = extras.getString("packageName");
        }

        mPinLockView.setPinLockListener(
                new PinLockListener() {
                    @Override
                    public void onComplete(String pin) {

                        if(pin.equalsIgnoreCase(preferences.getString("pin",""))){
                           // if(packageName.equalsIgnoreCase("isPinSet")){
                                startActivity(new Intent(ApplyPin.this,ChildrenList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            //}
                            //else {
                                /*Utilities utilities = Utilities.findById(Utilities.class, (long) 1);
                                utilities.isLockApplied = true;
                                utilities.save();*/
                              //  Intent intent = ApplyPin.this.getPackageManager().getLaunchIntentForPackage(packageName);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                               // startActivity(intent);
                                //overridePendingTransition(0,0);
                            //}
                            /*Intent intent = ApplyPin.this.getPackageManager().getLaunchIntentForPackage(packageName);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(0,0);*/
                        }
                        else {
                            mPinLockView.resetPinLockView();
                            Toast.makeText(ApplyPin.this, "Wrong Pin Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onEmpty() {

                    }

                    @Override
                    public void onPinChange(int pinLength, String intermediatePin) {

                    }
                }
        );
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}

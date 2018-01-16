package com.softvilla.parentalapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BatteryInfo extends AppCompatActivity {
    private android.support.v7.widget.Toolbar mToolbar;
    int scale;
    int level;
    float percentage;
    Calendar calander;
    SimpleDateFormat simpledateformat;
    String Date;
    private Context mContext;

    private TextView mTextViewInfo;
    private TextView mTextViewPercentage;
    private TextView dateTime;
    private ProgressBar mProgressBar;

    private int mProgressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_info);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mContext = getApplicationContext();

        // Set a title for toolbar
        mToolbar.setTitle("Battery Status");
        mToolbar.setTitleTextColor(Color.WHITE);

        // Set support actionbar with toolbar
        this.setSupportActionBar(mToolbar);

        // Change the toolbar background color
        mToolbar.setBackgroundColor(Color.parseColor("#FF68BFD1"));

        mTextViewInfo = (TextView) findViewById(R.id.tv_info);
        mTextViewPercentage = (TextView) findViewById(R.id.tv_percentage);
        dateTime = (TextView) findViewById(R.id.dateTime);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);
        getBatteryStatus();
    }

    public void getBatteryStatus(){

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BatteryInfo.this);
        final ProgressDialog progressDialog = new ProgressDialog(BatteryInfo.this);
        progressDialog.setMessage("Fetching data....\n Please Wait");
        progressDialog.show();
        AndroidNetworking.post("http://noorpublicschool.com/ApiPractice/getBatteryStatus")
                .addBodyParameter("childId", ChildrenInfo.childId)// posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(BatteryInfo.this, response, Toast.LENGTH_SHORT).show();

                        try {
                            JSONArray jsonArray = new JSONArray(response);


                            for(int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                scale = Integer.parseInt(jsonObject1.getString("bettaryscale"));
                                mTextViewInfo.setText("Battery Scale : " + scale);
                                level = Integer.parseInt(jsonObject1.getString("bettarylevel"));
                                mTextViewInfo.setText(mTextViewInfo.getText() + "\nBattery Level : " + level);
                                percentage = Float.parseFloat(jsonObject1.getString("bettarypercentage"));
                                mProgressStatus = (int)((percentage)*100);
                                mTextViewPercentage.setText("" + mProgressStatus + "%");

                                // Show the battery charged percentage in TextView
                                mTextViewInfo.setText(mTextViewInfo.getText() + "\nPercentage : "+ mProgressStatus + "%");

                                // Display the battery charged percentage in progress bar
                                mProgressBar.setProgress(mProgressStatus);
                                Date = jsonObject1.getString("date");
                                dateTime.setText("Date Time : "+ Date);



                            }
                           // Toast.makeText(mContext, String.valueOf(scale), Toast.LENGTH_SHORT).show();
                            // Toast.makeText(CallLogs.this, calllog.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            // e.printStackTrace();
                            Toast.makeText(BatteryInfo.this, e.toString()+  "Try Catch", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(BatteryInfo.this, anError.toString()+  "FAN", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(this,MainManu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return true;
    }
}

package com.softvilla.parentalapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppInfo extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private TextView mTextView;
    private android.support.v7.widget.Toolbar mToolbar;
    private android.support.v7.widget.SwitchCompat switchCompat;
    EditText search;
    Recycler_View_Adapter_AppInfo adapter;



    List<AppInfoo> data;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        //search = (EditText) findViewById(R.id.search);
        //String packageName = intent.getData().getEncodedSchemeSpecificPart();

      //  switchCompat = (SwitchCompat)findViewById(R.id.switchButton);

       // switchCompat.setOnCheckedChangeListener(AppInfo.this);




        mContext = getApplicationContext();

        // Get the activity
        mActivity = AppInfo.this;

        // Get the widgets reference from XML layout

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        // Set a title for toolbar
        mToolbar.setTitle("Application Info");
        mToolbar.setTitleTextColor(Color.WHITE);

        // Set support actionbar with toolbar
        this.setSupportActionBar(mToolbar);

        // Change the toolbar background color
        mToolbar.setBackgroundColor(Color.parseColor("#FF68BFD1"));

        data = new ArrayList<AppInfoo>();


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        Recycler_View_Adapter_AppInfo adapter = new Recycler_View_Adapter_AppInfo(data, this);
        recyclerView.setAdapter(adapter);
//        adapter.setOnCardClickListner((Recycler_View_Adapter.OnCardClickListner) this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        getApps();

    }

    public void getApps() {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AppInfo.this);
        final ProgressDialog progressDialog = new ProgressDialog(AppInfo.this);
        progressDialog.setMessage("Fetching data....\n Please Wait");
        progressDialog.show();
        AndroidNetworking.post("http://noorpublicschool.com/ApiPractice/getAppList")
                .addBodyParameter("childId", ChildrenInfo.childId)// posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        // JSONArray jsonArray = null;
//                        List<Child> child = Child.listAll(Child.class);
                        //Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                        try {


                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                AppInfoo obj = new AppInfoo();
                                obj.userName = jsonObject1.getString("appName");
                                obj.img = jsonObject1.getString("icon");
                                obj.pkgName = jsonObject1.getString("pkgName");
                                obj.isLock = Integer.parseInt(jsonObject1.getString("is_lock"));
                                obj.time = Integer.parseInt(jsonObject1.getString("time"));

                                //obj.id = jsonObject1.getString("");
                                data.add(obj);



                                //Toast.makeText(mContext, ChildrenInfo.childId, Toast.LENGTH_SHORT).show();
                                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
                                adapter = new Recycler_View_Adapter_AppInfo(data, AppInfo.this);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(AppInfo.this));}

                          //  search= (EditText) findViewById(R.id.search);
                            /*search.addTextChangedListener(new TextWatcher() {

                                public void onTextChanged(CharSequence s, int start, int before, int count) {



                                }

                                public void beforeTextChanged(CharSequence s, int start, int count,
                                                              int after) {


                                }

                                public void afterTextChanged(Editable s) {

                                }
                            });*/


                            progressDialog.dismiss();
                            if(data.size() == 0){

                                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AppInfo.this, R.style.LightDialogTheme);
                                builder.setMessage("No Apps Available...!")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(final DialogInterface dialog, final int id) {

                                                //ChildrenList.this.finish();
                                                //startActivity(new Intent(ChildrenList.this,MainManu.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                                //finish();
                                                //CurrentLocation.this.overridePendingTransition(0,0);
                                            }
                                        });
                                final android.support.v7.app.AlertDialog alert = builder.create();
                                alert.show();
                                // Toast.makeText(mContext, "Please Scane the QR Code.", Toast.LENGTH_SHORT).show();
                                //TextView textView = new TextView(ChildrenList.this);
                                //textView.setText("Please Scane the QR Code.");
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            // e.printStackTrace();
                          //  Toast.makeText(AppInfo.this, e.toString() + "Try Catch", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(AppInfo.this, "Server Error....!"/*anError.toString() + "FAN"*/, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(this,MainManu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return true;
    }



    }


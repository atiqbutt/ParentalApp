package com.softvilla.parentalapp;

import android.annotation.SuppressLint;
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
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildrenList extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private TextView mTextView;
    private android.support.v7.widget.Toolbar mToolbar;
    private IntentIntegrator qrScan;
    IntentResult result;


    List<ChildrenInfo> data;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_list);

        mContext = getApplicationContext();

        // Get the activity
        mActivity = ChildrenList.this;

        qrScan = new IntentIntegrator(this);

        // Get the widgets reference from XML layout

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        // Set a title for toolbar
        mToolbar.setTitle("Children List");
        mToolbar.setTitleTextColor(Color.WHITE);

        // Set support actionbar with toolbar
        this.setSupportActionBar(mToolbar);

        // Change the toolbar background color
        mToolbar.setBackgroundColor(Color.parseColor("#FF68BFD1"));

        data = new ArrayList<ChildrenInfo>();


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        Recycler_View_Adapter adapter = new Recycler_View_Adapter(data, this);
        recyclerView.setAdapter(adapter);
//        adapter.setOnCardClickListner((Recycler_View_Adapter.OnCardClickListner) this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        getChildList();
    }



    public void getChildList() {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChildrenList.this);
        final ProgressDialog progressDialog = new ProgressDialog(ChildrenList.this);
        progressDialog.setMessage("Fetching data....\n Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.post("http://noorpublicschool.com/ApiPractice/getChildList")
                .addBodyParameter("childId", preferences.getString("parent_id", ""))// posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                       // JSONArray jsonArray = null;
//                        List<Child> child = Child.listAll(Child.class);
                        try {


                            JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i<jsonArray.length();i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    ChildrenInfo obj = new ChildrenInfo();
                                    obj.userName = jsonObject1.getString("name");
                                    obj.id = jsonObject1.getString("childId");
                                    data.add(obj);


                                //Toast.makeText(mContext, ChildrenInfo.childId, Toast.LENGTH_SHORT).show();
                                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(ChildrenList.this);
                                mLayoutManager.setReverseLayout(true);
                                mLayoutManager.setStackFromEnd(true);
                                Recycler_View_Adapter adapter = new Recycler_View_Adapter(data, ChildrenList.this);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(mLayoutManager);
                                }


                            if(data.size() == 0){

                                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ChildrenList.this, R.style.LightDialogTheme);
                                builder.setMessage("Please Scan the QR Code For Add your Child.....!")
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

                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            // e.printStackTrace();
                            //Toast.makeText(ChildrenList.this, e.toString() + " Try Catch", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(ChildrenList.this, "Server Error.....!"/*anError.toString() + " FAN"*/, Toast.LENGTH_SHORT).show();

                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.scan:
                qrScan.initiateScan();
                //startActivity(new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                // Set the text color to red
                // mTextView.setTextColor(Color.RED);
                return true;
            /*case R.id.galley:
                startActivity(new Intent(this,Gallery.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            try {
                UpdateChildWithParent();

              //  startActivity(new Intent(MainActivity.this, ChildrenList.class)/*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)*/);
            }catch (Exception e){
                Toast.makeText(this, "No Related Data....", Toast.LENGTH_SHORT).show();
            }
            //if qrcode has nothing in it
            if (result == null) {
                /*final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this, R.style.LightDialogTheme);
                builder.setMessage("No Result Found....!")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                MainActivity.this.finish();
                                //startActivity(new Intent(MainActivity.this,MainManu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                //finish();
                                //CurrentLocation.this.overridePendingTransition(0,0);
                            }
                        });
                final android.support.v7.app.AlertDialog alert = builder.create();
                alert.show();*/
                //startActivity(new Intent(MainActivity.this, ChildrenList.class)/*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)*/);
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
                 finish();
            } else {
                //if qr contains data
                // Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();


                //converting the data to json
                // JSONObject obj = new JSONObject(result.getContents());
                //setting values to textviews
                //textViewName.setText(obj.getString("child_id"));
                //textViewAddress.setText(obj.getString("address"));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //styleMenuButton();
        MenuItem settingsMenuItem = mToolbar.getMenu().findItem(R.id.scan);
        SpannableString s = new SpannableString(settingsMenuItem.getTitle().toString());
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        settingsMenuItem.setTitle(s);

        return super.onPrepareOptionsMenu(menu);
    }


    public void UpdateChildWithParent(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Signing in...");
        pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
        // pDialog.show();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChildrenList.this);

        AndroidNetworking.post("http://noorpublicschool.com/ApiPractice/updateChildWithParent")
                .addBodyParameter("child_id", String.valueOf(result.getContents()))
                .addBodyParameter("parent_id", preferences.getString("parent_id",""))

                //.addBodyParameter("password", Password)
                //.addBodyParameter("child_id", id)

                //.addBodyParameter("phone", Phno)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChildrenList.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("child_id",response);
                        editor.apply();
                        startActivity(new Intent(ChildrenList.this,ChildrenList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        // pDialog.dismiss();

                    }


                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(ChildrenList.this, "Server Error.....!"/*error.toString()+"klk"*/, Toast.LENGTH_SHORT).show();
                        // handle error
                        pDialog.dismiss();
                    }
                });

    }


}

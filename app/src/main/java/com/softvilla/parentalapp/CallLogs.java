package com.softvilla.parentalapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

public class CallLogs extends AppCompatActivity {

   // TextView calllog;
    String name,number,calltype;
    List<CallLogInfo> data;
    RecyclerView recyclerView;
    private android.support.v7.widget.Toolbar mToolbar;
    private Context mContext;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_logs);
        //calllog = (TextView) findViewById(R.id.calllog);

        mContext = getApplicationContext();

        // Get the activity
        mActivity = CallLogs.this;

        // Get the widgets reference from XML layout

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        // Set a title for toolbar
        mToolbar.setTitle("Call Log Detail");
        mToolbar.setTitleTextColor(Color.WHITE);

        // Set support actionbar with toolbar
        this.setSupportActionBar(mToolbar);

        // Change the toolbar background color
        mToolbar.setBackgroundColor(Color.parseColor("#FF68BFD1"));

        data = new ArrayList<CallLogInfo>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        Recycler_View_Adapter_CallLog adapter = new Recycler_View_Adapter_CallLog(data, this);
        recyclerView.setAdapter(adapter);
//        adapter.setOnCardClickListner((Recycler_View_Adapter.OnCardClickListner) this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        getCallLogDetail();
    }

    public void getCallLogDetail(){

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CallLogs.this);
        final ProgressDialog progressDialog = new ProgressDialog(CallLogs.this);
        progressDialog.setMessage("Fetching data....\n Please Wait");
        progressDialog.show();
        AndroidNetworking.post("http://noorpublicschool.com/ApiPractice/getCallLogDetail")
                .addBodyParameter("childId", ChildrenInfo.childId)// posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(CallLogs.this, "Enter OnResponse", Toast.LENGTH_SHORT).show();

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                CallLogInfo obj = new CallLogInfo();
                                obj.name = jsonObject1.getString("name");
                                obj.number = jsonObject1.getString("phone_number");
                                obj.duration = jsonObject1.getString("call_duration");
                                obj.date = jsonObject1.getString("call_date");
                                obj.type = jsonObject1.getString("call_type");
                                data.add(obj);
                                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
                                Recycler_View_Adapter_CallLog adapter = new Recycler_View_Adapter_CallLog(data, CallLogs.this);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(CallLogs.this);
                                mLayoutManager.setReverseLayout(true);
                                mLayoutManager.setStackFromEnd(true);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(mLayoutManager);
                                /*calllog.append("***********Call Log Detail*************\n");
                                number = jsonObject1.getString("phone_number");
                                name = jsonObject1.getString("name");
                                calltype = jsonObject1.getString("call_type");
                                calllog.append("Number: "+number+"\n"+"Name: "+name+"\n"+"CallType: "+calltype+"\n");*/

                                //textViewAddress.setText(number+"\n"+name+"\n"+body);
                            }
                           // Toast.makeText(CallLogs.this, calllog.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            // e.printStackTrace();
                           // Toast.makeText(CallLogs.this, e.toString()+  "Try Catch", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CallLogs.this, "Server Error......!"/*anError.toString()+  "FAN"*/, Toast.LENGTH_SHORT).show();
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

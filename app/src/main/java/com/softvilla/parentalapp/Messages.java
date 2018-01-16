package com.softvilla.parentalapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class Messages extends AppCompatActivity {

    TextView msgs;
    //String name,number,body;
    List<MessageInfo> data;
    RecyclerView recyclerView;
    private android.support.v7.widget.Toolbar mToolbar;
    private Context mContext;
    private Activity mActivity;
    protected Handler handler;
    final int[] id = {0};
    boolean isComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        //invalidateOptionsMenu();


        mContext = getApplicationContext();
        handler = new Handler();
        // Get the activity
        mActivity = Messages.this;

        // Get the widgets reference from XML layout

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        // Set a title for toolbar
        mToolbar.setTitle("Messages Detail");
        mToolbar.setTitleTextColor(Color.WHITE);

        // Set support actionbar with toolbar
        this.setSupportActionBar(mToolbar);

        // Change the toolbar background color
        mToolbar.setBackgroundColor(Color.parseColor("#FF68BFD1"));


        data = new ArrayList<MessageInfo>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);

        Recycler_View_Adapter_Messages adapter = new Recycler_View_Adapter_Messages(data, recyclerView);
        recyclerView.setAdapter(adapter);
//        adapter.setOnCardClickListner((Recycler_View_Adapter.OnCardClickListner) this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

       // msgs = (TextView) findViewById(R.id.msgs);
        getMessageDetail();
    }

    public void getMessageDetail(){

        //Toast.makeText(this, ChildrenInfo.childId, Toast.LENGTH_LONG).show();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Messages.this);
        final ProgressDialog progressDialog = new ProgressDialog(Messages.this);
        progressDialog.setMessage("Fetching data....\n Please Wait");
        progressDialog.show();


        AndroidNetworking.post("http://noorpublicschool.com/ApiPractice/getSmsDetail")
                .addBodyParameter("childId", ChildrenInfo.childId)// posting json
                .addBodyParameter("id", String.valueOf(id[0]))// posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(Messages.this, "Enter OnResponse", Toast.LENGTH_SHORT).show();

                        try {
                            final JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                MessageInfo obj = new MessageInfo();

                                obj.name = jsonObject1.getString("name");
                                obj.number = jsonObject1.getString("phone_number");
                                obj.body = jsonObject1.getString("message_body");
                                obj.date = jsonObject1.getString("message_date");
                                obj.type = jsonObject1.getString("message_type");
                                data.add(obj);

                                id[0] = Integer.parseInt(jsonObject1.getString("id"));
                               // Toast.makeText(mContext, String.valueOf(id[0]), Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
                               /* recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                        getMessageDetail();
                                        super.onScrolled(recyclerView, dx, dy);
                                    }
                                });*/
                                final Recycler_View_Adapter_Messages adapter = new Recycler_View_Adapter_Messages(data, recyclerView);
                                /*recyclerView.setHasFixedSize(false);
                                adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {
                                        //add null , so the adapter will check view_type and show progress bar at bottom
                                        data.add(null);
                                        adapter.notifyItemInserted(data.size() - 1);

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //   remove progress item
                                                data.remove(data.size() - 1);
                                                adapter.notifyItemRemoved(data.size());
                                                //add items one by one
                                                int start = data.size();
                                                int end = start + 20;

                                                for (int i = start + 1; i <= end; i++) {

                                                    try {
                                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                        obj.name = jsonObject1.getString("name");
                                                        obj.number = jsonObject1.getString("phone_number");
                                                        obj.body = jsonObject1.getString("message_body");
                                                        obj.date = jsonObject1.getString("message_date");
                                                        obj.type = jsonObject1.getString("message_type");
                                                        data.add(obj);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    adapter.notifyItemInserted(data.size());
                                                }
                                                adapter.setLoaded();
                                                //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                            }
                                        }, 2000);

                                    }
                                });*/
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(Messages.this);
                                /*mLayoutManager.setReverseLayout(true);
                                mLayoutManager.setStackFromEnd(true);*/
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(mLayoutManager);
                                /*msgs.append("***********Messages Detail*************\n");
                                number = jsonObject1.getString("phone_number");
                                name = jsonObject1.getString("name");
                                body = jsonObject1.getString("message_body");
                                msgs.append("Number: "+number+"\n"+"Name: "+name+"\n"+"Message: "+body+"\n");

                                //textViewAddress.setText(number+"\n"+name+"\n"+body);*/
                            }
                            if(jsonArray.length() == 0){
                                isComplete = true;
                            }
                           // Toast.makeText(Messages.this, msgs.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            if(data.size() == 0){
                                Toast.makeText(mContext, "No data Available.", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            // e.printStackTrace();
                           // Toast.makeText(Messages.this, e.toString()+  "Try Catch", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(Messages.this, "Server Error......!"/*anError.toString()+  "FAN"*/, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.loadMore:
                if(!isComplete){
                    getMessageDetail();
                }

                //startActivity(new Intent(this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                // Set the text color to red
                // mTextView.setTextColor(Color.RED);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean result = super.onPrepareOptionsMenu(menu);
        return result;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(this,MainManu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return true;
    }

}

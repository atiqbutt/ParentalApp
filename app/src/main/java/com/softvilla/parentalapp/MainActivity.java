package com.softvilla.parentalapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //View Objects
    private Button buttonScan;
    private TextView textViewName, textViewAddress;
    String number,name,body;
   // private EditText userName,password;
    //qr code scanner object
    private IntentIntegrator qrScan;
    IntentResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View objects
        //buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.DatatextView);
        //textViewAddress = (TextView) findViewById(R.id.textViewAddress);

        //intializing scan object
        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();
        //attaching onclick listener
//        buttonScan.setOnClickListener(this);
        if(!haveNetworkConnection()){
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this, R.style.LightDialogTheme);
            builder.setMessage("No Internet Connection....!")
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
            alert.show();
        }
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            try {
                UpdateChildWithParent();

                startActivity(new Intent(MainActivity.this, ChildrenList.class)/*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)*/);
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
                startActivity(new Intent(MainActivity.this, ChildrenList.class)/*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)*/);
               Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
               // finish();
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
    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        //getCallLogDetail();

    }




    public void UpdateChildWithParent(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Signing in...");
        pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
       // pDialog.show();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

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
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("child_id",response);
                        editor.apply();
                        startActivity(new Intent(MainActivity.this,ChildrenList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                       // pDialog.dismiss();

                    }


                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(MainActivity.this, error.toString()+"klk", Toast.LENGTH_SHORT).show();
                        // handle error
                        pDialog.dismiss();
                    }
                });

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}

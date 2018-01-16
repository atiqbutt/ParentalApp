package com.softvilla.parentalapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

//import android.location.LocationListener;

public class CurrentLocation extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener{


    Geocoder geocoder;
    List<Address> addresses;
    Location loc;
    StringBuilder str;

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LatLng latLng;
    double Lat,Lng;
    String locationName;
    LocationManager locationManager;
    String provider;
    String lat,lng;
    String localityString,city,region_code,zipcode,street,house,locale;
    private android.support.v7.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        // Set a title for toolbar
        mToolbar.setTitle("Current Location");
        mToolbar.setTitleTextColor(Color.WHITE);

        // Set support actionbar with toolbar
        this.setSupportActionBar(mToolbar);
        mToolbar.setTitle("Current Location");

        // Change the toolbar background color
        mToolbar.setBackgroundColor(Color.parseColor("#FF68BFD1"));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(CurrentLocation.this);
    }


    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
       // mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);

            }

        final ProgressDialog progressDialog = new ProgressDialog(CurrentLocation.this);
        progressDialog.setMessage("Fetching data....\n Please Wait");
        progressDialog.show();
        AndroidNetworking.post("http://noorpublicschool.com/ApiPractice/getLocation")
                .addBodyParameter("childId", ChildrenInfo.childId)// posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(CurrentLocation.this, response, Toast.LENGTH_SHORT).show();

                        try {
                            String[] seperated = response.split(",");
                            lat = String.valueOf(Double.parseDouble(seperated[0]));
                            lng = String.valueOf(Double.parseDouble(seperated[1]));


                            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                           // Toast.makeText(CurrentLocation.this, String.valueOf(latLng), Toast.LENGTH_SHORT).show();

                            if (Double.parseDouble(lat) == 0.0 && Double.parseDouble(lng) == 0.0) {
                                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(CurrentLocation.this, R.style.LightDialogTheme);
                                builder.setMessage("No Coordinates Found. May be Your Child's GPS Is Off....!")
                                        .setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(final DialogInterface dialog, final int id) {
                                                CurrentLocation.this.finish();
                                                startActivity(new Intent(CurrentLocation.this,MainManu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                //finish();
                                                //CurrentLocation.this.overridePendingTransition(0,0);
                                            }
                                        });
                                final android.support.v7.app.AlertDialog alert = builder.create();
                                alert.show();
                               // Toast.makeText(CurrentLocation.this, "No Coordinates Found. May be Your Child GPS Is Off.", Toast.LENGTH_LONG).show();

                            } else {


                               /* Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                List<Address> list = null;

                                try {
                                    list = geocoder.getFromLocation(Lat, Lng, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                if (list != null & list.size() > 0) {
                                    Address address = list.get(0);
                                    locationName = address.getLocality();
                                    //return locationName;

                                    try {
                                        geocoder = new Geocoder(CurrentLocation.this, Locale.ENGLISH);
                                        addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
                                        str = new StringBuilder();
                                        if (geocoder.isPresent()) {
                                            Toast.makeText(getApplicationContext(),
                                                    "geocoder present", Toast.LENGTH_SHORT).show();
                                            Address returnAddress = addresses.get(0);

                                            for (Address address1 : addresses) {
                                                String outputAddress = "";
                                                for (int i = 0; i < address1.getMaxAddressLineIndex(); i++) {
                                                    localityString += " --- " + address1.getAddressLine(i);
                                                }
                                                //localityString = returnAddress.getLocality();
                                                city = returnAddress.getCountryName();
                                                region_code = returnAddress.getCountryCode();
                                                zipcode = returnAddress.getPostalCode();
                                                street = returnAddress.getAddressLine(2);
                                                house = returnAddress.getSubAdminArea();
                                                locale = String.valueOf(returnAddress.getMaxAddressLineIndex());

                                                // str.append(localityString + "");
                                                //str.append(city + "" + region_code + "");
                                                //str.append(zipcode + "");

                                                //address.setText(str);
                                                Toast.makeText(getApplicationContext(), str,
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "geocoder not present", Toast.LENGTH_SHORT).show();
                                        }

// } else {
// Toast.makeText(getApplicationContext(),
// "address not available", Toast.LENGTH_SHORT).show();
// }
                                    } catch (IOException e) {
// TODO Auto-generated catch block

                                        Log.e("tag", e.getMessage());
                                    }*/

                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(latLng);
                            //        markerOptions.title("Info").snippet(localityString + "\n" + city + "\n" + region_code + "\n" + zipcode/*+"\n"+street+"\n"+house+"\n"+locale*/);
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                                    mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                        @Override
                                        public View getInfoWindow(Marker marker) {
                                            return null;
                                        }

                                        @Override
                                        public View getInfoContents(Marker marker) {
                                            LinearLayout info = new LinearLayout(CurrentLocation.this);
                                            info.setOrientation(LinearLayout.VERTICAL);

                                            TextView title = new TextView(CurrentLocation.this);
                                            title.setTextColor(Color.BLACK);
                                            title.setGravity(Gravity.CENTER);
                                            title.setTypeface(null, Typeface.BOLD);
                                            title.setText(marker.getTitle());

                                            TextView snippet = new TextView(CurrentLocation.this);
                                            snippet.setTextColor(Color.GRAY);
                                            snippet.setText(marker.getSnippet());

                                            info.addView(title);
                                            info.addView(snippet);

                                            return info;
                                        }
                                    });

                                    //move map camera
                                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

                                  //  Toast.makeText(CurrentLocation.this, lat + lng, Toast.LENGTH_SHORT).show();
                                }


                            }catch(Exception e){
                              //  Toast.makeText(CurrentLocation.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(this, String.valueOf(latLng), Toast.LENGTH_SHORT).show();


                            // Toast.makeText(CurrentLocation.this, (int) (lat+lng), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();



                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CurrentLocation.this, "Server Error.......!"/*anError.toString()+  "FAN"*/, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });






       // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  CurrentLocation.this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
       /* mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }*/

        //Place current location marker

       /* MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));*/

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(CurrentLocation.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

   /* public void getCoordinates(){

        Toast.makeText(this, ChildrenInfo.childId, Toast.LENGTH_LONG).show();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CurrentLocation.this);
        final ProgressDialog progressDialog = new ProgressDialog(CurrentLocation.this);
        progressDialog.setMessage("Fetching data....\n Please Wait");
        progressDialog.show();
        AndroidNetworking.post("http://noorpublicschool.com/ApiPractice/getLocation")
                .addBodyParameter("childId", ChildrenInfo.childId)// posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CurrentLocation.this, "Enter OnResponse", Toast.LENGTH_SHORT).show();

                        try {



                            JSONObject jsonObject1 = new JSONObject(response);
                            Lat = Double.parseDouble(jsonObject1.getString("Lat"));//jsonObject1.getDouble("coordinates");
                            Lng = Double.parseDouble(jsonObject1.getString("Lng"));

                            *//*JSONArray jsonArray = new JSONArray(response);


                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                msgs.append("***********Messages Detail*************\n");
                                number = jsonObject1.getString("phone_number");
                                name = jsonObject1.getString("name");
                                body = jsonObject1.getString("message_body");*//*

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            // e.printStackTrace();
                            Toast.makeText(CurrentLocation.this, e.toString()+  "Try Catch", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CurrentLocation.this, anError.toString()+  "FAN", Toast.LENGTH_SHORT).show();
                         progressDialog.dismiss();
                    }
                });
    }*/



    /*@Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }*/


    /*public class MyLocationListner implements LocationListener {

        @SuppressWarnings("static-access")
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onLocationChanged(Location location) {
// TODO Auto-generated method stub

           // getLatitude = "" + location.getLatitude();
            //getLongitude = "" + location.getLongitude();

          //  lati.setText(getLatitude);
           // longi.setText(getLongitude);

            //x = location.getLatitude();
            //y = location.getLongitude();

            try {
                geocoder = new Geocoder(CurrentLocation.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
                StringBuilder str = new StringBuilder();
                if (geocoder.isPresent()) {
                    Toast.makeText(getApplicationContext(),
                            "geocoder present", Toast.LENGTH_SHORT).show();
                    Address returnAddress = addresses.get(0);

                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();

                    str.append(localityString + "");
                    str.append(city + "" + region_code + "");
                    str.append(zipcode + "");

                    //address.setText(str);
                    Toast.makeText(getApplicationContext(), str,
                            Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "geocoder not present", Toast.LENGTH_SHORT).show();
                }

// } else {
// Toast.makeText(getApplicationContext(),
// "address not available", Toast.LENGTH_SHORT).show();
// }
            } catch (IOException e) {
// TODO Auto-generated catch block

                Log.e("tag", e.getMessage());
            }

        }

}*/

    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(this,MainManu.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return true;
    }

}

package com.softvilla.parentalapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by Malik on 01/08/2017.
 */

public class Recycler_View_Adapter_AppInfo extends RecyclerView.Adapter<View_Holder_AppInfo> {

    List<AppInfoo> list = Collections.emptyList();
    Context context;
    OnCardClickListner onCardClickListner;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private android.support.v7.widget.SwitchCompat switchCompat;
    String isLock = "1";
    String isNoLock = "0";




    public Recycler_View_Adapter_AppInfo(List<AppInfoo> list, Context context) {
        this.list = list;
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();

    }

    @Override
    public View_Holder_AppInfo onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_appinfo, parent, false);
        View_Holder_AppInfo holder = new View_Holder_AppInfo(v);
        return holder;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final View_Holder_AppInfo holder, final int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.name.setText(list.get(position).userName);
        if(list.get(position).isLock==1){
            holder.switchCompat.setChecked(true);
        }
        holder.packegName.setText(list.get(position).time+" minutes used today.");
        //holder.description.setText(list.get(position).description);
        //Glide.with(context).load(list.get(position).img).into(holder.imageView);
        //holder.imageView.setImageBitmap(getBitmapFromURL(list.get(position).img));
        Picasso.with(context).load(list.get(position).img).placeholder(R.drawable.placeholder).into(holder.imageView);

        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true) {
                   // Toast.makeText(context, "true", Toast.LENGTH_SHORT).show();
                    final ProgressDialog pDialog = new ProgressDialog(context);
                    pDialog.setMessage("Locking...");
                    pDialog.show();
                    pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
                    // pDialog.show();
                    final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

                    AndroidNetworking.post("http://noorpublicschool.com/ApiPractice/CheckLock")
                            .addBodyParameter("pkgName", list.get(position).pkgName)
                            .addBodyParameter("id", ChildrenInfo.childId)
                            .addBodyParameter("is_lock", isLock)
                            //.addBodyParameter("child_id", id)

                            //.addBodyParameter("phone", Phno)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "Successfully Locked", Toast.LENGTH_SHORT).show();
                                    pDialog.dismiss();
                                }


                                @Override
                                public void onError(ANError error) {
                                   // Toast.makeText(context, error.toString()+"klk", Toast.LENGTH_SHORT).show();
                                    // handle error
                                    pDialog.dismiss();
                                }
                            });

                }
                else{
                   // Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
                    final ProgressDialog pDialog = new ProgressDialog(context);
                    pDialog.setMessage("UnLocking...");
                    pDialog.show();
                    pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
                    // pDialog.show();
                    final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

                    AndroidNetworking.post("http://noorpublicschool.com/ApiPractice/CheckLock")
                            .addBodyParameter("pkgName", list.get(position).pkgName)
                            .addBodyParameter("id", ChildrenInfo.childId)
                            .addBodyParameter("is_lock", isNoLock)
                            //.addBodyParameter("child_id", id)

                            //.addBodyParameter("phone", Phno)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    pDialog.dismiss();
                                    Toast.makeText(context, "Successfully UnLocked", Toast.LENGTH_SHORT).show();
                                }


                                @Override
                                public void onError(ANError error) {
                                   // Toast.makeText(context, error.toString()+"klk", Toast.LENGTH_SHORT).show();
                                    // handle error
                                    pDialog.dismiss();
                                }
                            });
                }
            }
        });
        //holder.imageView.setImageResource(list.get(position).imageId);
        /*holder.cv.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
               // final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                 //String childId = preferences.getString("child_id","");
                 AppInfoo.pakgName = list.get(position).pkgName;
                Toast.makeText(context, AppInfoo.pakgName, Toast.LENGTH_SHORT).show();
                //AttendanceInfo.promId = list.get(position).promId;
                //AttendanceInfo.name = list.get(position).name;
                //context.startActivity(new Intent(context, MainManu.class).putExtra("name",list.get(position).userName).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
               // editor.putString("name",list.get(position).userName);
                //editor.apply();

                //Toast.makeText(context, String.valueOf(list.get(position).userName), Toast.LENGTH_SHORT).show();
            }
        });*/

        /*if(list.get(position).text==0){
            holder.notification.setVisibility(View.INVISIBLE);
        }
        else {
            holder.notification.setVisibility(View.VISIBLE);
            holder.notification.setText(String.valueOf(list.get(position).text));

        }*/


        //animate(holder);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, AppInfoo data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(ChildrenInfo data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }


    public interface OnCardClickListner {
        void OnCardClicked(View view, int position);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    public void UpdateChildWithParent(){
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Signing in...");
        pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
        // pDialog.show();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        AndroidNetworking.post("http://noorpublicschool.com/ApiPractice/updateChildWithParent")
                .addBodyParameter("package_name", AppInfoo.pakgName)
                .addBodyParameter("id", ChildrenInfo.childId)
                .addBodyParameter("is_lock", isLock)
                //.addBodyParameter("child_id", id)

                //.addBodyParameter("phone", Phno)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("child_id",response);
                        editor.apply();
                        */
                        // pDialog.dismiss();

                    }


                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(context, "Server Error......!"/*error.toString()+"klk"*/, Toast.LENGTH_SHORT).show();
                        // handle error
                        pDialog.dismiss();
                    }
                });

    }
}

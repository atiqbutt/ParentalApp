package com.softvilla.parentalapp;

import android.content.Context;
import android.content.Intent;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by Malik on 01/08/2017.
 */

public class Recycler_View_Adapter extends RecyclerView.Adapter<View_Holder> {

    List<ChildrenInfo> list = Collections.emptyList();
    Context context;
    OnCardClickListner onCardClickListner;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;




    public Recycler_View_Adapter(List<ChildrenInfo> list, Context context) {
        this.list = list;
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(View_Holder holder, final int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.name.setText(list.get(position).userName);
        //holder.description.setText(list.get(position).description);
        //Glide.with(context).load(list.get(position).img).into(holder.imageView);
        //holder.imageView.setImageBitmap(getBitmapFromURL(list.get(position).img));
        //Picasso.with(context).load(list.get(position).img).into(holder.imageView);

        //holder.imageView.setImageResource(list.get(position).imageId);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                 //String childId = preferences.getString("child_id","");
                 ChildrenInfo.childId = list.get(position).id;
                //AttendanceInfo.promId = list.get(position).promId;
                //AttendanceInfo.name = list.get(position).name;
                context.startActivity(new Intent(context, MainManu.class)/*.putExtra("name",list.get(position).userName)*/.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                editor.putString("name",list.get(position).userName);
                editor.apply();

                //Toast.makeText(context, String.valueOf(list.get(position).userName), Toast.LENGTH_SHORT).show();
            }
        });

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
    public void insert(int position, ChildrenInfo data) {
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
}

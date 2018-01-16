package com.softvilla.parentalapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by Malik on 01/08/2017.
 */

public class Recycler_View_Adapter_Messages extends RecyclerView.Adapter {

    List<MessageInfo> list = Collections.emptyList();
    Context context;
    OnCardClickListner onCardClickListner;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;



    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;




    public Recycler_View_Adapter_Messages(List<MessageInfo> list, RecyclerView recyclerView) {
        this.list = list;
        this.context = context;


        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
       /* View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_message, parent, false);
        View_Holder_Message holder = new View_Holder_Message(v);
        return holder;
*/
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_message, parent, false);

            vh = new View_Holder_Message(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView

        if (holder instanceof View_Holder_Message) {
            ((View_Holder_Message) holder).name.setText("Name: " + list.get(position).name);
            ((View_Holder_Message) holder).number.setText("Ph.Number: " + list.get(position).number);
            ((View_Holder_Message) holder).type.setText("SMS Type: " + list.get(position).type);
            ((View_Holder_Message) holder).date.setText("Date: " + list.get(position).date);
            ((View_Holder_Message) holder).body.setText("SMS Body: " + list.get(position).body);
            //holder.description.setText(list.get(position).description);
            //Glide.with(context).load(list.get(position).img).into(holder.imageView);
            //holder.imageView.setImageBitmap(getBitmapFromURL(list.get(position).img));
            //Picasso.with(context).load(list.get(position).img).into(holder.imageView);

        }
        else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

        //holder.imageView.setImageResource(list.get(position).imageId);
       // holder.cv.setOnClickListener(new View.OnClickListener() {



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
    public void insert(int position, MessageInfo data) {
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

    @Override
    public int getItemViewType(int position) {
        return list.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public void setLoaded() {
        loading = false;
    }



    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }



    public class View_Holder_Message extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name,number,type,date,body;

        View_Holder_Message(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            name = (TextView) itemView.findViewById(R.id.name);
            number = (TextView) itemView.findViewById(R.id.number);
            type = (TextView) itemView.findViewById(R.id.type);
            date = (TextView) itemView.findViewById(R.id.date);
            body = (TextView) itemView.findViewById(R.id.body);

        /*notification = (TextView) itemView.findViewById(R.id.notification);
        imageView = (ImageView) itemView.findViewById(R.id.circleView);*/

        }


    }
}

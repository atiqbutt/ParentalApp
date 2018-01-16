package com.softvilla.parentalapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Malik on 01/08/2017.
 */

public class View_Holder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView name;
    TextView notification;
    ImageView imageView;



    View_Holder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        name = (TextView) itemView.findViewById(R.id.title);
        /*notification = (TextView) itemView.findViewById(R.id.notification);
        imageView = (ImageView) itemView.findViewById(R.id.circleView);*/

    }


}
package com.softvilla.parentalapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Malik on 01/08/2017.
 */

public class View_Holder_CallLog extends RecyclerView.ViewHolder {

    CardView cv;
    TextView name,number,type,date,duration;




    View_Holder_CallLog(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        name = (TextView) itemView.findViewById(R.id.name);
        number = (TextView) itemView.findViewById(R.id.number);
        type = (TextView) itemView.findViewById(R.id.type);
        date = (TextView) itemView.findViewById(R.id.date);
        duration = (TextView) itemView.findViewById(R.id.duration);
        /*notification = (TextView) itemView.findViewById(R.id.notification);
        imageView = (ImageView) itemView.findViewById(R.id.circleView);*/

    }


}
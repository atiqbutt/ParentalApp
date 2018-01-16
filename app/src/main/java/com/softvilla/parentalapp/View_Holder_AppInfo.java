package com.softvilla.parentalapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Malik on 01/08/2017.
 */

public class View_Holder_AppInfo extends RecyclerView.ViewHolder {

    CardView cv;
    TextView name;
    TextView packegName;
    ImageView imageView;
    android.support.v7.widget.SwitchCompat switchCompat;



    View_Holder_AppInfo(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        name = (TextView) itemView.findViewById(R.id.title);
        packegName = (TextView) itemView.findViewById(R.id.pkgName);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        switchCompat = (SwitchCompat)itemView.findViewById(R.id.switchButton);

    }


}
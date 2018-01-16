package com.softvilla.parentalapp;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Malik on 21/12/2017.
 */

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private int offset;

    public ItemOffsetDecoration(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        // Add padding only to the zeroth item
        if (parent.getChildAdapterPosition(view) == 0) {

            outRect.right = offset;
            outRect.left = offset;
            outRect.top = offset;
            outRect.bottom = offset;
        }
    }
}



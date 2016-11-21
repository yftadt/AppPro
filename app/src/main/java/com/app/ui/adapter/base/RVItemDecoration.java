package com.app.ui.adapter.base;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by guom on 2016/11/20.
 */

public class RVItemDecoration extends RecyclerView.ItemDecoration {


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int index = parent.getChildLayoutPosition(view);
        outRect.bottom = 8;
        if (index % 2 == 0) {
            outRect.left = 10;
            outRect.right = 5;

        }
        if ((index + 1) % 2 == 0) {
            outRect.left = 5;
            outRect.right = 10;
        }
      //  int height = outRect.height();
      //  int width = outRect.width();
      //  outRect.set(outRect.left, 0, outRect.right, 120);
    }
// GridLayoutManager layoutManager = new GridLayoutManager(baseActivity, 2);
    //   recyclerview.setLayoutManager(layoutManager);
    //   recyclerview.setHasFixedSize(true);
    //  recyclerview.setAdapter(adapter);
    //  recyclerview.addItemDecoration(new RVItemDecoration());
}

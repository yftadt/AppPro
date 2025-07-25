package test.app.ui.adapter.video;

import android.graphics.Rect;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 视频列表（AddArtVideoActivity）
 * Created by guom on 2016/11/20.
 */

public class RVItemVideoDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int index = parent.getChildLayoutPosition(view);
        if (index % 3 != 0) {
            outRect.left = 5;
        }

    }

}

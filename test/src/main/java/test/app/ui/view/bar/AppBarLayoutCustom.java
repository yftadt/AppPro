package test.app.ui.view.bar;

import android.content.Context;
 import android.util.AttributeSet;

import com.google.android.material.appbar.AppBarLayout;

/**
 * Created by Administrator on 2017/8/23.
 */

public class AppBarLayoutCustom extends AppBarLayout {
    public AppBarLayoutCustom(Context context) {
        super(context);
    }

    public AppBarLayoutCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private AppBarChangeListener changeListener;

    public void setAppBarChangeListener(AppBarChangeListener changeListener) {
        this.changeListener = changeListener;
        this.addOnOffsetChangedListener(new AppBarOffsetChangeListener());
    }

    public interface  AppBarChangeListener {
        //state 1:展开完成状态 2: 关闭完成状态 3:展开中  verticalOffset:偏移量
        void onStateChanged(AppBarLayout appBarLayout, int state, int verticalOffset, int maxVerticalOffset);
    }

    public class AppBarOffsetChangeListener implements AppBarLayout.OnOffsetChangedListener {


        private int maxVerticalOffset = -1;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (maxVerticalOffset == -1) {
                maxVerticalOffset = appBarLayout.getTotalScrollRange();
            }
            if (verticalOffset == 0) {
                //展开完成状态
                changeListener.onStateChanged(appBarLayout, 1, verticalOffset, maxVerticalOffset);
                return;
            }
            if (Math.abs(verticalOffset) >= maxVerticalOffset) {
                //关闭完成状态
                changeListener.onStateChanged(appBarLayout, 2, verticalOffset, maxVerticalOffset);
                return;
            }
            //展开中
            changeListener.onStateChanged(appBarLayout, 3, verticalOffset, maxVerticalOffset);

        }
    }
}

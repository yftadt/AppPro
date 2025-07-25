package test.app.ui.activity.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.library.baseui.utile.HandlerUtil;
import com.library.baseui.view.page.ViewPagerNotSlide;

import java.util.ArrayList;

import test.app.ui.activity.R;
import test.app.ui.activity.action.BaseBarActivity1;
import test.app.ui.adapter.ViewPagerAdapter;
import test.app.ui.pages.BaseViewPage;
import test.app.ui.pages.test.TestCardPager;
import test.app.ui.view.bar.AppBarLayoutCustom;
import test.app.ui.view.tab.Tab;


//卡片
public class TestCardActivity extends BaseBarActivity1 {

    private ViewPagerAdapter adapter;
    private CollapsingToolbarLayout collapsingTool;
    private ViewPagerNotSlide viewPager;
    private AppBarLayoutCustom barLayout;
    private SwipeRefreshLayout sr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusTransparent();
        setContentView(R.layout.activity_test_card);
        collapsingTool = (CollapsingToolbarLayout) findViewById(R.id.collapsing_tool);
        viewPager = (ViewPagerNotSlide) findViewById(R.id.view_pager);
        barLayout = (AppBarLayoutCustom) findViewById(R.id.bar_layout);
        sr = (SwipeRefreshLayout) findViewById(R.id.sr);
        sr.setOnRefreshListener(new RefreshListener());
        barLayout.setAppBarChangeListener(new BarChangeListener());
        setViewPager();

        setNoSlide();
    }

    private boolean isSlide = true;

    private void setNoSlide() {
        //AppBarLayout设置关闭且不可滑动
        isSlide = false;
        //
        HandlerUtil.runInMainThread(new Runnable() {
            @Override
            public void run() {
                barLayout.setExpanded(false, false);
                collapsingTool.setVisibility(View.GONE);
            }
        }, 1 * 500);
        int i0 = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;
        int i1 = AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED;
        View appBarChildAt = barLayout.getChildAt(0);
        AppBarLayout.LayoutParams appBarParams = (AppBarLayout.LayoutParams) appBarChildAt.getLayoutParams();
        appBarParams.setScrollFlags(0);//不可滑动
        appBarChildAt.setLayoutParams(appBarParams);
        //延时打开
        HandlerUtil.runInMainThread(new Runnable() {
            @Override
            public void run() {
                setSlide();
                collapsingTool.setVisibility(View.VISIBLE);
            }
        }, 3 * 1000);
    }

    private void setSlide() {
        //AppBarLayout设置关闭且不可滑动
        isSlide = true;
        //
        int i0 = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;
        int i1 = AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED;
        View appBarChildAt = barLayout.getChildAt(0);
        AppBarLayout.LayoutParams appBarParams = (AppBarLayout.LayoutParams) appBarChildAt.getLayoutParams();
        appBarParams.setScrollFlags(i0 | i1);// 重置折叠效果
        appBarChildAt.setLayoutParams(appBarParams);
    }

    public void setViewPager() {
        String[] title = new String[]{"简介", "精选", "动态"};
        adapter = new ViewPagerAdapter(getViews());
        viewPager.setAdapter(adapter);
        Tab indicator = (Tab) findViewById(R.id.view_pager_indicator);
        indicator.setTabTxt(title);
        indicator.setupWithViewPager(viewPager);
        indicator.setTabTestCard();

    }

    private ArrayList<BaseViewPage> getViews() {
        ArrayList<BaseViewPage> listPager = new ArrayList<BaseViewPage>();
        listPager.add(new TestCardPager(this));
        listPager.add(new TestCardPager(this));
        listPager.add(new TestCardPager(this));
        return listPager;
    }


    class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            uiHandler.sendEmptyMessageDelayed(1, 1 * 1000);
        }
    }

    private boolean init;

    class BarChangeListener implements AppBarLayoutCustom.AppBarChangeListener {

        @Override
        public void onStateChanged(AppBarLayout appBarLayout, int state, int verticalOffset, int maxVerticalOffset) {
            if (!init) {
                init = true;
                // collapsingTool.setScrimVisibleHeightTrigger(250);
                collapsingTool.setScrimAnimationDuration(200);
                collapsingTool.setContentScrimResource(R.color.colorAccent);
            }
            float value = (float) Math.abs(verticalOffset) / (float) maxVerticalOffset;
            sr.setEnabled(state == 1);
            //collapsingTool.setAlpha(1 - value);
            //barView.setAlpha(value);
            // barTitleTv.setAlpha(value);
        }
    }

    //=======================================================
    UiHandler uiHandler = new UiHandler();

    class UiHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sr.setRefreshing(false);
        }
    }
//高级自定义TabLayout , 修改 tab之间的距离，去掉默认 选中的框
      /*<com.google.android.material.tabs.TabLayout
    android:id="@+id/tab_video_type"
    android:layout_width="match_parent"
    android:layout_height="@dimen/dp_37"
    android:background="#141414"
    app:tabIndicatorColor="#0000"
    app:tabIndicatorHeight="0dp"
    app:tabMaxWidth="@dimen/dp_0"
    app:tabMinWidth="@dimen/dp_0"
    app:tabMode="auto"
    app:tabPadding="0dp"
    app:tabPaddingEnd="0dp"
    app:tabPaddingStart="0dp" />*/
}

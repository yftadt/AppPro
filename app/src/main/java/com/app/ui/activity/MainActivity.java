package com.app.ui.activity;


import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.app.ui.activity.action.NormalActionBar;
import com.app.ui.adapter.ViewPagerIconAdapter;
import com.app.ui.pager.BasePager;
import com.app.ui.pager.main.TestPager;
import com.app.utiles.other.ToastUtile;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

public class MainActivity extends NormalActionBar {

    private String[] title = new String[]{"首页", "资讯", "我的"};
    private ViewPagerIconAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
        setBarColor();
        setBarTvText(1, "主页");
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.view_pager_indicator);
        adapter = new ViewPagerIconAdapter(getView(), getIcon());
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        indicator.setOnPageChangeListener(new OnPagerCahnge());
    }


    private static final long PRESS_BACK_TIME = 2 * 1000;
    /**
     * 上次按返回的时间
     */
    private long lastPressBackTime = 0;

    @Override
    public void onBackPressed() {
        long currentPressBackTime = System.currentTimeMillis();
        if (currentPressBackTime - lastPressBackTime < PRESS_BACK_TIME) {
            finish();
        } else {
            ToastUtile.showToast("再按一次退出");
        }
        lastPressBackTime = currentPressBackTime;
        //moveTaskToBack(true);
    }

    /**
     * 获取tab页
     */
    private ArrayList<BasePager> getView() {
        ArrayList<BasePager> listPager = new ArrayList<BasePager>();
        listPager.add(new TestPager(this));
        listPager.add(new TestPager(this));
        listPager.add(new TestPager(this));
        return listPager;
    }

    /**
     * 获取tab图标
     */
    private ArrayList<Integer> getIcon() {
        ArrayList<Integer> listIcon = new ArrayList<Integer>();
        listIcon.add(R.drawable.main_tab_icon_one);
        listIcon.add(R.drawable.main_tab_icon_two);
        listIcon.add(R.drawable.main_tab_icon_three);
        return listIcon;
    }
    class OnPagerCahnge implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setBarTvText(1, title[position]);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}

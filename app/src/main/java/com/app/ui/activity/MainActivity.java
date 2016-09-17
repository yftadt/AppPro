package com.app.ui.activity;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.widget.ImageView;

import com.app.ui.activity.action.NormalActionBar;
import com.app.ui.adapter.ViewPagesrAdapter;
import com.app.ui.pager.BaseViewPager;
import com.app.ui.pager.main.TestPager;
import com.app.ui.pager.main.TestTabPager;
import com.app.utiles.other.ToastUtile;

import java.util.ArrayList;

public class MainActivity extends NormalActionBar {

    private String[] title = new String[]{"首页", "资讯", "我的"};
    private ViewPagesrAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBarColor();
        setBarTvText(1, "主页");
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout indicator = (TabLayout) findViewById(R.id.view_pager_indicator);
        adapter = new ViewPagesrAdapter(getView());
        viewPager.setAdapter(adapter);
        indicator.setupWithViewPager(viewPager);
        setTadIcon(indicator);
        viewPager.addOnPageChangeListener(new OnPagerChange());
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

    private void setTadIcon(TabLayout indicator) {
        ArrayList<Integer> icons = getIcon();
        int minimumHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        int minimumWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        for (int i = 0; i < icons.size(); i++) {
            indicator.newTab();
            TabLayout.Tab tab = indicator.getTabAt(i);
            ImageView iv = new ImageView(this);
            iv.setMinimumHeight(minimumHeight);
            iv.setMinimumWidth(minimumWidth);
            iv.setImageResource(icons.get(i));
            tab.setCustomView(iv);
        }

    }

    /**
     * 获取tab页
     */
    private ArrayList<BaseViewPager> getView() {
        ArrayList<BaseViewPager> listPager = new ArrayList<BaseViewPager>();
        listPager.add(new TestPager(this));
        listPager.add(new TestTabPager(this));
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

    class OnPagerChange implements ViewPager.OnPageChangeListener {

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

package test.app.ui.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.viewpager.widget.ViewPager;




import com.google.android.material.tabs.TabLayout;
import com.library.baseui.utile.toast.ToastUtile;

import java.util.ArrayList;

import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.adapter.ViewPagerAdapter;
import test.app.ui.pages.BaseViewPage;
import test.app.ui.pages.main.TestListPager;
import test.app.ui.pages.main.TestPager;
import test.app.ui.pages.main.TestTabPager;
import test.app.utiles.WMViewMaanger;

public class MainActivity extends NormalActionBar {

    private String[] title = new String[]{"首页", "资讯", "我的"};
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main, true);
        setBarColor();
        setBarTvText(1, "主页");
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout indicator = (TabLayout) findViewById(R.id.view_pager_indicator);
        adapter = new ViewPagerAdapter(getView());
        viewPager.setAdapter(adapter);
        indicator.setupWithViewPager(viewPager);
        setTadIcon(indicator);
        viewPager.addOnPageChangeListener(new OnPagerChange());
        new Han().sendEmptyMessageDelayed(1, 1 * 1000);

    }

    private void test3() {
        WindowManager windowManager = WMViewMaanger.getInstance().getWindowManager(this);
        TextView addView = new TextView(this);
        addView.setText("hahhaahah\nahahahahha\nahhdjlkdfjglkdlfjgl");
        addView.setBackgroundColor(Color.BLACK);
        addView.setTextColor(Color.WHITE);
        windowManager.addView(addView, WMViewMaanger.getInstance().getWindowManagerParams());
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
    private ArrayList<BaseViewPage> getView() {
        ArrayList<BaseViewPage> listPager = new ArrayList<BaseViewPage>();
        listPager.add(new TestListPager(this));
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

    class Han extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadingSucceed();
            //test3();
        }
    }

}

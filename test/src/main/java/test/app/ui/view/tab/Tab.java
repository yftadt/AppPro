package test.app.ui.view.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;
import com.library.baseui.utile.other.NumberUtile;

import java.util.ArrayList;

import test.app.ui.activity.R;

/**
 * Created by Administrator on 2017/6/15.
 */

public class Tab extends TabLayout {
    public Tab(Context context) {
        super(context);
    }

    public Tab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Tab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private ArrayList<Integer> icons;
    private String[] tabTxts;

    public void setIcon(ArrayList<Integer> icons) {
        this.icons = icons;
    }

    public void setTabTxt(String[] tabTxts) {
        this.tabTxts = tabTxts;
    }

    public void setTabMain() {
        if (tabTxts == null || icons == null) {
            return;
        }
        if (tabTxts.length != icons.size()) {
            return;
        }
        Context context = getContext();
        removeAllTabs();
        for (int i = 0; i < icons.size(); i++) {
            TabLayout.Tab tab = this.newTab();
            View view = LayoutInflater.from(context).inflate(R.layout.tab_main, null);
            ImageView iv = (ImageView) view.findViewById(R.id.tad_iv);
            TextView tv = (TextView) view.findViewById(R.id.tad_tv);
            tv.setText(tabTxts[i]);
            iv.setImageResource(icons.get(i));
            tab.setCustomView(view);
            addTab(tab);
        }
    }
    //医生名片
    public void setTabTestCard() {
        if (tabTxts == null || tabTxts.length < 1) {
            return;
        }
        removeAllTabs();
        for (int i = 0; i < tabTxts.length; i++) {
            TabLayout.Tab tab = this.newTab();
            View view = LayoutInflater.from(getContext()).inflate(R.layout.tab_test_card, null);
            TextView tv = (TextView) view.findViewById(R.id.tad_tv);
            tv.setText(tabTxts[i]);
            tab.setCustomView(view);
            addTab(tab);
        }
    }
    //红点显示+1；
    public void countAdd(int index) {
        countAdd(index, 1);
    }

    //红点显示+固定数量；
    public void countAdd(int index, String number) {
        int numberI = NumberUtile.getStringToInt(number);
        countAdd(index, numberI);
    }

    //红点显示+固定数量；
    public void countAdd(int index, int number) {
        setCount(index, 1, number);
    }

    //红点显示-1；
    public void countDelecte(int index) {
        countDelecteNumber(index, 1);
    }

    //红点显示-固定数量；
    public void countDelecteNumber(int index, String number) {
        int numberI = NumberUtile.getStringToInt(number);
        countDelecteNumber(index, numberI);
    }

    //红点显示-固定数量；
    public void countDelecteNumber(int index, int number) {
        setCount(index, 2, number);
    }

    //红点显示number；
    public void countRest(int index, String number) {
        int numberI = NumberUtile.getStringToInt(number);
        countRest(index, numberI);
    }

    //红点显示number；
    public void countRest(int index, int number) {
        setCount(index, 3, number);
    }

    private void setCount(int index, int type, int number) {
        int context = getTabCount();
        if (index >= context) {
            return;
        }
        Tab tb = getTabAt(index);
        View customView = tb.getCustomView();
        TextView tv = (TextView) customView.findViewById(R.id.tab_count_tv);
        String numberStr = tv.getText().toString();
        int n = NumberUtile.getStringToInt(numberStr, 0);
        //1:红点+ 2：红点-  3：红点设置
        switch (type) {
            case 1:
                // 红点+1；
                n += number;
                break;
            case 2:
                //红点-1
                n -= number;
                break;
            case 3:
                //设置红点数量
                n = number;
                break;

        }
        if (n <= 0) {
            n = 0;
        }
        int tvShow = n == 0 ? View.GONE : View.VISIBLE;
        tv.setText(String.valueOf(n));
        tv.setVisibility(tvShow);
    }
}

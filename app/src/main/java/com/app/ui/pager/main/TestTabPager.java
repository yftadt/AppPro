package com.app.ui.pager.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.app.ui.activity.R;
import com.app.ui.activity.base.BaseActivity;
import com.app.ui.adapter.ViewPagerStringAdapter;
import com.app.ui.pager.BaseViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/7.
 */
public class TestTabPager extends BaseViewPager {
    private ViewPager viewPager;

    public TestTabPager(BaseActivity baseActivity) {
        super(baseActivity);
    }

    @Override
    protected View onViewCreated() {
        View view = LayoutInflater.from(baseActivity).inflate(R.layout.test_three_tab, null);
        TabLayout indicator = (TabLayout) view.findViewById(R.id.view_pager_indicator);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        ViewPagerStringAdapter adapter = new ViewPagerStringAdapter(getViews(), getTitles());
        viewPager.setAdapter(adapter);
        indicator.setupWithViewPager(viewPager);
        return view;
    }
    //  获取tab页

    private ArrayList<BaseViewPager> getViews() {
        ArrayList<BaseViewPager> listPager = new ArrayList<BaseViewPager>();
        listPager.add(new TestPager(baseActivity));
        listPager.add(new TestPager(baseActivity));
        listPager.add(new TestPager(baseActivity));
        return listPager;
    }

    private ArrayList<String> getTitles() {
        ArrayList<String> titls = new ArrayList<String>();
        titls.add("未解答");
        titls.add("已解答");
        titls.add("已逾期");
        return titls;
    }
}

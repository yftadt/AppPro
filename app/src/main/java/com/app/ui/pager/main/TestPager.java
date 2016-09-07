package com.app.ui.pager.main;

import android.view.View;
import android.widget.TextView;

import com.app.ui.activity.base.BaseActivity;
import com.app.ui.pager.BasePager;

/**
 * Created by Administrator on 2016/9/7.
 */
public class TestPager extends BasePager {
    public TestPager(BaseActivity baseActivity) {
        super(baseActivity);
    }

    @Override
    protected View onViewCreated() {
        TextView tv = new TextView(baseActivity);
        tv.setText("123456");
        return tv;
    }
}

package com.app.ui.activity.action;

import android.view.View;
import android.widget.TextView;

import com.app.ui.activity.R;


/**
 * Created by Administrator on 2016/3/30.
 */
public class NormalActionBar extends BaseBarActivity implements View.OnClickListener {
    protected void setBarColor() {
        setViewColor(0xffffffff, 0xffffffff, 0xffffffff, 0xff7db2fd);
    }

    protected void setMainBarColor() {
        setViewColor(0xffffffff, 0xffffffff, 0xffffffff, 0xff7db2fd);

    }

    public void setContentView(int layoutResID, boolean initLoading) {
        super.setContentView(layoutResID, R.layout.action_bar_normal, initLoading);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID, R.layout.action_bar_normal, false);
    }

    public void setContentView(View view, boolean initLoading) {
        super.setContentView(view, R.layout.action_bar_normal, initLoading);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view, R.layout.action_bar_normal, false);
    }

    private TextView leftTv;
    private TextView rightTv;
    private TextView titleTv;

    @Override
    protected void initView() {
        leftTv = (TextView) findViewById(R.id.bar_left_tv);
        titleTv = (TextView) findViewById(R.id.bar_title_tv);
        rightTv = (TextView) findViewById(R.id.bar_right_tv);
        setViewColor();
        setTvOnClick();
    }

    protected void setTvOnClick() {
        leftTv.setOnClickListener(this);
        rightTv.setOnClickListener(this);
        titleTv.setOnClickListener(this);
    }

    private int[] clors;

    private void setViewColor() {
        if (clors == null) {
            return;
        }
        setViewColor(clors[0], clors[1], clors[2], clors[3]);
    }

    private void setViewColor(int barLeftTvColor, int titleTvColor, int rightTvColor, int barColor) {
        if (barView != null && leftTv != null && titleTv != null && rightTv != null) {
            barView.setBackgroundColor(barColor);
            leftTv.setTextColor(barLeftTvColor);
            titleTv.setTextColor(titleTvColor);
            rightTv.setTextColor(rightTvColor);
        } else {
            clors = new int[4];
            clors[0] = barLeftTvColor;
            clors[1] = titleTvColor;
            clors[2] = rightTvColor;
            clors[3] = barColor;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bar_left_tv:
                //返回
                onBackPressed();
                break;
            case R.id.bar_right_tv:
                option();
                break;
            default:
                onClick(id);
                break;
        }

    }

    protected void onClick(int id) {

    }

    //点击actionBar右侧
    protected void option() {

    }

    //设置标题
    protected void setBarTvText(int type, String text) {
        TextView tv = getTv(type);
        tv.setText(text);
    }

    protected void setBarTvText(int type, int iconId, String text, int iconLocation) {
        TextView tv = getTv(type);
        setText(tv, iconId, text, iconLocation);
    }

    private TextView getTv(int type) {
        TextView tv = null;
        switch (type) {
            case 0:
                tv = leftTv;
                break;
            case 1:
                tv = titleTv;
                break;
            case 2:
                tv = rightTv;
                break;
        }
        return tv;
    }
}

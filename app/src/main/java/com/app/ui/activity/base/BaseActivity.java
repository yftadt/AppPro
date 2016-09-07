package com.app.ui.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.net.common.RequestBack;
import com.app.ui.activity.R;

/**
 * Created by Administrator on 2016/9/7.
 */
public class BaseActivity extends AppCompatActivity implements RequestBack {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void OnBack(int what, Object obj, String msg, String other) {

    }
}

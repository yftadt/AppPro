package com.app.ui.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.net.common.RequestBack;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/7.
 */
public class BaseActivity extends AppCompatActivity implements RequestBack {
    private BaseApplication baseApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseApplication = (BaseApplication) getApplication();
    }

    private Intent it;
    private Bundle bundle;

    //arg0,   arg1
    protected String getStringExtra(String key) {
        if (it == null) {
            it = getIntent();
        }
        return it.getStringExtra(key);
    }

    protected Serializable getObjectExtra(String key) {
        if (it == null) {
            it = getIntent();
        }
        if (bundle == null) {
            bundle = it.getExtras();
        }
        if (bundle == null) {
            return null;
        }
        Serializable bean = bundle.getSerializable(key);
        return bean;
    }

    @Override
    public void OnBack(int what, Object obj, String msg, String other) {

    }
}

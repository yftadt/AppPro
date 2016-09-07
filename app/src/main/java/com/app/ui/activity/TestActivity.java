package com.app.ui.activity;

import android.os.Bundle;

import com.app.net.manager.account.LoginManager;
import com.app.ui.activity.action.NormalActionBar;
import com.app.ui.dialog.CustomWaitingDialog;
import com.app.utiles.other.ActivityUtile;
import com.app.utiles.other.DLog;

public class TestActivity extends NormalActionBar {

    private CustomWaitingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setBarColor();
        setBarTvText(1, "测试网络交互");
        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.login_main_btn).setOnClickListener(this);
        dialog = new CustomWaitingDialog(this);
    }

    private LoginManager manager;

    @Override
    protected void onClick(int id) {
        switch (id) {
            case R.id.login_btn:
                if (manager == null) {
                    manager = new LoginManager(this);
                }
                manager.setData("18868714254", "123456");
                manager.request();
                dialog.show();
                break;
            case R.id.login_main_btn:
                ActivityUtile.startActivityCommon(MainActivity.class);
                finish();
                break;
        }
    }

    @Override
    public void OnBack(int what, Object obj, String msg, String other) {
        super.OnBack(what, obj, msg, other);
        dialog.dismiss();
        DLog.e("请求" + what, "返回-" + msg);
    }
}

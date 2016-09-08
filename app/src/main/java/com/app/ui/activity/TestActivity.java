package com.app.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.app.net.manager.account.LoginManager;
import com.app.ui.activity.action.NormalActionBar;
import com.app.ui.dialog.CustomWaitingDialog;
import com.app.utiles.other.ActivityUtile;
import com.app.utiles.other.DLog;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends NormalActionBar {

    private CustomWaitingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "测试网络交互");
        ButterKnife.bind(this);
        dialog = new CustomWaitingDialog(this);
    }

    private LoginManager manager;


    @Override
    public void OnBack(int what, Object obj, String msg, String other) {
        super.OnBack(what, obj, msg, other);
        dialog.dismiss();
        DLog.e("请求" + what, "返回-" + msg);
    }

    @OnClick({R.id.login_btn, R.id.login_main_btn})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
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
}

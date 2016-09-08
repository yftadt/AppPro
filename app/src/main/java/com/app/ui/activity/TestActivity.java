package com.app.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.net.manager.account.LoginManager;
import com.app.ui.activity.action.NormalActionBar;
import com.app.ui.dialog.CustomWaitingDialog;
import com.app.utiles.image.ImageLoadingUtile;
import com.app.utiles.other.ActivityUtile;
import com.app.utiles.other.DLog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends NormalActionBar {

    @Bind(R.id.test_iv)
    ImageView testIv;
    private CustomWaitingDialog dialog;
    private String url = "http://d.hiphotos.baidu.com/image/h%3D200/" +
            "sign=5695f72692ef76c6cfd2fc2bad16fdf6/f9dcd100baa1cd11c1" +
            "c35727bb12c8fcc3ce2dbb.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "测试网络交互");
        ButterKnife.bind(this);
        dialog = new CustomWaitingDialog(this);
        ImageLoadingUtile.loadingCircle(this,url,R.mipmap.ic_launcher, testIv);
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

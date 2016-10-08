package com.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.net.manager.account.LoginManager;
import com.app.ui.activity.action.NormalActionBar;
import com.app.ui.dialog.DialogCustomWaiting;
import com.app.utiles.image.ImageLoadingUtile;
import com.app.utiles.other.ActivityUtile;
import com.app.utiles.other.DLog;
import com.app.utiles.photo.PhotoManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends NormalActionBar {

    @Bind(R.id.test_iv)
    ImageView testIv;
    @Bind(R.id.chat_left_iv)
    ImageView chatLeftIv;
    @Bind(R.id.chat_right_iv)
    ImageView chatRightIv;

    private DialogCustomWaiting dialog;

    public static String head = "http://img1.imgtn.bdimg.com/it/u=2633543122,1301516709&fm=11&gp=0.jpg";
    public static String docHead = "http://img3.imgtn.bdimg.com/it/u=2669114166,1444610691&fm=11&gp=0.jpg";
    public static String patHead = "http://d.hiphotos.baidu.com/image/h%3D200/" +
            "sign=5695f72692ef76c6cfd2fc2bad16fdf6/f9dcd100baa1cd11c1" +
            "c35727bb12c8fcc3ce2dbb.jpg";
    private PhotoManager photoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "测试网络交互");
        ButterKnife.bind(this);
        dialog = new DialogCustomWaiting(this);
        photoManager = new PhotoManager(this);
        ImageLoadingUtile.clear(this,0);
        ImageLoadingUtile.clear(this,1);
        ImageLoadingUtile.loadingCircle(this, head, R.mipmap.ic_launcher, testIv);
        ImageLoadingUtile.loadImageChat(this, docHead, R.mipmap.default_image,
                chatLeftIv, 0);
        ImageLoadingUtile.loadImageChat(this, docHead, R.mipmap.default_image,
                chatRightIv, 1);
    }

    private LoginManager manager;


    @Override
    public void OnBack(int what, Object obj, String msg, String other) {
        super.OnBack(what, obj, msg, other);
        dialog.dismiss();
        DLog.e("请求" + what, "返回-" + msg);
    }

    @OnClick({R.id.login_btn, R.id.login_main_btn,
            R.id.image_select_one_btn, R.id.image_select_more_btn,
            R.id.image_select_cop_btn, R.id.image_select_cops_btn})
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
            case R.id.image_select_one_btn:
                //选择一张
                photoManager.selectOne();
                break;
            case R.id.image_select_more_btn:
                //选择多张
                photoManager.selecMmore();
                break;
            case R.id.image_select_cop_btn:
                //按1:1裁剪
                photoManager.corpSquare();
                break;
            case R.id.image_select_cops_btn:
                //自由裁剪
                photoManager.corpFreedom();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoManager.onActivityResult(requestCode, resultCode, data);
    }


}

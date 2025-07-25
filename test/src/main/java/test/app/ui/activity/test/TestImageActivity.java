package test.app.ui.activity.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.images.config.entity.ImageEntity;
import com.library.baseui.utile.app.ActivityUtile;
import com.library.baseui.utile.img.ImageLoadingUtile;

import java.util.ArrayList;
import java.util.List;

import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;
import test.app.utiles.photo.ImageSelectManager;


//测试登录
public class TestImageActivity extends NormalActionBar {


    ImageView[] imagesIv = new ImageView[2];


    private ImageView image1Iv;
    private ImageView image2Iv;
    private ImageSelectManager photoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image);
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "图");
        photoManager = new ImageSelectManager(this);
        //


        findViewById(R.id.image_select_one_btn).setOnClickListener(this);
        findViewById(R.id.image_select_more_btn).setOnClickListener(this);
        findViewById(R.id.image_select_cop_btn).setOnClickListener(this);
        findViewById(R.id.image_select_cops_btn).setOnClickListener(this);
        //
        image1Iv = findViewById(R.id.image_1_iv);
        image2Iv = findViewById(R.id.image_2_iv);
        imagesIv[0] = image1Iv;
        imagesIv[1] = image2Iv;

        //
        initView();
    }

    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        if (id == R.id.image_select_one_btn) {
            //选择一张
            photoManager.getMoreConfig(1, null);
            return;
        }
        if (id == R.id.image_select_more_btn) {
            //选择多张
            photoManager.getMoreConfig();
            return;
        }
        if (id == R.id.image_select_cop_btn) {
            //拍照裁剪
            photoManager.getSinglePhotoCropConfig();
            return;
        }
        if (id == R.id.image_select_cops_btn) {
            //选图裁剪
            photoManager.getSingleCropConfig();
            return;
        }
        if (id == R.id.yp_btn) {
            //语音
            ActivityUtile.startActivityCommon(TestMusicActivity.class);

            return;
        }
        if (id == R.id.chat_btn) {
            //会话
            ActivityUtile.startActivityCommon(TestChatActivity.class);
            return;
        }
        if (id == R.id.card_btn) {
            ActivityUtile.startActivityCommon(TestCardActivity.class);
            return;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<ImageEntity> image = photoManager.onActivityResult(requestCode, resultCode, data);
        if (image == null) {
            image = new ArrayList<>();
        }
        int size = image.size();
        for (int i = 0; i < imagesIv.length; i++) {
            if (i >= size) {
                imagesIv[i].setVisibility(View.GONE);
                continue;
            }
            String imagePathSource = image.get(i).imagePathSource;
            ImageLoadingUtile.loading(this, imagePathSource, R.mipmap.ic_launcher, imagesIv[i]);
            imagesIv[i].setVisibility(View.VISIBLE);
        }
    }


}

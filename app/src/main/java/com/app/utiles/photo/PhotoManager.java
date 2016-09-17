package com.app.utiles.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.PermissionChecker;
import android.widget.ImageView;

import com.app.ui.activity.R;
import com.app.ui.manager.PermissionManager;
import com.app.utiles.other.DLog;
import com.bumptech.glide.Glide;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class PhotoManager {
    private final ImageConfig.Builder imageConfig;
    private Activity activity;
    private Resources res;

    public PhotoManager(Activity activity) {
        this.activity = activity;
        res = activity.getResources();
        imageConfig
                = new ImageConfig.Builder(activity, new GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                .steepToolBarColor(res.getColor(R.color.blue))
                // 标题的背景颜色 （默认黑色）
                .titleBgColor(res.getColor(R.color.blue))
                // 提交按钮字体的颜色  （默认白色）
                .titleSubmitTextColor(res.getColor(R.color.white))
                // 标题颜色 （默认白色）
                .titleTextColor(res.getColor(R.color.white));
    }

    public void selecMmore() {
        boolean isTrue = checkSelfPermission();
        if (!isTrue) {
            return;
        }
        imageConfig
                //用来设置已经选择的图片，这里是用来清空已经选择的图片
                .pathList(new ArrayList<String>())
                // 开启多选   （默认为多选）
                .mutiSelect()
                // 多选时的最大数量   （默认 9 张）
                .mutiSelectMaxSize(9)
                // 开启拍照功能 （默认关闭）
                .showCamera()
                // 已选择的图片路径
                // .pathList(path)
                // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
                .filePath("/ImageSelector/Pictures");
        ImageSelector.open(imageConfig.build());   // 开启图片选择器
    }

    public void selectOne() {
        boolean isTrue = checkSelfPermission();
        if (!isTrue) {
            return;
        }
        imageConfig
                //用来设置已经选择的图片，这里是用来清空已经选择的图片
                .pathList(new ArrayList<String>())
                // 开启单选   （默认为多选）
                .singleSelect()
                // 开启拍照功能 （默认关闭）
                .showCamera()
                // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
                .filePath("/ImageSelector/Pictures");
        ImageSelector.open(imageConfig.build());
    }

    public void corpSquare() {
        boolean isTrue = checkSelfPermission();
        if (!isTrue) {
            return;
        }
        imageConfig
                //用来设置已经选择的图片，这里是用来清空已经选择的图片
                .pathList(new ArrayList<String>())
                // (截图默认配置：关闭    比例 1：1    输出分辨率  500*500)
                .crop()
                // 开启单选   （默认为多选）
                .singleSelect()
                // 开启拍照功能 （默认关闭）
                .showCamera()
                // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
                .filePath("/ImageSelector/Pictures");
        ImageSelector.open(imageConfig.build());
    }

    //单选自定义截图
    public void corpFreedom() {
        boolean isTrue = checkSelfPermission();
        if (!isTrue) {
            return;
        }
        imageConfig
                //用来设置已经选择的图片，这里是用来清空已经选择的图片
                .pathList(new ArrayList<String>())
                // (截图默认配置：关闭    比例 1：1    输出分辨率  500*500)
                .crop(1, 2, 500, 1000)
                // 开启单选   （默认为多选）
                .singleSelect()
                // 开启拍照功能 （默认关闭）
                .showCamera()
                // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
                .filePath("/ImageSelector/Pictures");
        ImageSelector.open(imageConfig.build());
    }

    public class GlideLoader implements com.yancy.imageselector.ImageLoader {

        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                    .centerCrop()
                    .into(imageView);
        }

    }

    private PermissionManager permission;
    //图片类固定权限的requestCode
    public static final int PERMISSION_REQEST_CODE = 400;

    //检查授权
    public boolean checkSelfPermission() {
        if (permission == null) {
            permission = new PermissionManager();
        }
        int state = permission.checkSelfPermission(activity, PermissionManager.permission_camera);
        if (state != PermissionChecker.PERMISSION_GRANTED) {
            //去请求授权
            permission.requestPermissions(activity, state, PermissionManager.permission_camera,
                    PERMISSION_REQEST_CODE);
            return false;
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            for (String path : pathList) {
                DLog.e("ImagePathList", path);
            }
        }
    }
}

package com.app.utiles.image;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;

import com.app.ui.manager.PermissionManager;
import com.app.utiles.other.ToastUtile;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageCropActivity;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/16.
 */
public class ImageManager {
    private Activity activity;
    private ImagePicker imagePicker;

    public ImageManager(Activity activity) {
        this.activity = activity;
        initPicker();
    }

    /**
     * 初始化图片选择器
     */
    public void initPicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.clear();
    }

    private PermissionManager permission;

    /**
     * 启动拍照
     */
    public void startCamera() {
        if (permission == null) {
            permission = new PermissionManager();
        }
        int state = permission.checkSelfPermission(activity, PermissionManager.permission_camera);
        if (state != PermissionChecker.PERMISSION_GRANTED) {
            permission.requestPermissions(activity, state, PermissionManager.permission_camera,
                    PERMISSION_REQEST_CODE);
            return;
        }
        imagePicker.takePicture(activity, IMAGE_REQEST_CODE);
    }

    //图片类固定的requestCode  拍照
    public static final int IMAGE_REQEST_CODE = ImagePicker.RESULT_CODE_ITEMS;
    //图片类固定权限的requestCode
    public static final int PERMISSION_REQEST_CODE = ImageGridActivity.REQUEST_PERMISSION_CAMERA;

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != PERMISSION_REQEST_CODE) {
            return;
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // imagePicker.takePicture(activity, IMAGE_REQEST_CODE);
        } else {
            ToastUtile.showToast("权限被禁止，无法打开相机");
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         /*if (requestCode != IMAGE_REQEST_CODE) {
            return;
        }*/
        if (data != null) {
            if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                /** ImageGridActivity或ImageCropActivity 返回 */
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(
                        ImagePicker.EXTRA_RESULT_ITEMS);
                ToastUtile.showToast(images.get(0).path);
                image(images);
            } else {
                /** 从拍照界面返回 */
                //点击 X , 没有选择照片
                if (data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) == null) {
                    //什么都不做
                } else {
                    //说明是从裁剪页面过来的数据，直接返回就可以
                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(
                            ImagePicker.EXTRA_RESULT_ITEMS);
                    ToastUtile.showToast(images.get(0).path);
                    image(images);
                }
            }
        } else {
            //拍照存储的Uri，所以返回的data一定为null
            if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_TAKE) {
                //发送广播通知图片增加了
                ImagePicker.galleryAddPic(activity, imagePicker.getTakeImageFile());
                ImageItem imageItem = new ImageItem();
                imageItem.path = imagePicker.getTakeImageFile().getAbsolutePath();
                imagePicker.clearSelectedImages();
                imagePicker.addSelectedImageItem(0, imageItem, true);
                if (imagePicker.isCrop()) {
                    /** 裁剪 */
                    Intent intent = new Intent(activity, ImageCropActivity.class);
                    activity.startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
                } else {
                    ToastUtile.showToast(imagePicker.getSelectedImages().get(0).path);
                    image(imagePicker.getSelectedImages());
                }
            } else {
                ToastUtile.showToast("操作取消");
            }
        }
    }

    /**
     * 选择完图片
     **/
    public void image(ArrayList<ImageItem> list) {
        if (onImageReceive == null) {
            return;
        }
        onImageReceive.onImgage(list);
    }

    private OnImageReceive onImageReceive;

    public void setOnImageReceive(OnImageReceive onImageReceive) {
        this.onImageReceive = onImageReceive;
    }

    public interface OnImageReceive {
        public void onImgage(ArrayList<ImageItem> list);
    }
}

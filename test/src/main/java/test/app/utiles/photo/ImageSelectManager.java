package test.app.utiles.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;



import com.bumptech.glide.Glide;
import com.images.config.ConfigBuild;
import com.images.config.Configs;
import com.images.config.ImageLoader;
import com.images.config.entity.ImageEntity;
import com.library.baseui.utile.file.FileUtile;
import com.library.baseui.utile.img.ImageUtile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import test.app.ui.activity.R;

import test.app.utiles.other.DLog;
import test.app.utiles.other.PermissionManager;
import test.app.utiles.other.Permissions;

/**
 * Created by Administrator on 2016/10/17.
 */
public class ImageSelectManager {
    private Activity activity;
    private int barHeight;

    public ImageSelectManager(Activity activity) {
        this.activity = activity;
        barHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                42, activity.getResources().getDisplayMetrics());
    }

    //图片类固定权限的requestCode
    public static final int PERMISSION_REQEST_CODE = 400;

    //检查授权
    public boolean checkSelfPermission() {
        return PermissionManager.getInstance().isCheckPermission(activity,
                Permissions.permission_camera, PERMISSION_REQEST_CODE);
    }

    //更多选择
    public void getMoreConfig() {
        getMoreConfig(3, null);
    }

    //最多选择maximum张图片
    public void getMoreConfig(int maximum, ArrayList<String> paths) {
        boolean isNotPermission= checkSelfPermission();
        if (isNotPermission) {
            return;
        }
        if (paths == null) {
            paths = new ArrayList<>();
        }
        ConfigBuild.getNewBuild()
                .setBuildBar()
                .setActionBarColor(0xffffffff)
                .setStatusBarColor(0xff7db2fd)
                .setActionBarHeight(barHeight)
                .complete()
                .setBuildBarCommon()
                .setBack("返回")
                .setTitle("选择图片")
                .setOption("完成")
                .setOptionIconbg(R.drawable.green_4_bg)
                .complete()
                .setBuildBarPreview()
                .setOption("发送")
                .complete()
                .setLoading(new ImageShowType())
                .setDebug(true)
                .setBuildMore()
                .setImageSelectMaximum(maximum)
                .complete()
                .setImagePath(paths)
                .setShowCamera(true)
                .build(activity);
    }


    //单选只拍照
    public void getSinglePhotoConfig() {
        boolean isNotPermission= checkSelfPermission();
        if (isNotPermission) {
            return;
        }
        ConfigBuild.getNewBuild()
                .setLoading(new ImageShowType())
                .setDebug(true)
                .setOnlyPhotograph(true)
                .build(activity);

    }

    //拍照裁剪
    public void getSinglePhotoCropConfig() {
        boolean isNotPermission= checkSelfPermission();
        if (isNotPermission) {
            return;
        }
        crop(true);
    }

    //只预览
    public void previewImage(ArrayList<String> listImagePath, int index) {
        ConfigBuild.getNewBuild()
                .setBuildBar()
                .setActionBarColor(0xffffffff)
                .setStatusBarColor(0xff7db2fd)
                .setActionBarHeight(barHeight)
                .complete()
                .setBuildBarPreview()
                .setBack("返回")
                .setTitle("预览")
                .complete()
                .setImagePath(listImagePath)
                .setLoading(new ImageShowType())
                .setDebug(true)
                .buildPreviewOnly(activity, index);
    }

    //只预览可以删除
    public void previewImageDelect(ArrayList<String> listImagePath, int index) {
        ConfigBuild.getNewBuild()
                .setBuildBar()
                .setActionBarColor(0xffffffff)
                .setStatusBarColor(0xff7db2fd)
                .setActionBarHeight(barHeight)
                .complete()
                .setBuildBarPreview()
                .setBack("返回")
                .setTitle("预览")
                .setOption("删除")
                .complete()
                .setImagePath(listImagePath)
                .setLoading(new ImageShowType())
                .setDebug(true)
                .buildPreviewDelete(activity, index);
    }

    public void getSingleCropConfig() {
        boolean isNotPermission= checkSelfPermission();
        if (isNotPermission) {
            return;
        }
        crop(false);

    }

    private void crop(boolean isonlyPhotograph) {
        ConfigBuild.getNewBuild()
                .setBuildBar()
                .setActionBarColor(0xffffffff)
                .setStatusBarColor(0xff7db2fd)
                .setActionBarHeight(barHeight)
                .complete()
                .setBuildBarCommon()
                .setBack("返回")
                .setTitle("选择图片")
                .setOption("完成")
                .setOptionIconbg(R.drawable.green_4_bg)
                .complete()
                .setBuildBarCrop()
                .setTitle("裁剪")
                .setOption("发送")
                .complete()
                .setBuildCrop()
                .setAspect(600, 600)
                .setOutput(600, 600)
                .setNotSystemCrop(true)
                .complete()
                .setLoading(new ImageShowType())
                .setDebug(true)
                .setShowCamera(true)
                .setOnlyPhotograph(isonlyPhotograph)
                .build(activity);
    }

    class ImageShowType implements ImageLoader {

        @Override
        public void imageLoading(Context context, String path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .placeholder(R.mipmap.image_select_default)
                    //.centerCrop()
                    .into(imageView);
        }

        @Override
        public void interdictMsg(Context context, ImageEntity imageEntity) {

        }
    }

    public List<ImageEntity> onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != Configs.TASK_START) {
            return null;
        }
        if (resultCode == Configs.TASK_CANCEL) {
            return null;
        }
        //按了返回键
        if (data == null) {
            return null;
        }
        Bundle bundle = data.getExtras();
        ArrayList<ImageEntity> images = (ArrayList<ImageEntity>) bundle.get(Configs.TASK_COMPLETE_RESULT);
        if (images == null || images.size() == 0) {
            return images;
        }
        if (resultCode == Configs.TASK_CROP_COMPLETE) {
            return images;
        }
        for (ImageEntity image : images) {
            String path = image.imagePathSource;
            if (path.startsWith("http://")) {
                image.imagePath = path;
                continue;
            }
            Bitmap bit = ImageUtile.getSmallBitmap(image.imagePathSource);
            if (bit == null) {
                continue;
            }
            String bitPath = String.valueOf(new Date().getTime());
            bitPath = FileUtile.saveBitmap(bit, bitPath, false);
            image.imagePath = bitPath;
            DLog.e("ImagePathList", image.imagePath);
        }
        return images;
    }

}

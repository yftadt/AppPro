package test.app.ui.view.images;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
 import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.images.config.entity.ImageEntity;

import java.util.HashMap;
import java.util.List;

import test.app.ui.window.popup.BasePopupWindow;
import test.app.ui.window.popup.PopupPhotoOption;
import test.app.utiles.photo.ImageSelectManager;

/**
 * Created by Administrator on 2017/7/4.
 */

public class ImagesLayout extends ImagesBaseLayout {
    //选择获取图片方式
    private PopupPhotoOption photoPopupMenuView;
    //图片管理器
    private ImageSelectManager managerPhoto;
    private View activityRoot;

    public ImagesLayout(Context context) {
        super(context);
    }

    public ImagesLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImagesLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //设置一排3张图片的view
    public void setImage(Activity activiy, List<String> images) {
        setImage(activiy, images, true);
    }

    //设置一排3张图片的view    isImageClick:true 图片看可以点击
    public void setImage(Activity activiy, List<String> images, boolean isImageClick) {
        setImage(activiy, images, 1, isImageClick);
    }

    //设置row(最大3排)排3张图片的view    isImageClick:true 图片看可以点击
    public void setImage(Activity activiy, List<String> images, int row, boolean isImageClick) {
        if (!isViewInit) {
            initManagerPhoto(activiy);
            setIsImageClick(isImageClick);
            setImageParameter(getContext(), 3, row, 14, 6);
        }
        int imageMaxCount = 3 * row;
        if (images.size() > imageMaxCount) {
            images = images.subList(0, imageMaxCount);
        }
        setImagData(images);
        showImage(true);
    }


    //设置3排3张图片的view 并且可以获取图片
    public void setImageUpload(Activity activity) {
        setImageUpload(activity, false);
    }

    public void setImageUpload(Activity activity, boolean isOnlyShow) {
        if (!isViewInit) {
            imageAddView(getContext());
            setIsImageClick(true);

            setImageParameter(getContext(), 3, 3, 14, 6);
            initManagerPhoto(activity);
        }
        showImage(isOnlyShow);
    }

    @Override
    protected void imageClick(int index, int type) {
        if (managerPhoto == null) {
            return;
        }
        if (onImageTypeListener != null) {
            onImageTypeListener.onImageType(type);
        }
        //type 1：只显示 2：选择添加图片方式 3：可以删除
        switch (type) {
            case 1:
                managerPhoto.previewImage(getSourceIamgePaths(), index);
                break;
            case 2:
                photoPopupMenuView.showLocation(activityRoot, Gravity.BOTTOM);
                break;
            case 3:
                managerPhoto.previewImageDelect(getSourceIamgePaths(), index);
                break;
        }

    }

    //初始化管理器
    private void initManagerPhoto(Activity activity) {
        managerPhoto = new ImageSelectManager(activity);
        photoPopupMenuView = new PopupPhotoOption(activity);
        photoPopupMenuView.setOnPopupBackListener(new PhotoListener());
        photoPopupMenuView.setTitleText("上传照片");
        activityRoot = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);

    }

    //true: 拍照
    private boolean isTake;

    //获取相册选择的图片
    public List<ImageEntity> onActivityResult(int requestCode, int resultCode, Intent data) {
        List<ImageEntity> iamges = managerPhoto.onActivityResult(requestCode, resultCode, data);
        return iamges;
    }

    //
    public void setImagesPath(List<ImageEntity> images) {
        if (isTake) {
            setImagesTakePath(images);
        } else {
            setImagesOptionPath(images);
        }
        showImage();
    }

    //设置拍照的图片
    private void setImagesTakePath(List<ImageEntity> images) {
        setImageData(images, null);
    }

    //设置从相册选择的图片
    private void setImagesOptionPath(List<ImageEntity> images) {
        HashMap<String, ImagePath> imagesMap = getImages();
        imagePaths.clear();
        //
        setImageData(images, imagesMap);
    }

    //整合图片数据
    private void setImageData(List<ImageEntity> images, HashMap<String, ImagePath> imagesMap) {
        for (int i = 0; i < images.size(); i++) {
            ImageEntity image = images.get(i);
            String path = image.imagePath;
            String pathSource = image.imagePathSource;
            if (TextUtils.isEmpty(pathSource)) {
                pathSource = path;
            }
            ImagePath m = null;
            if (imagesMap != null) {
                m = imagesMap.get(pathSource);
            }
            if (m == null) {
                m = new ImagePath(pathSource, path);
            }
            imagePaths.add(m);
        }
    }

    //整合图片数据
    private void setImagData(List<String> images) {
        imagePaths.clear();
        for (int i = 0; i < images.size(); i++) {
            String url = images.get(i);
            ImagePath imagePath = new ImagePath();
            imagePath.setUrl("", url);
            imagePaths.add(imagePath);
        }

    }

    //设置获取图片的方式
    class PhotoListener implements BasePopupWindow.OnPopupBackListener  {

        @Override
        public void onPopupBack(int pupupType, int eventType, Object object) {
            switch (eventType) {
                case PopupPhotoOption.PHOTO_TYPE_LOCALITY:
                    //本地
                    isTake = false;
                    managerPhoto.getMoreConfig(9, getSourceIamgePaths());
                    break;
                case PopupPhotoOption.PHOTO_TYPE_CAMERA:
                    //相机拍照
                    isTake = true;
                    managerPhoto.getSinglePhotoConfig();
                    break;
                case PopupPhotoOption.PHOTO_TYPE_CANCEL:
                    //取消

                    break;
            }
        }
    }
    private OnImageTypeListener onImageTypeListener;

    public void setOnImageTypeListener(OnImageTypeListener onImageTypeListener) {
        this.onImageTypeListener = onImageTypeListener;
    }

    public interface OnImageTypeListener {
        void onImageType(int type);
    }
}

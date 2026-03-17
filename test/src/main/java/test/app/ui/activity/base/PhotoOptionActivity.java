package test.app.ui.activity.base;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;



import java.util.List;

import media.library.images.config.entity.MediaEntity;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.window.popup.BasePopupWindow;
import test.app.ui.window.popup.PopupPhotoOption;


/**
 * 选择单张照片
 * Created by junfengwang on 2017/1/5.
 */

public class PhotoOptionActivity extends NormalActionBar implements BasePopupWindow.OnPopupBackListener {
    private PopupPhotoOption photoPopupMenuView;
    private View view;
   // protected ImageSelectManager imageSelectManager;


    public void initSeteleView() {
        View view = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        initSeteleView(view);
    }

    public void initSeteleView(View view) {
        this.view = view;
        //imageSelectManager = new ImageSelectManager(this);
        photoPopupMenuView = new PopupPhotoOption(this);
        photoPopupMenuView.setOnPopupBackListener(this);
    }


    public void showView() {
        photoPopupMenuView.showLocation(view, Gravity.BOTTOM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*  List<MediaEntity> iamges = null;
        if (imageSelectManager != null) {
            iamges = imageSelectManager.onActivityResult(requestCode, resultCode, data);
        }
        if (iamges != null) {
            getImage(iamges);
        }*/
    }

    protected void setTitle(String popText) {
        photoPopupMenuView.setTitleText(popText);
    }

    protected void getImage(List<MediaEntity> images) {

    }

    @Override
    public void onPopupBack(int pupupType, int eventType, Object object) {
        switch (eventType) {
            case PopupPhotoOption.PHOTO_TYPE_LOCALITY:
                //本地
                onPhotoChoose();
                break;
            case PopupPhotoOption.PHOTO_TYPE_CAMERA:
                //相机拍照
                onPhotoTake();
                break;
            case PopupPhotoOption.PHOTO_TYPE_CANCEL:
                //取消
                onPhotoCancel();
                break;
        }
    }


    public void onPhotoChoose() {
    }

    public void onPhotoTake() {
      //  imageSelectManager.getSinglePhotoConfig();
    }

    public void onPhotoCancel() {
    }


}

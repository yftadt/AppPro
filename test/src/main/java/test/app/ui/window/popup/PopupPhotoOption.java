package test.app.ui.window.popup;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import test.app.ui.activity.R;


/**
 * Created by junfengwang on 2017/1/5.
 */

public class PopupPhotoOption extends BasePopupWindow implements View.OnClickListener {


    public PopupPhotoOption(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate() {
        setPopupWindowView(R.layout.popupview_option_photo);
        findViewById(R.id.camera_tv).setOnClickListener(this);
        findViewById(R.id.photo_tv).setOnClickListener(this);
        findViewById(R.id.cancel_tv).setOnClickListener(this);
    }

    public void setTitleText(String title) {
        ((TextView) findViewById(R.id.title_tv)).setText(title);
    }

    //选择本地
    public static final int PHOTO_TYPE_LOCALITY = 0;
    //照相
    public static final int PHOTO_TYPE_CAMERA = 1;
    //取消
    public static final int PHOTO_TYPE_CANCEL = 2;

    @Override
    public void onClick(View v) {
        dismiss();
        if (onPopupBackListener == null) {
            return;
        }
        int id = v.getId();
        if (id == R.id.photo_tv) {
            //选择本地照片
            onPopupBackListener.onPopupBack(POPUP_TYPE_PHOTO, PHOTO_TYPE_LOCALITY, null);

            return;
        }
        if (id == R.id.camera_tv) {
            //照相
            onPopupBackListener.onPopupBack(POPUP_TYPE_PHOTO, PHOTO_TYPE_CAMERA, null);

            return;
        }
        if (id == R.id.cancel_tv) {
            //取消
            onPopupBackListener.onPopupBack(POPUP_TYPE_PHOTO, PHOTO_TYPE_CANCEL, null);

            return;
        }


    }

}
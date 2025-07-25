package test.app.ui.view.images;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 * Created by Administrator on 2017/7/19.
 */

public class ImageLoadingView extends RelativeLayout {
    private ImageView iv;
    private TextView tv;
    private ImagePath image;

    public ImageLoadingView(Context context) {
        super(context);
        setImage();
    }

    public ImageLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setImage();
    }

    public ImageLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImage();
    }

    private void setImage() {
        iv = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        addView(iv);
        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        tv = new TextView(getContext());
        tv.setBackgroundColor(0x44333333);
        tv.setLayoutParams(layoutParams);
        tv.setText("正在上传");
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(0xffffffff);
        addView(tv);
        tv.setVisibility(View.GONE);
    }

    //
    public void setImage(ImagePath image) {
        this.image = image;
    }

    public ImagePath getImage() {
        return image;
    }

    //图片是否正在上传 true：正在上传
    public boolean isUploading() {
        return tv.getVisibility() == View.VISIBLE;
    }

    public void setLoadingBackgroundColor(int color) {
        tv.setBackgroundColor(color);
    }

    public void setLoadingBackgroundId(int resId) {
        tv.setBackgroundResource(resId);
    }


    public void setUpLodingShow() {
        tv.setVisibility(View.VISIBLE);
    }

    public void setUpLodingComplete() {
        tv.setVisibility(View.GONE);
    }

    public ImageView getImageView() {
        return iv;
    }

    public void setImageResource(int resId) {
        iv.setImageResource(resId);
    }

}

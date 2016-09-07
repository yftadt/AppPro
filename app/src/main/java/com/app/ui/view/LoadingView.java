package com.app.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.ui.activity.R;
import com.app.utiles.other.ImageUtile;


/**
 * Created by Administrator on 2016/3/30.
 */
public class LoadingView extends RelativeLayout {
    public LoadingView(Context context) {
        super(context);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private ImageView loagdingTailorIv;
    private ImageView loagdingFailureIv;
    private int backgColor = 0xfff1f1f1;

    private void init(Context context) {
        setBackgroundColor(backgColor);
        //
        ImageView loagdingFixationIv = new ImageView(context);
        loagdingFixationIv.setImageResource(R.mipmap.loading_fixation);
        RelativeLayout.LayoutParams rlpFixation = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlpFixation.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(loagdingFixationIv, rlpFixation);
        //
        tailorBitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.loading_tailor);
        loagdingTailorIv = new ImageView(context);
        loagdingTailorIv.setImageBitmap(tailorBitmap);
        RelativeLayout.LayoutParams rlpTailor = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlpTailor.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(loagdingTailorIv, rlpTailor);
        //
        loagdingFailureIv = new ImageView(context);
        loagdingFailureIv.setImageResource(R.mipmap.loading_failure);
        loagdingFailureIv.setBackgroundColor(backgColor);
        RelativeLayout.LayoutParams rlpFailure = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlpFailure.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(loagdingFailureIv, rlpFailure);
        loagdingFailureIv.setVisibility(View.GONE);
        loagdingFailureIv.setOnClickListener(new OnClick());
    }

    //开始显示加载效果
    public void startLoading() {
        handler.sendEmptyMessageDelayed(LOADING, 10);
    }

    //加载成功
    public void setLoadingSucceed() {
        handler.removeMessages(LOADING);
    }

    //加载失败
    public void setLoadingFailed() {
        if (loagdingFailureIv == null) {
            return;
        }
        if (loagdingFailureIv.getVisibility() == View.GONE) {
            loagdingFailureIv.setVisibility(View.VISIBLE);
        }
        handler.removeMessages(LOADING);
    }


    class OnClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            //重新加载
            if (loagdingFailureIv.getVisibility() == View.VISIBLE) {
                loagdingFailureIv.setVisibility(View.GONE);
            }
            handler.sendEmptyMessageDelayed(LOADING, 5);
            if (onResetLoagding == null) {
                return;
            }
            onResetLoagding.onResetRequest();
        }
    }

    // 加载中
    private final int LOADING = -1;
    /**
     * 当前百分数
     */
    private int num = 0;
    private Bitmap tailorBitmap;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    num++;
                    if (num > 100) {
                        num = 0;
                    }
                    loagdingTailorIv.setImageBitmap(ImageUtile.getRoundedCornerBitmap(
                            tailorBitmap, num));
                    handler.sendEmptyMessageDelayed(LOADING, 5);
                    break;
                default:

                    break;
            }
        }
    };
    private OnResetLoagding onResetLoagding;

    public void setOnResetLoagding(OnResetLoagding onResetLoagding) {
        this.onResetLoagding = onResetLoagding;
    }

    public interface OnResetLoagding {
        void onResetRequest();
    }
}

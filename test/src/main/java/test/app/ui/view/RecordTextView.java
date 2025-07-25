package test.app.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.library.baseui.utile.file.FileUtile;
import com.library.baseui.utile.file.Md5Utile;
import com.library.baseui.utile.media.MediaRecorderManager;
import com.library.baseui.utile.toast.ToastUtile;

import java.io.File;

import test.app.ui.activity.R;
import test.app.utiles.other.DLog;
import test.app.utiles.other.PermissionManager;
import test.app.utiles.other.Permissions;


public class RecordTextView extends TextView {
    /**
     * 是否取消录音
     */
    public boolean isCancel;
    private RecordHandler recordHandler;
    //声音录制状态的显示
    private View soundStateView;
    //声音录制进行中显示
    private View soundProceedLl;
    //声音录制取消显示
    private View soundCancelLl;
    //声音录制音量大小显示
    private ImageView soundVolumeIv;
    private String path ;

    public RecordTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RecordTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecordTextView(Context context) {
        super(context);
    }

    private Activity activity;

    public void setView(View view, Activity activity) {
        soundStateView = view;
        soundProceedLl = view.findViewById(R.id.sound_proceed_ll);
        soundCancelLl = view.findViewById(R.id.sound_cancel_ll);
        soundVolumeIv = (ImageView) view.findViewById(R.id.sound_volume_iv);
        recordHandler = new RecordHandler();
        this.activity = activity;
    }

    private long downTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!Environment.getExternalStorageDirectory().exists()) {
            ToastUtile.showToast("No SDCard");
            return false;
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_CANCEL:
                //取消录音
                if (isCancel) {
                    break;
                }
                recordHandler.sendEmptyMessage(5);
                break;

            case MotionEvent.ACTION_DOWN:
                boolean isNoPermission = PermissionManager.getInstance().isCheckPermission(
                        activity, Permissions.permission_record, 300);
                if (isNoPermission) {
                    isCancel = true;
                    break;
                }
                isCancel = false;
                loosenCancel = false;
                downTime = System.currentTimeMillis();
                //
                String fileName= Md5Utile.encode(downTime + "");
                path = FileUtile.getVoiceCacheFileName(fileName) + ".amr";
                MediaRecorderManager.getInstance().setAudioOutput(path);
                MediaRecorderManager.getInstance().setMediaWork(0);
                MediaRecorderManager.getInstance().setMediaWork(1);
                //
                recordHandler.sendEmptyMessage(1);
                //20秒超时
                recordHandler.sendEmptyMessageDelayed(4, 2 * 60 * 1000);
                recordHandler.sendEmptyMessageDelayed(6, 500);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isCancel) {
                    break;
                }
                float y = event.getY();
                Message msg = recordHandler.obtainMessage();
                msg.what = 2;
                msg.obj = y;
                recordHandler.sendMessage(msg);
                break;
            case MotionEvent.ACTION_UP:
                recordHandler.removeMessages(4);
                recordHandler.removeMessages(6);
                if (isCancel) {
                    break;
                }
                if (loosenCancel) {
                    //取消录音
                    recordHandler.sendEmptyMessage(5);
                    break;
                }
                long upTime = System.currentTimeMillis();
                double time = (upTime - downTime) / 1000;
                msg = recordHandler.obtainMessage();
                msg.what = 3;
                msg.obj = time;
                // 录音完成
                recordHandler.sendMessage(msg);
                DLog.e("录音时间=" + time, "----------");
                break;
        }

        return super.onTouchEvent(event);
    }

    //true 松开取消录音
    private boolean loosenCancel;

    public class RecordHandler extends Handler {
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // 开始录音
                    soundStateView.setVisibility(View.VISIBLE);
                    soundProceedLl.setVisibility(View.VISIBLE);
                    soundCancelLl.setVisibility(View.GONE);
                    break;
                case 2:
                    // 取消录音逻辑
                    float y = (Float) (msg.obj);
                    if (y > 0) {
                        break;
                    }
                    y = -y;
                    if (y > 300) {
                        //松开取消录音
                        loosenCancel = true;
                        soundProceedLl.setVisibility(View.GONE);
                        soundCancelLl.setVisibility(View.VISIBLE);
                        break;
                    }
                    loosenCancel = false;
                    soundProceedLl.setVisibility(View.VISIBLE);
                    soundCancelLl.setVisibility(View.GONE);
                    // this.sendEmptyMessage(5);
                    break;
                case 3:
                    // 录音完成
                    soundStateView.setVisibility(View.GONE);
                    isCancel = true;
                    MediaRecorderManager.getInstance().setMediaWork(4);

                     DLog.e("录音文件", "path=" + path);
                    if (onCompleteSoundListener == null) {
                        break;
                    }
                    double time = (double) msg.obj;
                    if (time < 1) {
                        ToastUtile.showToast("时间太短");
                        break;
                    }
                    if (TextUtils.isEmpty(path)) {
                        return;
                    }
                    File file = new File(path);
                    if (!file.exists()) {
                        return;
                    }
                    onCompleteSoundListener.onCompleteSound(path, time);
                    break;
                case 4:
                    // 录音超时
                    isCancel = true;
                    MediaRecorderManager.getInstance().setMediaWork(4);
                    //
                    file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                    ToastUtile.showToast("录音超时");
                    soundStateView.setVisibility(View.GONE);
                    break;
                case 5:
                    // 录音取消
                    isCancel = true;
                    MediaRecorderManager.getInstance().setMediaWork(4);
                    //
                    file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                    this.removeMessages(4);
                    ToastUtile.showToast("录音取消");
                    soundStateView.setVisibility(View.GONE);
                    break;
                case 6:
                    // 录音声音大小更新
                    updateSoundVolume();
                    if (isCancel) {
                        break;
                    }
                    sendEmptyMessageDelayed(6, 500);
                    break;
            }
        }
    }
    //更换声音大小标志
    private void updateSoundVolume() {
        int soundVolume =   MediaRecorderManager.getInstance().getAudioAmplitude(7);
        switch (soundVolume) {

            case 1:
                soundVolumeIv.setImageResource(R.drawable.sound_volume_1);
                break;
            case 2:

                soundVolumeIv.setImageResource(R.drawable.sound_volume_2);
                break;
            case 3:
                soundVolumeIv.setImageResource(R.drawable.sound_volume_3);
                break;
            case 4:
                soundVolumeIv.setImageResource(R.drawable.sound_volume_4);
                break;
            case 5:
                soundVolumeIv.setImageResource(R.drawable.sound_volume_5);
                break;
            case 6:
                soundVolumeIv.setImageResource(R.drawable.sound_volume_5);
                break;
            default:
                soundVolumeIv.setImageResource(R.drawable.sound_volume_5);
                break;
        }
    }

    private OnCompleteSoundListener onCompleteSoundListener;

    public void setOnCompleteSoundListener(OnCompleteSoundListener onCompleteSoundListener) {
        this.onCompleteSoundListener = onCompleteSoundListener;
    }

    //完成录音监听
    public interface OnCompleteSoundListener {
        //语音路径，语音长度
        public void onCompleteSound(String path, double length);
    }
}

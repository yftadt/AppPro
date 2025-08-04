package test.app.ui.activity.test;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;


import com.library.baseui.utile.app.APKInfo;
import com.library.baseui.utile.file.FileUtile;
import com.library.baseui.utile.file.Md5Utile;
import com.library.baseui.utile.media.MediaPlayerManager;
import com.library.baseui.utile.media.MediaRecorderManager;
import com.library.baseui.view.text.TextViewManager;
import com.retrofits.utiles.Json;


import java.util.ArrayList;

import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.view.text.ExpansionTextView;
import test.app.ui.view.RecordTextView;
import test.app.utiles.other.DLog;

//测试音频
public class TestMusicActivity extends NormalActionBar {


    private RecordTextView sayTv;
    private View chatPopup;
    protected Handler handler;// 用来传递事件
    private TextView msgTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_music);
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "测试音频");

        sayTv = (RecordTextView) findViewById(R.id.chat_say_tv);
        chatPopup = findViewById(R.id.chat_popup);
        sayTv.setView(chatPopup, this);
        msgTv = (TextView) findViewById(R.id.msg_tv);
        String msg = "毛玉娟";
        TextPaint p = msgTv.getPaint();
        float px1 = APKInfo.getInstance().getDIPTOPX(210) * 2;
        float px2 = APKInfo.getInstance().getDIPTOPX(40);
        int cd = TextViewManager.getTextWidth(p, msg);
        //String str = TextViewManager.getSuitableText((int) (px1 - px2), p, msg);
        DLog.e("-------", "cd:" + cd);
        //msgTv.setText(str);
        findViewById(R.id.tets_1_btn).setOnClickListener(this);
        findViewById(R.id.tets_2_btn).setOnClickListener(this);
        findViewById(R.id.test_zt_tv).setOnClickListener(this);
        findViewById(R.id.test_ks_tv).setOnClickListener(this);
        findViewById(R.id.test_js_tv).setOnClickListener(this);
        MediaPlayerManager.getInstance().setAddOnMediaListener(new L());
        MediaRecorderManager.getInstance().setOnRecorderListener(new OnMediaRecorder());

        TextView msg2tv = (TextView) findViewById(R.id.msg_2_tv);
        msg2tv.setText("肠镜检查及内镜下治疗、痔动脉结扎及肛门直肠测压肛45252525dgdfgdfgdfg");
        //msg2tv.setText("肠镜检查及内镜下治疗、痔动脉结扎及肛门直肠测压、肛门内窥镜下微创手术空肠镜检查及内镜下治疗、痔动脉结扎及肛门直肠测压、肛门内窥镜下微创手术空");
        ExpansionTextView etView = (ExpansionTextView) findViewById(R.id.mdoc_good_view);
        //etView.setText("治疗、痔");
        etView.setText("肠镜检查及内镜下治疗、痔动脉结扎及肛门直肠测压、肛门内窥镜下微创手术空肠镜检查及内镜下治、疗、痔动脉结扎及肛门直肠测压、肛门内窥镜下微创手术空");


    }

    private int i = 1;

    @Override
    protected void onClick(int id) {
        if (id == R.id.tets_1_btn) {
            MediaPlayerManager.getInstance().setDataSource("", "/storage/emulated/0/cs/sound/e1bf037c2d09d152f1112bec890df9bb.amr");
            MediaPlayerManager.getInstance().setMediaWork(0);

            return;
        }
        if (id == R.id.tets_2_btn) {
            //MediaPlayerManager.getInstance().setDataSourcePlay("http://abv.cn/music/光辉岁月.mp3", "");
            MediaPlayerManager.getInstance().setDataSource("http://abv.cn/music/光辉岁月.mp3", "");
            MediaPlayerManager.getInstance().setMediaWork(0);
            return;
        }
        if (id == R.id.test_ks_tv) {
            long downTime = System.currentTimeMillis();
            String fileName = Md5Utile.encode(downTime + "");
            String path = FileUtile.getVoiceCacheFileName(fileName) + ".amr";
            DLog.e("path", path);
            MediaRecorderManager.getInstance();
            MediaRecorderManager.getInstance().setAudioOutput(path,
                    MediaRecorder.OutputFormat.AMR_NB,
                    MediaRecorder.AudioEncoder.AMR_NB);
            // MediaRecorderManager.getInstance().setAudioOutput(path);
            MediaRecorderManager.getInstance().setMediaStart();
            return;
        }
        if (id == R.id.test_js_tv) {
            MediaRecorderManager.getInstance().setMediaStop();
            return;
        }
        if (id == R.id.test_zt_tv) {
            i++;
            if (i % 2 == 0) {
                MediaRecorderManager.getInstance().setMediaPause();
            } else {
                MediaRecorderManager.getInstance().setMediaResume();
            }
            return;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.getInstance().setMediaWork(3);
        MediaPlayerManager.getInstance().onDestroy();
    }

    class OnMediaRecorder implements MediaRecorderManager.OnMediaRecorderListener {

        @Override
        public void onRecorderState(int state, long progressTime) {
            ArrayList<String> paths = MediaRecorderManager.getInstance().getRecorderPaths();
            DLog.e("onRecorderState", "" + state);
            DLog.e("onRecorderState", "paths:" + Json.obj2Json(paths));
        }

        @Override
        public void onRecorderTime(long recordTotalTime, long progressTime) {
            DLog.e("onRecorderTime", "" + progressTime);
        }

        @Override
        public void onVoiceAmplitude() {
            DLog.e("onVoiceAmplitude", "");
        }

        @Override
        public void onRecorderTimeOut(long recordTotalTime, long progressTime) {
            DLog.e("onRecorderTimeOut", "");
        }
    }

    class L implements MediaPlayerManager.OnMediaPlayerListener {

        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            DLog.e("大小", "====================");
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            DLog.e("缓冲", "====================");
        }

        @Override
        public void onPayState(MediaPlayer mp, int state, String url) {

        }


        @Override
        public void onError(MediaPlayer mp, int what, int extra) {
            DLog.e("错误", "====================");
        }


    }
}

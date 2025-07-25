package test.app.ui.activity.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;

import android.view.View;
import android.widget.TextView;



import java.io.File;
import java.util.ArrayList;



import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.library.baseui.utile.toast.ToastUtile;

import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.adapter.video.AddArtVideoAdapter;
import test.app.ui.adapter.video.RVItemVideoDecoration;
import test.app.ui.bean.VideoData;
import test.app.utiles.other.DLog;

//视频扫描测试
public class TestVideoScanActivity extends NormalActionBar {

    private ScanMediaReceive scanSdReceiver;
    private RecyclerView recyclerView;
    private TextView msgTv;
    private AddArtVideoAdapter adapter;
    //true扫描中
    private boolean isScan;//private TextView startTv;
    //1:扫描指定文件 2：扫描全部
    private int scanType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_video_scan, false);
        setBarTvText(1, "视频扫描测试");
        msgTv = (TextView) findViewById(R.id.scan_msg_tv);
        findViewById(R.id.scan_start_1_tv).setOnClickListener(this);
        findViewById(R.id.scan_start_2_tv).setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        //
        adapter = new AddArtVideoAdapter(this);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new RVItemVideoDecoration());
        recyclerView.setAdapter(adapter);


    }

    //获取视频
    private void initData() {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            loadingSucceed();
            return;
        }
        ArrayList<VideoData> videos = new ArrayList<>();
        while (cursor.moveToNext()) {
            //视频文件名称
            int videoNameIndex = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
            String videoName = cursor.getString(videoNameIndex);
            //视频缩略图的文件路径
            int videoImageIndex = cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA);
            String videoImagePath = cursor.getString(videoImageIndex);
            //视频文件路径
            int videoIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
            String videoPath = cursor.getString(videoIndex);
            //视频标题
            int titleIndex = cursor.getColumnIndex(MediaStore.Video.Media.TITLE);
            String videoTitle = cursor.getString(titleIndex);
            //视频大小
            int videoSizeIndex = cursor.getColumnIndex(MediaStore.Video.Media.SIZE);
            String videoSize = cursor.getString(videoSizeIndex);
            //视频时长
            int videoLengthIndex = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);
            String videoLength = cursor.getString(videoLengthIndex);
            //DLog.e("视频", "名称:" + videoName + " 缩略图:" + videoImagePath + " 文件:" + videoPath +
            //       " 标题:" + videoTitle + " 大小:" + videoSize + " 时长：" + videoLength);
            VideoData video = new VideoData();
            video.videoName = videoName;
            video.videoImagePath = videoImagePath;
            video.videoPath = videoPath;
            video.videSize = videoSize;
            video.videoLength = videoLength;
            videos.add(video);
        }
        cursor.close();
        int isShow = videos.size() == 0 ? View.VISIBLE : View.GONE;
        findViewById(R.id.scan_empty_tv).setVisibility(isShow);
        loadingSucceed();
        adapter.setData(videos);
        DLog.e("从数据库读取", ".................");
    }

    @Override
    protected void onClick(int id) {
        dialogShow();
        if (isScan) {
            ToastUtile.showToast("正在扫描....");
            return;
        }
        time = 0;
        if (id == R.id.scan_start_1_tv) {
            scanType = 1;
            satrtScan1();
            getHandler().sendEmptyMessageDelayed(1, 1000);

            return;
        }
        if (id == R.id.scan_start_2_tv) {
            scanType = 2;
            satrtScan2();
            getHandler().sendEmptyMessageDelayed(1, 1000);
            return;

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scanSdReceiver != null) {
            unregisterReceiver(scanSdReceiver);
        }
    }

    private int time;

    //4.4以上，以下都行
    private void satrtScan1() {
        isScan = true;
        String root = Environment.getExternalStorageDirectory().toString();
        String qq = root + "/tencent/QQfile_recv";
        String weix = root + "/tencent/micromsg/WeiXin";
        String test = root + "/video_test/天龙八部2.mp4";

        DLog.e("扫描文件：", "qq:" + qq + "\nweix:" + weix);
        String[] paths = new String[]{qq, weix, test};
        MediaScannerConnection.scanFile(this, paths, null, new OnScanCompleted());
    }

    private void start3() {
        String[] paths = new String[]{Environment.getExternalStorageDirectory().toString()};
        MediaScannerConnection.scanFile(this, paths, null, null);
    }

    //扫描全局
    private void satrtScan2() {
        isScan = true;
        String root = Environment.getExternalStorageDirectory().toString();

        String[] paths = new String[]{root};
        MediaScannerConnection.scanFile(this, paths, null, new OnScanCompleted());
    }

    //开始扫描
    private void satrtScan() {
        isScan = true;
        String root = Environment.getExternalStorageDirectory().toString();
        String qq = root + "/tencent/QQfile_recv";
        String weix = root + "/tencent/micromsg/WeiXin";
        String test = root + "/video_test";
        DLog.e("扫描文件：", "qq:" + qq + "\nweix:" + weix);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 判断SDK版本是不是4.4或者高于4.4

            String[] paths = new String[]{qq, weix, test};
            DLog.e("扫描文件", paths[0] + "\n" + paths[1] + "\n" + paths[2]);
            MediaScannerConnection.scanFile(this, paths, null, new OnScanCompleted());
            return;
        }
        //4.4以下==================无效===================
        DLog.e("扫描文件：", "4.4以下");
        //扫描指定目录
        String dir = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";
        //扫描指定文件
        String action = Intent.ACTION_MEDIA_SCANNER_SCAN_FILE;
        if (scanSdReceiver == null) {
            scanSdReceiver = new ScanMediaReceive();
        }
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentfilter.addAction("test");
        registerReceiver(scanSdReceiver, intentfilter);
        //
        File file = new File(qq);
        if (!file.exists()) {
            file.mkdirs();
        }
        Uri uri = Uri.fromFile(file);
        DLog.e("扫描文件：", "uri:" + uri);
        Intent scanIntent = new Intent(dir);
        scanIntent.setAction("test");
        scanIntent.setData(uri);
        sendBroadcast(scanIntent);
    }

    //4.4以下 ------扫描完成-------------
    class ScanMediaReceive extends BroadcastReceiver {
        private int count = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            DLog.e("扫描中", "" + action);
            if (!Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
                return;
            }
            count += 1;
            DLog.e("4.4结束扫描", "============");
            if (count % 2 != 0) {
                return;
            }
            getHandler().removeMessages(1);
            isScan = false;
            getHandler().sendEmptyMessage(2);

        }
    }

    // 扫描完成
    class OnScanCompleted implements MediaScannerConnection.OnScanCompletedListener {

        @Override
        public void onScanCompleted(String path, Uri uri) {
            DLog.e("扫描结果", "path:" + path + " uri:" + uri);
            getHandler().removeMessages(1);
            isScan = false;
            getHandler().sendEmptyMessage(2);

        }
    }

    @Override
    protected void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                time += 1;
                getHandler().sendEmptyMessageDelayed(1, 1000);
                break;
            case 2:
                dialogDismiss();
                initData();
                break;
        }

    }

}

package test.app.ui.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;

import sj.mblog.Logx;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.web.WebViewManager;
import test.app.utiles.other.DLog;


/**
 * title = "<h3 align = center >" + title + "</h3>";
 * webView.loadDataWithBaseURL(null,
 * "<style>img{border:0;width:100%;}</style>" + title + content, "text/html",
 * "utf-8", null);
 */
public class WebView2Activity extends NormalActionBar {


    private WebViewManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview2);

        setBarColor();
        setBarTvText(1, "详情");
        FrameLayout frameLayout = findViewById(R.id.frame_layout);
        EditText tvUrl = (EditText) findViewById(R.id.tv_url);
        manager = new WebViewManager(this, frameLayout);
        String url = "https://vjwkb.yhzu.cn/index.php?r=index/wap";
        url = "https://www.admin.umqnb.com/full/protocol/logoutProtocol.html?isHideTitle=1&nbcopen://m.nbc.com";
        url = "http://10.168.3.101:10012/full/share/shareInfo.html";
        String data = "/data/user/0/com.app.baseui/cache/image_manager_disk_cache/82d4c425e47188afaab829c1394ea038dfc57919cbe47e15d7bbe658475252fd.0";
        //
       /* File file = new File(data);
        Uri imageUri = Uri.fromFile(file);
        url=imageUri.toString();*/
        //
        tvUrl.setText(url);
        //url="https://u.jd.com/CG2bOAo"
        manager.startLoadUrl(url);
        findViewById(R.id.btn_geturl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = manager.getNowUrl();
                Logx.d("webView   当前页面\n$str");
            }
        });


    }

    @Override
    public void onBackPressed() {
        boolean isBack = manager.onBackPressed();
        if (isBack == true) {
            return;
        }
        super.onBackPressed();
    }


}

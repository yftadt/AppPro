package test.app.ui.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;


import com.library.baseui.utile.img.ImageBase64;
import com.library.baseui.utile.img.ImageLoadingUtile;
import com.library.baseui.utile.img.ImageUtile;
import com.library.baseui.utile.toast.ToastUtile;

import java.io.Serializable;

import test.app.ui.activity.action.NormalActionBar;
import test.app.utiles.other.DLog;


/**
 * title = "<h3 align = center >" + title + "</h3>";
 * webView.loadDataWithBaseURL(null,
 * "<style>img{border:0;width:100%;}</style>" + title + content, "text/html",
 * "utf-8", null);
 */
public class WebViewActivity extends NormalActionBar {

    ImageView ivImg;
    TextView tvImg, tvText;
    WebView webView;
    TextView webTitleTv;
    TextView webTv;

    private boolean loadingFailed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        setBarColor();
        setBarTvText(1, "详情");
        ivImg = findViewById(R.id.iv_img);
        tvImg = findViewById(R.id.tv_img);
        tvText = findViewById(R.id.tv_text);

        webView = (WebView) findViewById(R.id.web_view);
        webTitleTv = (TextView) findViewById(R.id.web_title_tv);
        webTv = (TextView) findViewById(R.id.web_tv);
        String title = getStringExtra("arg0");
        String url = getStringExtra("arg1");
        String htmlData = getStringExtra("arg2");
        if (!TextUtils.isEmpty(title)) {
            webTitleTv.setText(title);
            webTitleTv.setVisibility(View.VISIBLE);
        }
        Serializable fixationTag = getObjectExtra("bean");
        if (fixationTag != null) {
            webTv.setVisibility(View.VISIBLE);
        }
        if (fixationTag != null && fixationTag instanceof String) {
            webTv.setText((String) fixationTag);
        }


        WebSettings settings = webView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDefaultTextEncodingName("UTF -8");
        settings.setJavaScriptEnabled(true);
        //设置显示图片
        settings.setBlockNetworkImage(false);
        //支持显示https 网址图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //显示h5网页
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("shouldOverrideUrl", "url:" + url);
                boolean isUrl = url.startsWith("http");
                if (!isUrl) {
                    isUrl = url.startsWith("www");
                }

                if (!isUrl) {
                    // 自定义处理逻辑
                    ToastUtile.showToast("重定向");
                    // ...
                    return true;
                }
                //view.loadUrl(url);
                return false;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                Log.e("onLoadResource", "url:" + url);

                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("onPageFinished", "url:" + url);

                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                DLog.e("onPageStarted", "url:" + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //super.onReceivedError(view, errorCode, description, failingUrl);
                loadingFailed = true;
            }
        });


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100 && !loadingFailed) {
                    // 网页加载完成
                    DLog.e("onProgressChanged", "网页加载完成:" + newProgress);
                }
                if (newProgress == 100 && loadingFailed) {
                    loadingFailed = false;
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                DLog.e("onReceivedTitle", "title:" + title);
                if (!TextUtils.isEmpty(title) && title.contains("404")) {
                    webView.stopLoading();
                    finish();
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                DLog.e("onJsAlert", "url:" + url + " message:" + message);
                return super.onJsAlert(view, url, message, result);
            }
        });
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
            return;
        }
        if (!TextUtils.isEmpty(htmlData)) {
            webView.loadData(htmlData, "text/html; charset=UTF-8", null);
        }
        setWebViewTest();
    }

    private String img = "https://ww2.sinaimg.cn/mw690/007ut4Uhly1hx4v37mpxcj30u017cgrv.jpg";

    private void setWebViewTest() {
        ImageLoadingUtile.loading(this, img, ivImg);
        tvImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivImg.buildDrawingCache();
                Bitmap bit = ivImg.getDrawingCache();
                String str = ImageBase64.convertBitmapToBase64Def(bit);
                String body = "这里是邮件正文，并包含了一个图片：\n<img src=\"data:image/png;base64," + str + "\"/>";

                webView.loadData(body, "text/html; charset=UTF-8", null);
                String body1 = "这里是邮件正文，并包含了一个图片：\n<img src=\"" + str + "\"/>";

                Spanned htmlData = Html.fromHtml(body1);//不支持base64字符串图片
                tvText.setText(htmlData);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }


}

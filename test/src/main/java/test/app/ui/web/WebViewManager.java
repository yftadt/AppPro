package test.app.ui.web;

import android.app.Activity;
import android.webkit.WebView;
import android.widget.FrameLayout;



import java.util.ArrayList;
import java.util.HashMap;

import sj.mblog.Logx;

public class WebViewManager {
    private Activity act;
    private FrameLayout frameLayout;

    public WebViewManager(Activity act, FrameLayout ft) {
        this.act = act;
        this.frameLayout = ft;
    }

    private HashMap<String, WebView> map = new HashMap<>();
    private ArrayList<String> urls = new ArrayList<>();

    public void startLoadUrl(String url) {
        onLoadUrl(url);
    }

    public boolean onBackPressed() {
        if (urls.size() == 0) {
            return false;
        }
        String url = urls.get(urls.size() - 1);
        WebView tempWebView = map.get(url);
        if (tempWebView == null) {
            return false;
        }
        if (tempWebView.canGoBack()) {
            tempWebView.goBack();
            return true;
        }
        if ((urls.size() == 1 && !tempWebView.canGoBack())) {
            return false;
        }
        Logx.d("webView 删除url", url);
        frameLayout.removeView(tempWebView);
        map.remove(url);
        urls.remove(urls.size() - 1);
        return true;
    }

    private int index = 0;

    private void onLoadUrl(String url) {
        WebViewUse webViewUse = new WebViewUse(new WedUse());
        WebView web = webViewUse.getWebView(act, url);
        frameLayout.addView(web);
        if (urls.contains(url)) {
            index += 0;
            url += ":wm_" + index;
        }
        Logx.d("webView 加载新的url", url);
        urls.add(url);
        map.put(url, web);
    }

    //获取当前页面加载的url
    public String getNowUrl() {
        String str = "";
        if (urls.size() > 0) {
            String url = urls.get(urls.size() - 1);
            WebView tempWebView = map.get(url);
            str = tempWebView.getUrl();
        }
        return str;
    }

    class WedUse implements WebViewUse.WedUseListener {

        @Override
        public void onNewLoadUrl(String url) {
            onLoadUrl(url);
        }

        @Override
        public void onNonWebsit(String link) {

        }

        @Override
        public boolean isIntercept(String url) {
            boolean isIntercept = false;
            if (url.contains("login.m.taobao.com")) {
                isIntercept = true;
            }
            return isIntercept;
        }
    }

}

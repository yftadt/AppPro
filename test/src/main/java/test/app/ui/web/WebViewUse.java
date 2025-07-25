package test.app.ui.web;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;

import sj.mblog.Logx;

public class WebViewUse {
    private Activity act;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private WedUseListener wedUseListener;

    public WebViewUse(WedUseListener wedUseListener) {
        this.wedUseListener = wedUseListener;
    }

    private WebView web;

    public WebView getWebView(Activity act, String url) {
        this.act = act;
        if (web == null) {
            web = new WebView(act);
            setSetting(web.getSettings());
            web.setWebViewClient(new WebClient());
            web.setWebChromeClient(new WebProgressClient());
            web.loadUrl(url);
        }
        return web;
    }

    private void setSetting(WebSettings webSettings) {
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false);  //隐藏原生的缩放控件

        //其他细节操作  setCacheMode(WebSettings.LOAD_NO_CACHE);LOAD_CACHE_ELSE_NETWORK
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);   //关闭webview中缓存
        webSettings.setAllowContentAccess(true); //设置可以访问文件
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setDefaultTextEncodingName("uft-8");    //设置编码格式
        webSettings.setDomStorageEnabled(true);
    }

    class WebProgressClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
        }
    }

    class WebClient extends WebViewClient {
        private boolean isFinished = false;
        //加载完成的url（包括 ：网页没有新加载页面，即它是一个内部跳转，网址会更新）
        ArrayList urlFinishes = new ArrayList();
        //被拦截的url
        ArrayList urlOff = new ArrayList();

        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            super.onPageStarted(webView, s, bitmap);
        }
        //API 24 中不推荐使用
       /* @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
            if (isIntercept("Loading1", webView)) {
                return true;
            }
            return super.shouldOverrideUrlLoading(webView, s);
        }*/

        /* private boolean isIntercept(String tag, WebView webView) {
             boolean isIntercept = false;
             Logx.d("webView", tag);
             String loadUrl = webView.getUrl();
             Logx.d("webView " + tag + "请求url", loadUrl);
             if (isFinished && !urlFinishes.contains(loadUrl)) {
                 //拦截
                 Logx.d("webView " + tag + " 拦截url", loadUrl);
                 wedUseListener.loadUrl(loadUrl);
                 return true;
             }
             return isIntercept;
         }
 */


        /**
         * API 24（或以后 ）
         * 拦截的是url加载阶段，主要拦截url
         * 执行时机是当一个新页面即将被打开或重定向时（网页自动重定向或手动点击网页内部链接）。
         * 但是此方法不会拦截来自内部的资源加载，
         * 例如，来自 HTML 或 Script 标签中的 iframe 或 src 属性。
         * 另外 XmlHttpRequests也不会被拦截，为了拦截这些请求，就可以使用
         * WebViewClient shouldInterceptRequest方法。
         *
         * @param webView
         * @param webResourceRequest
         * @return
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            Logx.d("webView", "Loading2");
            String loadUrl = webView.getUrl();
            Logx.d("webView Loading2 请求url", loadUrl);
            String urlReq = webResourceRequest.getUrl().toString();
            Logx.d("webView Loading2 请求urlReq", urlReq);
            if (webResourceRequest.isRedirect()) {
                Logx.d("webView Loading2 是重定向:urlReq:" + urlReq);
                return super.shouldOverrideUrlLoading(webView, webResourceRequest);
            }
            if (!isLinkUrl(urlReq)) {
                //不是网页
                Logx.d("webView Loading2 请求拒绝1 不是网页" + urlReq);
                wedUseListener.onNonWebsit(urlReq);
                return true;
            }
            if (wedUseListener.isIntercept(urlReq)) {
                //捕获这个网址
                Logx.d("webView Loading2 网址被捕获了" + urlReq);
                return true;
            }
            if (isFinished && !urlFinishes.contains(urlReq)) {
                //拦截，（新请求，加载到新页面）
                Logx.d("webView Loading2 拦截urlReq 打开新页面", urlReq);
                urlOff.add(urlReq);
                wedUseListener.onNewLoadUrl(urlReq);
                return true;
            }
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            isFinished = true;
            String url = webView.getUrl();
            Logx.d("webView", "加载完成url=" + url);
            if (urlOff.contains(url)) {
                webView.goBack();
                Logx.d("webView", "加载完成但是要回退" + url);
            }
        }


        @Override
        public void onReceivedLoginRequest(WebView webView, String s, String s1, String s2) {
            super.onReceivedLoginRequest(webView, s, s1, s2);
        }


        /**
         * API 21  非UI线程
         * 加载的是响应主体阶段，可拦截url、js、css等
         *
         * @param webView
         * @param request
         * @return
         */
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest request) {
            //WebResourceResponse response = new WebResourceResponse("image/jpeg", "UTF-8", stream);
            if (interceptUrl(request)) {
                //
                Logx.d("webView   拦截打开淘宝登录页面31");
                //终止请求 这里
                //return new WebResourceResponse("text/plain", "UTF-8", new ByteArrayInputStream(new byte[0]));
                return new WebResourceResponse("text/plain", "UTF-8",  new ByteArrayInputStream("测试".getBytes()));
                //返回 null 仍然会继续加载
                //  return new WebResourceResponse("text/plain", "UTF-8", null);

            }

            WebResourceResponse temp = super.shouldInterceptRequest(webView, request);
            return temp;
        }

        private void readInputStream(InputStream inputStream) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                Logx.d("webView   拦截打开淘宝登录页面4=>" + e.getMessage());
            }
            Logx.d("webView   拦截打开淘宝登录页面4=>" + stringBuilder.toString());
        }

        @Override
        public void onLoadResource(WebView webView, String resUrl) {
            super.onLoadResource(webView, resUrl);
            String url = webView.getUrl();
            if (urlOff.contains(url)) {
                return;
            }
            if (!urlFinishes.contains(url)) {
                urlFinishes.add(url);
            }
            //Logx.d("webView onLoadResource  \n加载网址", url + "  \n资源=" + resUrl);
        }

        @Override
        public void onReceivedError(WebView webView, int i, String errorMsg, String url) {
            super.onReceivedError(webView, i, errorMsg, errorMsg);
            Logx.d("webView", "加载出错 msg=" + errorMsg + " url=" + url);
            String urlLoading = webView.getUrl();
            String originalUrl = webView.getOriginalUrl();
            Logx.d("webView", "加载出错 urlLoading=" + urlLoading + " \noriginalUrl=" + originalUrl);

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    }

    private boolean isLinkUrl(String url) {
        boolean isLinkUrl = false;
        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("www.")) {
            isLinkUrl = true;
        }
        return isLinkUrl;
    }

    interface WedUseListener {
        //新的网页
        void onNewLoadUrl(String url);

        //不是网站
        void onNonWebsit(String link);

        //true ：捕获这个网址
        boolean isIntercept(String url);
    }

    private WebResourceResponse getWebResponse(int type, File file) {
        WebResourceResponse response = null;
        switch (type) {
            case 1:
                try {
                    // 加载内置资源文件
                    InputStream inputStream = act.getResources().getAssets().open("/asset/xxx");
                    // 获取 mimeType
                    String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
                    // 返回 WebResourceResponse
                    response = new WebResourceResponse(mimeType, "UTF-8", inputStream);
                } catch (Exception e) {
                    Logx.d(e.getMessage());
                }
                break;
            case 2:
                try {
                    // 加载存储文件
                    InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                    // 获取 mimeType
                    String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
                    // 返回 WebResourceResponse
                    response = new WebResourceResponse(mimeType, "UTF-8", inputStream);
                } catch (Exception e) {
                    Logx.d(e.getMessage());
                }
                break;
            case 3:
                // 加载 HTML
                String content = "<html>\n" +
                        "<title>标题</title>\n" +
                        "<body>\n" +
                        "<a href=\"www.baidu.com\">百度</a>,测试连接\n" +
                        "</body>\n" +
                        "<html>";
                response = new WebResourceResponse("text/html", "UTF-8", new ByteArrayInputStream(content.getBytes()));
                break;
        }
        return response;
    }

    private boolean interceptUrl(WebResourceRequest req) {
        boolean interceptUrl = false;
        String requestUrl = req.getUrl().toString();
        if (requestUrl.contains("login")) {
            Logx.d("webView   拦截打开淘宝登录页面1\n" + requestUrl);
            //interceptUrl = true;
        }
        //https://login.taobao.com
        if (requestUrl.contains("login.taobao.com") || requestUrl.contains("login.m.taobao.com")) {
            Logx.d("webView   拦截打开淘宝登录页面2\n" + requestUrl);
            interceptUrl = true;
        }
        return interceptUrl;
    }


}

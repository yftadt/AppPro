package test.app.ui.web;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import media.library.images.config.entity.MediaEntity;
import sj.mblog.Logx;

//只加载Html文本(文本，图片，视频)
public class WebViewHtml extends WebView {
    public WebViewHtml(@NonNull Context context) {
        super(context);
    }

    public WebViewHtml(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WebViewHtml(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WebViewHtml(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private RelativeLayout webVideoRl;

    //设置全屏videoRl
    public void setVideoAllRl(RelativeLayout videoRl) {
        this.webVideoRl = videoRl;
        webVideoRl.setVisibility(View.GONE);
    }
    //测试html
    private String testHtml="<p><span style=\"font-family: Roboto, Roboto; font-weight:400; font-size:13px; color:#181818; overflow-wrap: break-word; white-space: normal;\"><span style=\"font-family: Roboto, Roboto; font-weight:400; font-size:13px; color:#181818; overflow-wrap: break-word; white-space: normal;\">hello world~</span></span></p><p><span style=\"font-family: Roboto, Roboto; font-weight:400; font-size:13px; color:#181818; overflow-wrap: break-word; white-space: normal;\"><span style=\"font-family: Roboto, Roboto; font-weight:400; font-size:13px; color:#181818; overflow-wrap: break-word; white-space: normal;\">HAhaha</span></span></p><p><br></p><p><img src=\"https://nbc.vtnbo.com/nbc/msg/image/dev/17781191335145985.jpg\" alt=\"\" data-href=\"https://nbc.vtnbo.com/nbc/msg/image/dev/17781191335145985.jpg\"></p><p><br></p><div data-w-e-type=\"video\" data-w-e-is-void=\"\">\n<video poster=\"https://nbc.vtnbo.com/nbc/msg/image/beta/17787593818804188.jpg\" controls=\"true\" width=\"auto\" height=\"auto\"><source src=\"https://nbc.vtnbo.com/nbc/msg/video/beta/17787593835725604.mp4\" type=\"video/mp4\"></video>\n</div><p><br></p>";
    private String htmlText;

    //设置文本
    public void setHtml(Context content, String htmlText) {
        if (TextUtils.isEmpty(htmlText)) {
            return;
        }
        this.htmlText = htmlText;
        setWebViewSetting(this);
        loadDataWithBaseURL(null, getHtmlData(htmlText), "text/html", "utf-8", null);
        addJavascriptInterface(new WebAppInterface(content), "android");
    }


    private boolean init;

    private void setWebViewSetting(WebView webView) {
        if (init) {
            return;
        }
        init = true;
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        //
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //设置显示图片
        settings.setBlockNetworkImage(false);
        //支持显示https 网址图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //显示h5网页
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("UTF -8");

        //
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLoadWithOverviewMode(true);
        //settings.setTextZoom(100);
        settings.setSupportZoom(false); // 支持缩放
        webView.setWebChromeClient(new WebClient());
        webView.setWebViewClient(new WebViewCustomClient());

    }


    class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        private ImgRes imgRes;

        //抓取到的图片列表
        private ImgRes getImgRes(String imgUrl) {
            ImgRes tempImgRes = new ImgRes();
            String content = htmlText;
            ArrayList<String> urls = ImageUrlExtract.extractImageUrls(content);
            for (int i = 0; i < urls.size(); i++) {
                String url = urls.get(i);
                MediaEntity bean = new MediaEntity();
                bean.url = url;
                bean.type = 1;
                if (imgUrl.equals(url)) {
                    tempImgRes.index = i;
                }
                tempImgRes.setMediaEntity(bean);
            }
            return tempImgRes;
        }

        @JavascriptInterface
        public void imageClicked(String imgUrl) {
            if (imgRes == null) {
                imgRes = getImgRes(imgUrl);
            } else {
                //标注当前选中的图片
                ArrayList<MediaEntity> datas = imgRes.datas;
                for (int i = 0; i < datas.size(); i++) {
                    MediaEntity bean = datas.get(i);
                    if (imgUrl.equals(bean.url)) {
                        imgRes.index = i;
                        break;
                    }
                }
            }


        }
    }

    //添加头部，设置样式
    private String getHtmlData(String html) {
        html = setImgClick(html);
        html = setVideo(html);
        //<style>
        //    body {
        //        background-color: transparent !important; /* 设置为透明 */
        //        background-image: none !important; /* 移除背景图片 */
        //    }
        //</style>
        String head = ("<head>" + "<meta name=viewport content=width=device-width,initial-scale=1.0,"
                + " maximum-scale=1.0,minimum-scale=1.0 user-scalable=no />"
                + "<style>"
                //+ "img{border-radius:4px!important;max-width: 100%; width:auto; height:auto;}"
                //+ "video{border-radius:4px!important;max-width: 100%; width:auto; height:auto;}"
                //+ "body{padding: 0;margin: 0;}"
                + "body, img, iframe, video,p, h1, h2, h3, h4, h5, h6, span, div"
                + " {border-radius:4px!important;max-width: 100% !important;height: auto !important;"
                + "font-family: Roboto, Roboto; font-weight:400; font-size:13px; color:#181818;" +
                "background-color:transparent!important;}"
                + "</style>" + "</head>");

        return "<html>" + head + "<body>" + html + "</body></html>";
    }

    //设置点击事件
    private String setImgClick(String html) {
        Pattern pattern = Pattern.compile("<img src=");
        Matcher matcher = pattern.matcher(html);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            // 将找到的数字替换为*号
            matcher.appendReplacement(sb, "<img onclick=\"android.imageClicked(this.src)\" src=");
        }
        matcher.appendTail(sb); // 将剩余的文本追加到sb中
        html = sb.toString();
        Logx.d("==>图片" + html);
        //<img src="image_url" onclick="android.imageClicked(this.src)">
        return html;
    }


    //设置播放器
    private String setVideo(String html) {
        boolean isVideo = html.contains("<video ") && html.contains("</video>");
        if (!isVideo) {
            return html;
        }
        Pattern pattern = Pattern.compile("<video ");
        Matcher matcher = pattern.matcher(html);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            // 将找到的数字替换为*号
            matcher.appendReplacement(sb, "<video controlsList=\"nodownload noremoteplayback noplaybackrate disablePictureInPicture\"");
            //matcher.appendReplacement(sb, "<video controlsList=\"nodownload nofullscreen \"");
            //matcher.appendReplacement(sb, "<video controlsList=\"nodownload \"");
        }
        matcher.appendTail(sb); // 将剩余的文本追加到sb中
        html = sb.toString();
        Logx.d("==>视频" + html);
        //<img src="image_url" onclick="android.imageClicked(this.src)">
        return html;
    }

    private class WebClient extends WebChromeClient {
        // 全屏的时候调用
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            /*new Handler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onCustomViewHidden();
                }
            });*/
            setVideoAll(1, view, callback);

        }

        // 退出全屏
        @Override
        public void onHideCustomView() {
            setVideoAll(2, null, null);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Logx.d("视频地址：" + consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }
    }

    private class WebViewCustomClient extends WebViewClient {
        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (url.contains(".mp4") || url.contains(".m3u8") || url.contains(".avi") || url.contains(".mov") || url.contains(".mkv") || url.contains(".flv") || url.contains(".f4v") || url.contains(".rmvb")) {
                Logx.d("视频地址", url);

            }
            return super.shouldInterceptRequest(view, url);

        }
    }

    private WebChromeClient.CustomViewCallback webCallback;
    //1 全屏 2 非全屏
    private int videoAllType = 0;

    private void setVideoAll(int type, View view, WebChromeClient.CustomViewCallback callback) {
        switch (type) {
            case 1:
                //全屏
                if (view == null) {
                    callback.onCustomViewHidden();
                    webCallback = null;
                    videoAllType = 2;
                    return;
                }
                webCallback = callback;
                ViewParent parents = view.getParent();
                if (parents != null && (parents instanceof ViewGroup)) {
                    ((ViewGroup) parents).removeView(view);
                }
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                webVideoRl.addView(view, params);
                webVideoRl.setVisibility(View.VISIBLE);
                videoAllType = 1;
                break;
            case 2:
                //退出全屏
                videoAllType = 2;
                webCallback = null;
                webVideoRl.setVisibility(View.GONE);
                break;
        }
    }

    //true 退出全屏
    public boolean setVideoAllBack() {
        if (webCallback == null) {
            return false;
        }
        if (videoAllType == 1) {
            webCallback.onCustomViewHidden();
            webVideoRl.setVisibility(View.GONE);
            return true;
        }
        return false;
    }
}

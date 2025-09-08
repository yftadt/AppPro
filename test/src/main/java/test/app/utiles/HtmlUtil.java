package test.app.utiles;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.library.baseui.utile.app.ActivityUtile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sj.mblog.Logx;

class HtmlUtil {
    //设置颜色
    //  postDetailsBean?.content = "<a href='https://www.baidu.com'  style='text-decoration: none;'>" +
    //                "<font color=#ff00ff>访问百度网站</font></a>" +
    //                "<p style='color:#ff00ff;'>这是一个红色的段落。</p>" +
    //                "<font color=#ff00ff>这是一个红色的段落2。</font>"
    private String test = "<p>1213132<img src=\"https://nbc.vtnbo.com/nbc/msg/image/beta/17490304322134042.png\" alt=\"\" data-href=\"https://nbc.vtnbo.com/nbc/msg/image/beta/17490304322134042.png\" />12121</p>";

    // 添加头部，设置样式等，可省略
    public static String getHtmlData(String html) {
        //html = test;
        html = setImgClick(html);
        String head = ("<head>"
                + "<meta name=viewport content=width=device-width,initial-scale=1.0," +
                " maximum-scale=1.0,minimum-scale=1.0 user-scalable=no />"
                + "<style>"
                //+ "img{border-radius:4px!important;max-width: 100%; width:auto; height:auto;}"
                //+ "video{border-radius:4px!important;max-width: 100%; width:auto; height:auto;}"
                //+ "body{padding: 0;margin: 0;}"
                + "body, img, iframe, video,p, h1, h2, h3, h4, h5, h6, span, div" +
                " {border-radius:4px!important;max-width: 100% !important;height: auto !important;}"
                + "</style>"
                + "</head>");

        return "<html>" + head + "<body>" + html + "</body></html>";
    }

    //设置点击事件
    public static String setImgClick(String html) {
        Pattern pattern = Pattern.compile("<img src=");
        Matcher matcher = pattern.matcher(html);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            // 将找到的数字替换为*号
            matcher.appendReplacement(sb, "<img onclick=\"android.imageClicked(this.src)\" src=");
        }
        matcher.appendTail(sb); // 将剩余的文本追加到sb中
        html = sb.toString();
        return html;
    }

    //点击事件
    static class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void imageClicked(String imgUrl) {

            String url = imgUrl;

        }
    }

    public static void setLoadHtml(Context context, WebView webView, String html) {
        webView.loadDataWithBaseURL(null, getHtmlData(html),
                "text/html", "utf-8", null);
        webView.addJavascriptInterface(new WebAppInterface(context), "android");
    }
}

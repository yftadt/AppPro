package test.app.ui.activity.test;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;

import com.library.baseui.utile.app.ActivityUtile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import sj.mblog.Logx;
import test.app.open.OpenAppManager;
import test.app.ui.activity.MainActivity;
import test.app.ui.activity.R;
import test.app.ui.activity.WebView2Activity;
import test.app.ui.activity.WebViewActivity;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.activity.databinding.ActivityTestAppOpenBinding;

import com.library.baseui.utile.file.FileUtile;
import com.library.baseui.utile.toast.ToastUtile;

//打开第三方应用

public class OpenAppActivity extends NormalActionBar {


    private ActivityTestAppOpenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestAppOpenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "打开第三方app");


        findViewById(R.id.web_btn).setOnClickListener(this);
        findViewById(R.id.open_btn).setOnClickListener(this);
        findViewById(R.id.open_tkl_btn).setOnClickListener(this);
        findViewById(R.id.open_pdd_btn).setOnClickListener(this);
        findViewById(R.id.open_id_btn).setOnClickListener(this);
        findViewById(R.id.open_ks_btn).setOnClickListener(this);
        findViewById(R.id.open_dy_btn).setOnClickListener(this);
        findViewById(R.id.nbc_btn).setOnClickListener(this);
        findViewById(R.id.nbc_btn_open).setOnClickListener(this);
        findViewById(R.id.web_btn2).setOnClickListener(this);
        findViewById(R.id.write_private_file).setOnClickListener(this);
        findViewById(R.id.open_private_file).setOnClickListener(this);

        testDoc();
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent();
        int id = v.getId();
        if (id == R.id.web_btn) {
            ActivityUtile.startActivityCommon(WebView2Activity.class);
        }

        String type = "";
        if (id == R.id.open_btn) {
            type = "open_btn";
        }
        if (id == R.id.open_tkl_btn) {
            type = "open_tkl_btn";
        }
        if (id == R.id.open_pdd_btn) {
            type = "open_pdd_btn";
        }
        if (id == R.id.open_id_btn) {
            type = "open_id_btn";
        }
        if (id == R.id.open_ks_btn) {
            type = "open_ks_btn";
        }
        if (id == R.id.open_dy_btn) {
            type = "open_dy_btn";
        }
        if (id == R.id.nbc_btn) {
            type = "nbc_btn";
        }
        if (id == R.id.nbc_btn_open) {
            type = "nbc_btn_open";
        }
        if (id == R.id.web_btn2) {
            type = "nbc_btn_opem";
        }
        if (id == R.id.web_btn2) {
            type = "web_btn2";
        }
        if (id == R.id.write_private_file) {
            type = "write_private_file";
        }
        if (id == R.id.open_private_file) {
            type = "open_private_file";
        }

        switch (type) {
            case "web_btn2":
                //打开网页
                //打开本地html
                String u7 = "file:///android_asset/jump.html";
                ActivityUtile.startActivityString(WebViewActivity.class, "测试", u7, "");
                break;
            case "nbc_btn":
                // 调用：弹出系统保存选择器，默认文件名 newfile.txt
                String filePath = "nbc.html";
                mCreateDocLauncher.launch(filePath);
                break;
            case "nbc_btn_open":
                //只允许选 文本文件 / 所有文件
                //mOpenDocLauncher.launch(new String[]{"text/plain", "*/*"});
                mimeType = "html/plain";
                mOpenDocLauncher.launch(new String[]{mimeType, "*/*"});
                break;
            case "open_btn":
                //打开淘宝商品
                String url = "https://uland.taobao.com/coupon/edetail?e=9nO3TO85rxClhHvvyUNXZfh8CuWt5YH5OVuOuRD5gLJMmdsrkidbOWBzzpT26idJAsxzDOnNI6u91Rs23934%2BR3IIVemz1lo%2BxM8Ws8c3mAEBParLnxZKgF%2BCrgLwCaEk3toOSlQXmUUcLJJYP0Uq7Kr%2FrPaBPPmT%2F1mTtwPHxT0fJdlTnMtJ7zyLDEH%2BRdaeOx3QABhn03ZWB8yRYykDoTdig4t62Y8b%2FQFo4AOnZegJbXyzB1Cwsi60bBTauYG0g9sJZIZMaKyq%2F6z2gTz5rcZelJt%2Bzjy3e0yoyBTEcvSbhe62Zg%2BOSF%2BB1UWTwNemDCSbDT2I2q5ydwAC3myF1Vusqqnhify&traceId=21050db617261043614415775e1eca&relationId=3111129563&&union_lens=lensId:TAPI@1726104361@216698cc_1225_191e3d65a3b_a46d@01&activityId=0e2e1d44593d4c3298b5849ee4ad7788";
                //淘宝活动
                url = "https://s.click.taobao.com/t?union_lens=lensId%3AMAPI%401726109051%4021084f6d_1426_191e41deb01_7766%4001%40eyJmbG9vcklkIjo4ODYzOH0ie%3BeventPageId%3A20150318020016122&e=m%3D2%26s%3DcobKq190EBNw4vFB6t2Z2iperVdZeJviU%2F9%2F0taeK29yINtkUhsv0BZZsUZROQ4DuMnR%2Bo32kurLePpVa6ChWFPukb9BYFwUwW1KZDrTzIK%2FYp4T7sV6mfYuc71rx11iAGIx0oe2X2hZfJ7ZQxC1%2Fb%2BmvmXqUq2iEBnEBk3xaGylLmcSHKfaX7mF0vI1ZsIL81t6POd4ujeDUcZf5l6E27qdmmTA%2BxZw9ie9yII59KjO54LQ%2FVw1L%2B3qDFf3G11Zj5QwTervQtmHWWrZt0BFT261pPB3rRJ3qnEMuznJbCUKTfW%2BjcWf2gKMM%2B8LpG6sTdrdtBL5HigCvZuBJPx5YLf2anhEob7h2o96oyOcLw9nHisTXG%2FPFJXdKCvVsmnwFkdMrR0JdyLkw1nwChX752ZeChxABkvxz6GXxc3tQdeIfnrg1PVyhr%2FVrGlEZXJuF1IWp%2BDTE8%2B0NAR5Gh2I8ww9zdxU5aWKK%2F%2BZ27JO57%2F2VAhKYa2CXONz2L0RiKXtH95eV4i%2BDlZa55fucyyW0XnWwnbNEyQPG%2FzIQH4iAgqTZv4MXcoP0wGOkq3GdzR17cn%2BWPnHzd%2F0QYPr7Dt2TtphjAusosNfYmEJmtdoy7zkE2ereXiuWdxS2t6QltALIYULNg46oBA%3D";
                url = "https://mo.m.taobao.com/union2/page_1686731494574?es=j8aom27XrMJD3FSiAPfS1BsAGBd41Dm%2BDN%2FqEx0yQ3D9b04v9JvciXiuqNCguS1gUonghCcNbZYNn%2Fv%2B9%2Fhdwsi3moJpcsjqI92%2Bf4b1d3AzfFZFKWXNdA%3D%3D&tkSid=1726103029652_560410185_0.0&ali_trackid=2:mm_6780106115_3118100326_115751550078:1726110026465_554219191_0&rid=3120665331&union_lens=lensId:MAPI@1726109051@21084f6d_1426_191e41deb01_7766@01@eyJmbG9vcklkIjo4ODYzOH0ie;eventPageId:20150318020016122;recoveryid:554219191_0@1726110026475&relationId=3120665331&ak=34787809&pageId=20150318020016122&rootPageId=20150318020016122&xMktId=ORy0pmwczt6qOvveR4tMjC4UX-mzQzaBohkt5aK83d95AaFrUJ6&bxsign=tbkf2MIc_0cco4Yj6Vms1IH0JOED--2aOHc88SixNY6GpafmQ9BYuFOs1h2r2sgGGmf8Ysj5FIjEnhJishxcTLdsWQj_7XOAFXmXDTrRqjyLStqveRYtCN5_tk5s9d5hte8IY5iutYSppxnESRB8szYbHBXRPloNi5Vl6e-mV4wSM9f6vuELk__4_8j-OhMSAYy";
                //url = "https://s.click.taobao.com/jIt24Vt";
                //无效
                url = "https://s.click.taobao.com/t?e=m%3D2%26s%3D8OC7b5sG4jxw4vFB6t2Z2ueEDrYVVa64yK8Cckff7TVRAdhuF14FMQICNenGZx%2BQ1aH1Hk3GeOhFdnFNAwbfM%2FNRfS0VT8wsrB7OvHPOL58XW%2BEakCPBJrhxqfEofdtgOlGkMkr%2BSUUYmUGuVZn0uSqjeu1WxkN2pr8hwbwskT0LZMqoQW%2BfuKGzo1lVxIioc2ZTU26sKYTKmSsbd%2BQZMjIZFranY3A5%2FaT7G0L3ZuxiYWg%2FYYx0V1Y5HwkexEbclbRMz6GTKAYZjQKfJ2UQf03O4wtQ3K90XXhKrcBJdMxTV8LV9bVBtCiz72f28oCKX0%2B3obhRTo40HEcfBN0xrz7WWXJ0wFOsXSZnxp3KPzzGDmntuH4VtA%3D%3D&union_lens=lensId:TAPI@1726113636@213dcdf8_12a2_191e463e11d_3dbc@01";
                OpenAppManager.getInstance().openApp(this, "OPEN_TBHD", url);
                return;
            case "open_tkl_btn":
                //使用淘口令打开淘宝
                url = "￥5cZS3hpPwZz￥";
                copyText(url);
                OpenAppManager.getInstance().openAppKL(this, "OPEN_TBHD", url);
                return;
            case "open_pdd_btn":
                //拼多多 会场
                url = "https://mobile.yangkeduo.com/promotion_op.html?type=20&id=199610&group_index=0&pid=41186513_292060576&_x_ddjb_dmn=%7B%22cpsSign%22%3A%22CM_240912_41186513_292060576_e200ffa5ed23a2e1ffd8ec41e098fd2f%22%2C%22id%22%3A%22199610%22%2C%22type%22%3A%2220%22%7D&customParameters=%7B%22uid%22%3A%22242%22%2C%22sid%22%3A%22V0haGsyjyb%22%7D&cpsSign=CM_240912_41186513_292060576_e200ffa5ed23a2e1ffd8ec41e098fd2f&_x_ddjb_act=%7B%22st%22%3A%223%22%7D&duoduo_type=2";
                //pinduoduo://com.xunmeng.pinduoduo/promotion_op.html?type=20&id=199610&group_index=0&pid=41186513_292060576&_x_ddjb_dmn=%7B%22cpsSign%22%3A%22CM_240912_41186513_292060576_e200ffa5ed23a2e1ffd8ec41e098fd2f%22%2C%22id%22%3A%22199610%22%2C%22type%22%3A%2220%22%7D&customParameters=%7B%22uid%22%3A%22242%22%2C%22sid%22%3A%22V0haGsyjyb%22%7D&cpsSign=CM_240912_41186513_292060576_e200ffa5ed23a2e1ffd8ec41e098fd2f&_x_ddjb_act=%7B%22st%22%3A%223%22%7D&duoduo_type=2
                url = "https://mobile.yangkeduo.com/duo_transfer_channel.html?resourceType=39998&pid=41186513_293435769&_pdd_fs=1&_pdd_tc=ffffff&_pdd_sbs=1&redirect=https%3A%2F%2Fmobile.yangkeduo.com%2Fbrand_activity_subsidy.html%3F_pdd_fs%3D1%26_pdd_tc%3Dffffff%26_pdd_sbs%3D1%26access_from%3Dheishenhua%26_tab_id%3D154237&customParameters=%7B%22uid%22%3A%221000023%22%2C%22sid%22%3A%227T2kf-ezBr%22%7D&cpsSign=CE_240924_41186513_293435769_69d6f7af76a53343df98358c3a827d72&_x_ddjb_act=%7B%22st%22%3A%226%22%7D&duoduo_type=2";
                //pinduoduo://com.xunmeng.pinduoduo/duo_transfer_channel.html?resourceType=39998&pid=41186513_293435769&_pdd_fs=1&_pdd_tc=ffffff&_pdd_sbs=1&redirect=https%3A%2F%2Fmobile.yangkeduo.com%2Fbrand_activity_subsidy.html%3F_pdd_fs%3D1%26_pdd_tc%3Dffffff%26_pdd_sbs%3D1%26access_from%3Dheishenhua%26_tab_id%3D154237&customParameters=%7B%22uid%22%3A%221000023%22%2C%22sid%22%3A%227T2kf-ezBr%22%7D&cpsSign=CE_240924_41186513_293435769_69d6f7af76a53343df98358c3a827d72&_x_ddjb_act=%7B%22st%22%3A%226%22%7D&duoduo_type=2
                //拼多多频道推广
                url = "https://mobile.yangkeduo.com/duo_today_burst.html?pid=41186513_292060576&customParameters=%7B%22uid%22%3A%22242%22%2C%22sid%22%3A%22vOhkoRKDYW%22%7D&cpsSign=CM_241022_41186513_292060576_13149601146592b4e1dbbd96c126d6cc&_x_ddjb_act=%7B%22st%22%3A%223%22%7D&duoduo_type=2&launch_pdd=1&campaign=ddjb&cid=launch_";
                //pinduoduo://com.xunmeng.pinduoduo/duo_today_burst.html?pid=41186513_292060576&customParameters=%7B%22uid%22%3A%22242%22%2C%22sid%22%3A%22vOhkoRKDYW%22%7D&cpsSign=CM_241022_41186513_292060576_13149601146592b4e1dbbd96c126d6cc&_x_ddjb_act=%7B%22st%22%3A%223%22%7D&duoduo_type=2&launch_pdd=1&campaign=ddjb&cid=launch_
                //商品
                url = "https://mobile.yangkeduo.com/duo_coupon_landing.html?goods_id=666267849840&pid=41186513_292060576&display_mod=101&goods_sign=E9L2OUrmauRgyyPxwvfeGh_5QV_Jugee_JQT1ObFUYL&customParameters={\"uid\":\"1123\",\"sid\":\"xa7hoAuHRS\"}&authDuoId=10916965&cpsSign=CC_241213_41186513_292060576_3a27d6074136c957b05a3261507f44a8&mall_collect_coupon=1&_x_ddjb_act={\"st\":\"1\"}&duoduo_type=2&launch_pdd=1&campaign=ddjb&cid=launch_dl_force_";
                //商品解码后的
                url = "https://mobile.yangkeduo.com/duo_coupon_landing.html?goods_id=666267849840&pid=41186513_292060576&display_mod=101&goods_sign=E9L2OUrmauRgyyPxwvfeGh_5QV_Jugee_JQT1ObFUYL&customParameters=%7B%22uid%22%3A%221123%22%2C%22sid%22%3A%22xa7hoAuHRS%22%7D&authDuoId=10916965&cpsSign=CC_241213_41186513_292060576_3a27d6074136c957b05a3261507f44a8&mall_collect_coupon=1&_x_ddjb_act=%7B%22st%22%3A%221%22%7D&duoduo_type=2&launch_pdd=1&campaign=ddjb&cid=launch_dl_force_";
                //
                // url = "https://mobile.yangkeduo.com/duo_coupon_landing.html?goods_id=527577237911&pid=41186513_292060576&display_mod=101&goods_sign=E9f22LxDNwNgyyPxwvfeGgdrxPOfB1NB_JGZwe1M6c&customParameters=" + urlJM("%7B%22uid%22%3A%22245%22%2C%22sid%22%3A%22OIkpb6II3M%22%7D") + "&authDuoId=10916965&cpsSign=" + urlJM("CC_241213_41186513_292060576_627165e10fb9f5aa925953d4cf03f04c") + "&mall_collect_coupon=1&_x_ddjb_act=" + urlJM("%7B%22st%22%3A%221%22%7D") + "&duoduo_type=2&launch_pdd=1&campaign=ddjb&cid=launch_dl_force_";
                OpenAppManager.getInstance().openApp(this, "OPEN_PDDPT_TEST", url);
                return;
            case "open_id_btn":
                //打开京东
                url = "https://u.jd.com/uaF1Jqc";
                OpenAppManager.getInstance().openApp(this, "OPEN_JDHD", url);
                return;
            case "open_ks_btn":
                //打开快手
                url = "https://30cza.xmmicvfxi.com/f/X-9WrHwXcv8o61xc";
                url = "https://30cza.xmmicvfxi.com/web/kwaishop-goods-detail-page-app?cc=share_copylink&kpf=IPHONE&fid=3374198015&shareMode=app&shareMethod=token&tokenLogType=diversitySeparatorLongToken&kpn=KUAISHOU&subBiz=KS_FENXIAO&shareId=18221765270384&shareToken=X-9oC9t7TGSHI2oI&shareObjectId=22903768559491&id=22903768559491";
                url = "kwai://webview?fid=3374198015&cc=share_copylink&followRefer=151&shareMethod=TOKEN&kpn=KUAISHOU&subBiz=KS_FENXIAO&shareId=18221703056665&url=https%3A%2F%2Fapp.kwaixiaodian.com%2Fweb%2Fkwaishop-goods-detail-page-app%3Fid%3D23509523331900%26carrierType%3D601&shareMode=APP&originShareId=18221703056665&shareObjectId=23509523331900&shareUrlOpened=0&timestamp=1735617077590&hyId=kwaishopGoodsDetailPageApp&openFrom=%7B%22fid%22%3A%223374198015%22%2C%22cc%22%3A%22share_copylink%22%2C%22shareMethod%22%3A%22TOKEN%22%2C%22kpn%22%3A%22KUAISHOU%22%2C%22subBiz%22%3A%22KS_FENXIAO%22%2C%22channel%22%3A%22share%22%2C%22shareToken%22%3A%22X-4tRWLbhqNRY2iV%22%2C%22shareUrlOpened%22%3A%220%22%2C%22url%22%3A%22%2F%3Ffid%3D3374198015%26cc%3Dshare_copylink%26shareMethod%3DTOKEN%26kpn%3DKUAISHOU%26subBiz%3DKS_FENXIAO%26channel%3Dshare%26shareToken%3DX-4tRWLbhqNRY2iV%26shareUrlOpened%3D0%22%7D";
                //url="kwai://webview?fid=3374198015&cc=share_copylink&followRefer=151&shareMethod=TOKEN&kpn=KUAISHOU&subBiz=KS_FENXIAO&shareId=18221735902529&url=https%3A%2F%2Fapp.kwaixiaodian.com%2Fweb%2Fkwaishop-goods-detail-page-app%3Fid%3D22500655053161%26carrierType%3D601&shareMode=APP&originShareId=18221735902529&shareObjectId=22500655053161&shareUrlOpened=0&timestamp=1735617385330&hyId=kwaishopGoodsDetailPageApp&openFrom=%7B%22fid%22%3A%223374198015%22%2C%22cc%22%3A%22share_copylink%22%2C%22shareMethod%22%3A%22TOKEN%22%2C%22kpn%22%3A%22KUAISHOU%22%2C%22subBiz%22%3A%22KS_FENXIAO%22%2C%22channel%22%3A%22share%22%2C%22shareToken%22%3A%22Xa7d5ffwSQIb1J0%22%2C%22shareUrlOpened%22%3A%220%22%2C%22url%22%3A%22%2F%3Ffid%3D3374198015%26cc%3Dshare_copylink%26shareMethod%3DTOKEN%26kpn%3DKUAISHOU%26subBiz%3DKS_FENXIAO%26channel%3Dshare%26shareToken%3DXa7d5ffwSQIb1J0%26shareUrlOpened%3D0%22%7D";
                OpenAppManager.getInstance().openApp(this, "OPEN_KS", url);
                return;
            case "open_dy_btn":
                //打开抖音
                url = "snssdk1128://poi/goodsdetail?back_page=0&activity_id=1797193016931344&common_extra=%7B%22poi_distribution_info%22%3A%7B%22pid%22%3A%22dy_2421566367335466_174078_5466194%22%2C%22external_info%22%3A%22api_396124_8726686%22%2C%22product_id%22%3A1797193016931344%2C%22sign%22%3A%22tx7bpA39qZi0zUzDEpBkg0QqNuV5M5AaFopHfgsH_zc%3D%22%2C%22source%22%3A1%2C%22by_track%22%3A0%7D%7D&sale_channel=distribution.default.default&detail_enter_page=deeplink";
                OpenAppManager.getInstance().openApp(this, "OPEN_DY", url);
                return;
            case "write_private_file":
                //写入私有文件
                testNBC();
                break;
            case "open_private_file":
                //打开私有文件
                File htmlFile = new File(getFilesDir(), "nbc.html");
                if (htmlFile.exists()) {
                    ActivityUtile.openHtmlInBrowser(this, htmlFile);
                }
                break;

        }


    }

    // 注册 文件创建 回调
    private ActivityResultLauncher<String> mCreateDocLauncher;
    private ActivityResultLauncher<String[]> mOpenDocLauncher;

    private String mimeType = "";

    private void testDoc() {
        // 初始化回调
        mCreateDocLauncher = registerForActivityResult(
                new ActivityResultContracts.CreateDocument("*/*"),
                uri -> {
                    // 用户选好保存路径 & 文件名后回调
                    if (uri != null) {
                        //"content://com.android.providers.downloads.documents/document/163"
                        // 往文件写入文本内容
                        String html = "<a href=\"nbcopen://m.nbc.com?page=login\">打开</a>";
                        // 创建HTML字符串
                        String htmlContent = "<html><body>" + html + "</body></html>";
                        String saveContent = htmlContent;
                        writeFileToUri(OpenAppActivity.this, uri, saveContent);
                        ToastUtile.showToast("文件保存成功");

                    }
                });
        // 读取文件选择器：只选 txt 所有文本文件
        mOpenDocLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        // 读取 Uri 里的文件内容
                        String text = readTextFromUri(this, uri);
                        // text 就是你保存时写入的内容
                        ToastUtile.showToast("读取成功");
                        //
                        Logx.d("内容：==>" + text);
                        ActivityUtile.openFileByOtherApp(OpenAppActivity.this, uri, mimeType);
                    }
                });
    }


    /**
     * 通过 Uri 写入文件内容
     */
    private void writeFileToUri(Context context, Uri uri, String content) {
        OutputStream outputStream = null;
        try {
            // 获得输出流
            outputStream = context.getContentResolver().openOutputStream(uri);
            if (outputStream != null) {
                outputStream.write(content.getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {

            ToastUtile.showToast("保存失败");
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 从 Uri 读取文本文件
    private String readTextFromUri(Context context, Uri uri) {
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    //跳转至nbc 把html 代码片段写入sd卡，再用浏览器打开
    private void testNBC() {
        File htmlFile = new File(getFilesDir(), "nbc.html");

        String html = "<a href=\"nbcopen://m.nbc.com?page=login\">打开46</a>";
        // 创建HTML字符串
        String htmlContent = "<html><body>" + html + "</body></html>";
        try {
            FileOutputStream fos = new FileOutputStream(htmlFile);
            fos.write(htmlContent.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            ToastUtile.showToast("写入失败");
        }


    }

    //跳转至小红书 下载页面
    private void testXHS() {
        String filePath = FileUtile.getTextFile("xhs.html");
        File htmlFile = new File(filePath);
        if (!htmlFile.exists()) {
            String html = "<a rel=\"follow\" href=\"market://details?id=com.xingin.xhs\">打开APP</a>";
            // 创建HTML字符串
            String htmlContent = "<html><body>" + html + "</body></html>";
            FileUtile.writeTxet(htmlContent, "xhs.html");
            return;
        }
        if (!htmlFile.exists()) {
            ToastUtile.showToast("文件不存在");
            return;
        }
        ActivityUtile.openFile(this, htmlFile);
    }


    //解码
    private String urlJM(String str) {
        String decodedString = str;
        try {
            decodedString = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logx.d("解码错误：" + e.getMessage());
        }
        return decodedString;
    }

    //编码
    private String urlBM(String str) {
        String decodedString = str;
        try {
            decodedString = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logx.d("编码错误：" + e.getMessage());
        }
        return decodedString;
    }

    //复制到剪贴版
    public void copyText(final CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText("text", text));
        }
    }
}

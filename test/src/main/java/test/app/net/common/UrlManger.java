package test.app.net.common;

import android.content.Context;
import android.text.TextUtils;


import com.library.baseui.utile.file.DataSave;
import com.retrofits.net.common.BaseUrl;

/**
 * Created by Administrator on 2017/6/14.
 */

public class UrlManger extends BaseUrl {
    public static String TEST_SERVICE_URL_APP = "http://test-api-ywfy.hztywl.cn";
    public static String SEARVICE_APP = TEST_SERVICE_URL_APP;
    private static String TOKEN = null;

    @Override
    public String getUrl() {
        return TEST_SERVICE_URL_APP;
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public boolean isSSL() {
        return false;
    }

    @Override
    public String[] getSSLCertificates() {
        //return new String[]{"djy_djbx.com.cer"};
        return null;
    }

    public static String getTOKEN() {
        if (TextUtils.isEmpty(TOKEN)) {
            TOKEN = DataSave.stringGet(DataSave.TOKEN);
        }
        if ("null".equals(TOKEN)) {
            TOKEN = null;
        }
        if ("".equals(TOKEN)) {
            TOKEN = null;
        }
        return TOKEN;
    }

    public static void setTOKEN(String token) {
        TOKEN = token;
        DataSave.stringSave(DataSave.TOKEN, token);
    }
}

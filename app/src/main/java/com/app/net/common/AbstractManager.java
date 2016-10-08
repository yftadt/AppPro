package com.app.net.common;

import android.content.Context;
import android.text.TextUtils;

import com.app.net.res.BaseResult;
import com.app.ui.dialog.DialogCustomWaiting;
import com.app.utiles.other.DLog;
import com.google.gson.Gson;

import java.lang.ref.SoftReference;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/9/7.
 */
public abstract class AbstractManager {
    private DialogCustomWaiting dialog;

    public void setDialogShow(Context contex) {
        if (dialog == null) {
            dialog = new DialogCustomWaiting(contex);
        }
        dialog.show();
    }

    public void setDialogDismiss() {
        if (dialog == null) {
            return;
        }
        dialog.dismiss();
    }

    SoftReference<RequestBack> requestBacks = null;

    public AbstractManager(RequestBack requestBack) {
        if (requestBack == null) {
            return;
        }
        requestBacks = new SoftReference<RequestBack>(requestBack);
    }

    private RequestBack getRequestBack() {
        if (requestBacks == null) {
            return null;
        }
        RequestBack requestBack = requestBacks.get();
        return requestBack;
    }

    public void onBack(int code, Object obj, String msg) {
        RequestBack requestBack = getRequestBack();
        if (requestBack == null) {
            return;
        }
        requestBack.OnBack(code, obj, msg, "");
    }

    public class DataManagerListener<T> implements Callback<T> {
        private Object reqObj;

        public DataManagerListener() {
        }

        public DataManagerListener(Object reqObj) {
            this.reqObj = reqObj;
        }

        //onResponse:此方法被调用 都是connectCode==200；
        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            // int connectCode = response.code();
            //
            T body = response.body();
            HttpUrl url = call.request().url();
            String path = url.url().toString();
            log(path, body, null);
            BaseResult baseResult = (BaseResult) body;
            //
            String responseMsg = baseResult.getMsg();
            String code = baseResult.getCode();
            Object obj = getObject(response);
            if ("0".equals(code) || TextUtils.isEmpty(code)) {
                //业务请求成功
                onBack(onDealSucceed(WHAT_NET_REQUEST_SUCCEED), obj, responseMsg);
                return;
            }
            //业务请求 其他原因失败
            onBack(onDealFailed(WHAT_DEAL_FAILED), obj, responseMsg);
        }
        //onResponse:此方法被调用 都是connectCode！=200；
        @Override
        public void onFailure(Call<T> call, Throwable t) {
            onBack(onDealFailed(WHAT_LOCALITY_NET_WORK_ERROR), null, t.getMessage());
            HttpUrl url = call.request().url();
            String path = url.url().toString();
            log(path, null, t.getMessage());
        }

        private void log(String url, T respBody, String error) {
            if (!DLog.DBUG) {
                return;
            }
            Gson gson = new Gson();
            //
            String resultMsg = gson.toJson(respBody);
            if (respBody == null) {
                resultMsg = error;
            }
            //
            String reqMsg = "未设置打印";
            if (reqObj != null) {
                reqMsg = gson.toJson(reqObj);
            }
            DLog.e("url", url);
            DLog.e("请求", reqMsg);
            DLog.e("返回", resultMsg);
        }

        public Object getObject(Response<T> response) {
            return response;
        }

        public int onDealSucceed(int what) {
            return what;
        }

        public int onDealFailed(int what) {
            return what;
        }

    }

    /**
     * 访问网络成功
     */
    public static final int WHAT_NET_REQUEST_SUCCEED = 200;

    /**
     * 业务处理成功
     */
    public static final int WHAT_DEAL_SUCCEED = 300;
    /**
     * 业务操作失败
     */
    public static final int WHAT_DEAL_FAILED = 301;
    /**
     * json转换类失败
     */
    public static final int WHAT_DATA_ERROR = 302;
    /**
     * token失效或不能为空
     */
    public static final int WHAT_DATA_TOKEN_ERROR = 306;

    /**
     * 网络错误
     */
    public static final int WHAT_LOCALITY_NET_WORK_ERROR = 101;
    public static final int WHAT_OTHER = 100;
}

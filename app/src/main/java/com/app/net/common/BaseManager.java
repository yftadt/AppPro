package com.app.net.common;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.app.net.common.thread.NetSourceThreadPool;
import com.app.net.res.BaseResult;
import com.app.ui.dialog.DialogCustomWaiting;
import com.app.utiles.other.DLog;
import com.app.utiles.other.Json;

import java.io.IOException;
import java.lang.ref.SoftReference;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/10/20.
 */
public class BaseManager {
    /**
     * 访问网络成功
     */
    public static final int WHAT_NET_REQUEST_SUCCEED = 200;
    /**
     * 网络错误
     */
    public static final int WHAT_LOCALITY_NET_WORK_ERROR = 201;

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


    public static final int WHAT_OTHER = 100;
    private DialogCustomWaiting dialog;
    private HandleCall handleCall = new HandleCall();

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

    public BaseManager(RequestBack requestBack) {
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

    public void onBack(int code, Object obj, String msg, String other, boolean isExchange) {
        if (isExchange) {
            Message message = handleCall.obtainMessage();
            message.obj = obj;
            Bundle bundle = new Bundle();
            bundle.putInt("code", code);
            bundle.putString("msg", msg);
            bundle.putString("other", other);
            message.setData(bundle);
            handleCall.sendMessage(message);
            return;
        }
        RequestBack requestBack = getRequestBack();
        if (requestBack == null) {
            return;
        }
        requestBack.OnBack(code, obj, msg, other);
    }
    //

    public class DataManagerListener<T> implements Callback<T> {
        public Object reqObj;
        private String other;
        private boolean isExchange;

        public DataManagerListener() {
        }

        protected void isExchange() {
            isExchange = true;
        }

        public DataManagerListener(Object reqObj) {
            this.reqObj = reqObj;
        }

        public DataManagerListener(Object reqObj, String other) {
            this.reqObj = reqObj;
            this.other = other;
        }

        public DataManagerListener(String other) {
            this.other = other;
        }

        public void setOther(String other) {
            this.other = other;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            T body = response.body();
            HttpUrl url = call.request().url();
            String path = url.url().toString();
            log(path, body, null);
            BaseResult baseResult = (BaseResult) body;
            Object obj = null;
            String responseMsg = "";
            String code = "";
            //
            if (baseResult == null) {
                responseMsg = "没有返回数据";
                code = "-1";
            } else {
                responseMsg = baseResult.msg;
                code = baseResult.code;
                obj = getObject(response);
            }
            if ("0".equals(code) || TextUtils.isEmpty(code)) {
                //业务请求成功
                onBack(onDealSucceed(WHAT_DEAL_SUCCEED), obj, responseMsg, other, isExchange);
                return;
            }
            //业务请求 其他原因失败
            onBack(onDealFailed(WHAT_DEAL_FAILED), obj, responseMsg, other, isExchange);
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            String msg = t.toString();
            String m = msg;
            if (msg.contains("TimeoutException")) {
                //m = "请求超时";
                m = "网络出小差，请稍后重试";
            }
            if (msg.contains("Failed to connect to")) {
                m = "无法连接服务器";
            }
            if (msg.contains("No address associated with hostname")) {
                m = "网络连接失败";
            }
            if (msg.contains("JsonParseException")) {
                m = "数据解析失败";
            }
            onBack(onDealFailed(WHAT_LOCALITY_NET_WORK_ERROR), reqObj, m, other, isExchange);
            HttpUrl url = call.request().url();
            String path = url.url().toString();
            log(path, null, msg);
        }

        private void log(String url, T respBody, String error) {
            if (!DLog.DBUG) {
                return;
            }
            //
            String resultMsg = Json.obj2Json(respBody);
            if (respBody == null) {
                resultMsg = error;
            }
            //
            String reqMsg = "未设置打印";
            if (reqObj != null) {
                reqMsg = Json.obj2Json(reqObj);
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

    public class Execute<T> extends DataManagerListener<T> implements Runnable {
        private Call<T> call;

        public Execute(Call<T> call) {
            this.call = call;
            isExchange();
            NetSourceThreadPool.getInstance().execute(this);
        }

        public Execute(Call<T> call, Object reqObj) {
            this.call = call;
            this.reqObj = reqObj;
            isExchange();
            NetSourceThreadPool.getInstance().execute(this);
        }

        @Override
        public void run() {
            try {
                Response<T> response = call.execute();
                onResponse(call, response);
            } catch (IOException e) {
                e.printStackTrace();
                onFailure(call, e);
            }
        }

    }

    class HandleCall extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Object obj = msg.obj;
            Bundle bundle = msg.getData();
            String other = "";
            String hint = "";
            int code = -1;
            if (bundle != null) {
                other = bundle.getString("other");
                hint = bundle.getString("msg");
                code = bundle.getInt("code", -1);
            }
            RequestBack requestBack = getRequestBack();
            if (requestBack == null) {
                return;
            }
            requestBack.OnBack(code, obj, hint, other);
        }
    }
}

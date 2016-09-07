package com.app.net.common;

import android.text.TextUtils;

import com.app.net.res.BaseResult;

import java.lang.ref.SoftReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/9/7.
 */
public abstract class AbstractManager {
    SoftReference<RequestBack> requestBacks = null;

    public AbstractManager(RequestBack requestBack) {
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
        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            response.code();
            T body = response.body();
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

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            onBack(onDealFailed(WHAT_DEAL_FAILED), null, t.getMessage());
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
}

package com.app.net.manager.account;

import com.app.net.req.LoginBeanReq;
import com.app.net.res.ResultObject;
import com.app.net.res.SysUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


/**
 * Created by Administrator on 2016/9/7.
 */
public interface ApiAccount {

    @POST("app/")
    Call<ResultObject<SysUser>> loginIn(@Body LoginBeanReq resetPushBean);
}


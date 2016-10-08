package com.app.net.manager.account;

import com.app.net.req.LoginBeanReq;
import com.app.net.res.ResultObject;
import com.app.net.res.SysUser;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/**
 * Created by Administrator on 2016/9/7.
 */
public interface ApiAccount {

    @POST("app/")
    Call<ResultObject<SysUser>> loginIn(@Body LoginBeanReq resetPushBean);
    //上传文件
    @Multipart
    @POST("app/")
    Call<ResultObject<String>> uploading(
            @Part("service") RequestBody service,
            @Part("spid") RequestBody spid,
            @Part("oper") RequestBody oper,
            @Part("channel") RequestBody channel,
            @Part("random") RequestBody random,
            @Part("sign") RequestBody sign,
            @Part MultipartBody.Part file);
}


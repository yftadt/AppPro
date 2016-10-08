package com.app.net.manager.account;

import com.app.net.common.AbstractManager;
import com.app.net.common.NetSource;
import com.app.net.common.RequestBack;
import com.app.net.req.UploadingBeanReq;
import com.app.net.res.ResultObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;


/**
 * 上传文件
 * Created by Administrator on 2016/9/7.
 */
public class UploadingManager extends AbstractManager {


    public UploadingManager(RequestBack requestBack) {
        super(requestBack);

    }

    private File file;

    public void setData(File file) {
        this.file = file;
    }


    public void request() {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        //
        ApiAccount service = NetSource.getRetrofit().create(ApiAccount.class);
        //
        UploadingBeanReq req = new UploadingBeanReq();
        RequestBody serviceReq = RequestBody.create(null, req.service);
        RequestBody spid = RequestBody.create(null, req.spid);
        RequestBody oper = RequestBody.create(null, req.oper);
        RequestBody channel = RequestBody.create(null, req.channel);
        RequestBody random = RequestBody.create(null, req.random);
        RequestBody sign = RequestBody.create(null, req.sign);
        //
        Call<ResultObject<String>> call = service.uploading(serviceReq, spid, oper, channel, random, sign, body);
        DataManagerListener<ResultObject<String>> listener = new DataManagerListener<ResultObject<String>>() {
            @Override
            public Object getObject(Response<ResultObject<String>> response) {
                ResultObject<String> body = response.body();
                String obj = body.getObj();
                return obj;
            }
        };
        listener.setOther(file.getName());
        call.enqueue(listener);

    }

}

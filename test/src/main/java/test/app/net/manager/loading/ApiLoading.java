package test.app.net.manager.loading;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import test.app.net.res.ResultObject;
import test.app.net.res.loading.AttaRes;


/**
 * Created by Administrator on 2016/9/7.
 */
public interface ApiLoading {
    //上传文件
    @Multipart
    @POST("app/")
    Call<ResultObject<AttaRes>> uploading(
            @HeaderMap Map<String, String> map,
            @Part("service") RequestBody service,
            @Part("module") RequestBody module,
            @Part("fileType") RequestBody fileType,
            @Part("fileName") RequestBody fileName,
            @Part("spid") RequestBody spid,
            @Part("oper") RequestBody oper,
            @Part("channel") RequestBody channel,
            @Part("random") RequestBody random,
            @Part("sign") RequestBody sign,
            @Part MultipartBody.Part file);

}


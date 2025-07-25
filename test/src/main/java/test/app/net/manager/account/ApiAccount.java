package test.app.net.manager.account;



import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import test.app.net.req.LoginBeanReq;
import test.app.net.res.ResultObject;
import test.app.net.res.SysUser;


/**
 * Created by Administrator on 2016/9/7.
 */
public interface ApiAccount {

    @POST("app/")
    Call<ResultObject<SysUser>> loginIn(@Body LoginBeanReq resetPushBean);

}


package test.app.net.manager.account;


import com.retrofits.net.common.RequestBack;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import test.app.net.common.AbstractBasePageManager;
import test.app.net.common.RequestResultListener;
import test.app.net.req.LoginBeanReq;
import test.app.net.res.ResultObject;
import test.app.net.res.SysUser;

/**
 * Created by Administrator on 2016/9/7.
 */
public class MainManager extends AbstractBasePageManager {
    public MainManager(RequestBack requestBack) {
        super(requestBack);

    }

    private LoginBeanReq loginBeanReq;

    @Override
    protected void init() {
        loginBeanReq = new LoginBeanReq();
        setBaseReq(loginBeanReq);
    }



    public void setData(String account, String password) {
        loginBeanReq.userMobile = account;
        loginBeanReq.userPassword = password;
    }
    @Override
    protected void request(Retrofit retrofit, String other) {
        ApiAccount service =retrofit.create(ApiAccount.class);
        Call<ResultObject<SysUser>> call = service.loginIn(loginBeanReq);
        call.enqueue(new RequestResultListener<ResultObject<SysUser>>(this, loginBeanReq) {
            @Override
            public Object getObject(Response<ResultObject<SysUser>> response) {
                ResultObject<SysUser> body = response.body();
                SysUser obj = body.obj;
                return obj;
            }
        });
    }

}

package test.app.net.common;


import com.library.baseui.utile.app.APKInfo;
import com.library.baseui.utile.file.Md5Utile;
import com.retrofits.net.common.BaseNetSource;
import com.retrofits.net.common.RequestBack;
import com.retrofits.net.manager.BaseManager;
import com.retrofits.utiles.Json;
import com.retrofits.utiles.RLog;


import java.util.HashMap;
import java.util.Random;

import retrofit2.Retrofit;
import test.app.net.req.BaseReq;
import test.app.utiles.other.DLog;

/**
 * Created by javacoder on 2016/10/19.
 */

public abstract class AbstractBaseManager extends BaseManager {
    protected BaseReq baseReq;

    public AbstractBaseManager(RequestBack requestBack) {
        super(requestBack);
        init();
        RLog.DBUG = DLog.DBUG;
    }

    protected void setBaseReq(BaseReq baseReq) {
        this.baseReq = baseReq;
    }

    public void doRequest() {
        doRequest("");
    }

    public void doRequest(String other) {
        baseReq.setToken( UrlManger.getTOKEN());
        baseReq.setRandom(getRandom());
        String code = APKInfo.getInstance().getVersionCode();
        baseReq.setVersion(code);
        BaseNetSource source = new BaseNetSource();
        Retrofit retrofit = source.getRetrofit(new  UrlManger());
        request(retrofit, other);
    }

    //获取随机码
    public static String getRandom() {
        Random random = new Random();
        String randoms = "";
        for (int i = 0; i < 4; i++) {
            int randomInt = random.nextInt(9);
            randoms += randomInt;
        }
        return randoms;
    }

    protected HashMap getHeadMap() {
        HashMap<String, String> map = new HashMap();
        String p = Md5Utile.encode("aAr9MVS9j1");
        String sign = Md5Utile.encode(p + Json.obj2Json(baseReq));
        // map.put("sign", sign);
        map.put("sign", "test");
        DLog.e("获取sign", "sign:" + sign);
        return map;
    }

    protected abstract void init();

    protected abstract void request(Retrofit retrofit, String other);


}

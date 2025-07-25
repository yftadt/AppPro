package test.app.net.manager.loading;


import android.text.TextUtils;

import com.library.baseui.utile.file.Md5Utile;
import com.retrofits.net.common.RequestBack;
import com.retrofits.net.common.thread.NetSourceThreadPool;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import test.app.net.common.AbstractBaseManager;
import test.app.net.common.RequestResultThreadListener;
import test.app.net.req.loading.UploadingBeanReq;
import test.app.net.res.ResultObject;
import test.app.net.res.loading.AttaRes;
import test.app.utiles.other.DLog;


/**
 * 上传文件
 * Created by Administrator on 2016/9/7.
 */
public class UploadingManager extends AbstractBaseManager {
    private UploadingBeanReq req;

    public UploadingManager(RequestBack requestBack) {
        super(requestBack);

    }

    //停止任务
    public void stopTask(List<String> keys) {
        if (keys == null || keys.size() == 0) {
            return;
        }
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Future future = NetSourceThreadPool.getInstance().getTask(key);
            if (future == null) {
                continue;
            }
            future.cancel(true);
        }
    }

    public boolean getLoadingPath(String key) {
        boolean isRun = false;
        if (TextUtils.isEmpty(key)) {
            return isRun;
        }
        Future future = NetSourceThreadPool.getInstance().getTask(key);
        if (future != null && !future.isDone()) {
            isRun = true;

        }
        return isRun;
    }

    @Override
    protected void init() {
        req = new UploadingBeanReq();
        setBaseReq(req);
    }

    private File file;

    public void setDataFile(File file) {
        this.file = file;
    }

    //上传头像
    public void setDataHead() {
        req.module = "PAT";
        req.fileType = "IMAGE";
    }

    //上传预约图片
    public void setDataIll() {
        req.module = "APPOINTMENT";
        req.fileType = "IMAGE";
    }

    //上传健康档案图片
    public void setDataRecord() {
        req.module = "MEDICAL";
        req.fileType = "IMAGE";
    }

    //上传会话图片
    public void setDataChat() {
        req.module = "CONSULT";
        req.fileType = "IMAGE";
    }

    //上传会话语音
    public void setDataChatAudio() {
        req.module = "CONSULT";
        req.fileType = "AUDIO";
    }

    //上传问诊图片
    public void setDataApplyConsult() {
        req.module = "CONSULT";
        req.fileType = "IMAGE";
    }


    public static final int UPLOADING_WHAT_SUCCEED = 800;
    public static final int UPLOADING_WHAT_FAILED = 801;

    @Override
    protected void request(Retrofit retrofit, String other) {
        String name = file.getName();
        if ("IMAGE".equals(req.fileType) && !name.endsWith(".png") && !name.endsWith(".jpg")) {
            name += ".png";
        }
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", name, requestFile);
        //

        req.random = getRandom();
        //
        String serviceTest= req.service;
        DLog.e("===", "--" +serviceTest);
        RequestBody serviceReq = RequestBody.create(null, req.service);
        RequestBody module = RequestBody.create(null, req.module);
        RequestBody fileType = RequestBody.create(null, req.fileType);
        RequestBody fileName = RequestBody.create(null, name);
        //
        RequestBody spid = RequestBody.create(null, req.spid);
        RequestBody oper = RequestBody.create(null, req.oper);
        RequestBody channel = RequestBody.create(null, req.channel);
        RequestBody random = RequestBody.create(null, req.random);
        //
        HashMap<String, String> map = new HashMap();
        String p = Md5Utile.encode(req.getPwd());
        String s = Md5Utile.encode(p + req.spid + req.random);
        map.put("sign", s);
        RequestBody sign = RequestBody.create(null, s);
        //
        ApiLoading service = retrofit.create(ApiLoading.class);
        Call<ResultObject<AttaRes>> call = service.uploading(map, serviceReq, module, fileType, fileName,
                spid, oper, channel, random, sign, body);
        RequestResultThreadListener<ResultObject<AttaRes>> listener = new RequestResultThreadListener<ResultObject<AttaRes>>(this, call) {
            @Override
            public Object getObject(Response<ResultObject<AttaRes>> response) {
                ResultObject<AttaRes> body = response.body();
                AttaRes obj = body.obj;
                return obj;
            }

            @Override
            public int onDealSucceed(int what) {
                return super.onDealSucceed(UPLOADING_WHAT_SUCCEED);
            }

            @Override
            public int onDealFailed(int what, String code) {
                return super.onDealFailed(UPLOADING_WHAT_FAILED, code);
            }
        };
        String path = file.getAbsolutePath();
        String o = TextUtils.isEmpty(other) ? path : other;
        listener.setOther(o);
        listener.start(path);
    }


}

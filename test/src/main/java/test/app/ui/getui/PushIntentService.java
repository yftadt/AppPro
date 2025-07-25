package test.app.ui.getui;

import android.content.Context;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.library.baseui.activity.BaseApplication;
import com.library.baseui.utile.toast.ToastUtile;

import test.app.utiles.other.DLog;


/**
 * 进行个推接收
 * Created by Administrator on 2017/2/16.
 */

public class PushIntentService extends GTIntentService {
    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        if (application == null) {
            DLog.e("PushService", "application:null");
            return;
        }

        DLog.e("PushService", "推送id获取成功:" + clientid);
        // application.setUploadPushId(clientid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        DLog.e("PushService", "推送透传数据");
        if (gtTransmitMessage != null) {
            byte[] payload = gtTransmitMessage.getPayload();
            if (payload != null && payload.length > 0) {
                String data = new String(payload);
                DLog.e("PushService：测试-", "推送透传数据/n" + data);
                // PushVo pushVo = (PushVo) JsonT.json2Obj(data, PushVo.class);
                //InformManager.getInstance(context).setData(pushVo);
                ToastUtile.showToast("测试：" + data);
            }
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {

    }
}

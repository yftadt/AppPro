package test.app.ui.activity.test;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.activity.databinding.ActivityTestCanvasBinding;
import test.app.ui.activity.databinding.ActivityTestNoticeBinding;
import test.app.ui.notification.InformBean;
import test.app.ui.notification.InformManager;
import test.app.ui.notification.InformPermissionUtils;

//测试通知
public class TestNoticeActivity extends NormalActionBar {


    private ActivityTestNoticeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBarColor();
        initViews();
    }

    private void initViews() {
        binding.tvTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //普通通知
                test1();
            }
        });
        binding.tvTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义通知
                test2();
            }
        });
    }

    private int pt1 = 0;
    private int pt2 = 2;

    //普通通知
    private void test1() {
        if (!isNoticeOpen()) {
            return;
        }
        pt1 += 1;
        pt2 += 1;
        int id = -1000;
        InformManager informManager = InformManager.getInstance(this);
        informManager.clearNotifyId(id);
        String packageName = getPackageName();

        Intent it = new Intent();
        it.setClassName(packageName, TestStartActivity.class.getName());
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        InformBean informBean = new InformBean();
        informBean.msgTitle = "普通消息";
        informBean.msgContent = "未读消息数" + pt1;
        informBean.msgTicker = "未读消息数" + pt2;
        informBean.intent = it;
        informBean.informId = id;
        informBean.informReqCode = id;
        informBean.informAutoCancel = true;
        NotificationCompat.Builder builder = informManager.setInformContent(informBean);
        //
        //NotificationCompat.Builder builder = informManager.getBuilder();
        //Notification notification = builder.build();
        //
        informManager.show(builder, informBean.informId);
    }

    private int zdt1 = 0;
    private int zdt2 = 2;

    //自定义通知
    private void test2() {
        if (!isNoticeOpen()) {
            return;
        }
        zdt1 += 1;
        zdt2 += 1;
        int id = -1001;
        InformManager informManager = InformManager.getInstance(this);
        informManager.clearNotifyId(id);
        String packageName = getPackageName();

        Intent it = new Intent();
        it.setClassName(packageName, TestStartActivity.class.getName());
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        InformBean informBean = new InformBean();
        informBean.informType = 4;
        informBean.msgTitle = "自定义通知";
        informBean.msgContent = "自定义通知" + zdt1;
        informBean.customTicker = "自定义通知" + zdt2;
        informBean.intent = it;
        informBean.informId = id;
        informBean.informReqCode = id;
        informBean.informAutoCancel = true;
        //
        //折叠情况下
        RemoteViews views = new RemoteViews(packageName, R.layout.layout_notice_custom);
        views.setTextViewText(R.id.tv_test_1, informBean.msgTitle);
        views.setTextViewText(R.id.tv_test_2, informBean.msgContent);
        informBean.customViews = views;
        //展开情况下
        RemoteViews bigViews = new RemoteViews(packageName, R.layout.layout_notice_custom_big);
        bigViews.setTextViewText(R.id.tv_test_1, informBean.msgTitle);
        bigViews.setTextViewText(R.id.tv_test_2, informBean.msgContent);
        informBean.customBigViews = bigViews;
        informBean.customSmallIcon = R.mipmap.ic_launcher;
        NotificationCompat.Builder builder = informManager.setInformContent(informBean);
        //
        //NotificationCompat.Builder builder = informManager.getBuilder();
        //Notification notification = builder.build();
        //
        informManager.show(builder, informBean.informId);
    }

    private boolean isNoticeOpen() {
        if (!InformPermissionUtils.checkNotifyPermission(this)) {
            InformPermissionUtils.tryJumpNotifyPage(this);
            return false;
        }
        return true;
    }
}

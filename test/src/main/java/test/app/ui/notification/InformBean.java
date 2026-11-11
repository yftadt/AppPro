package test.app.ui.notification;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.RemoteViews;

//通知栏
public class InformBean {

    //渠道id：未指定时，进度通知使用下载渠道，其他通知按 feelingType 选择提醒渠道。
    public String channelId;

    //0 消息通知 1 大图 ，2 长文字 3 进度通知 4 自定义通知
    public int informType;//通知类型
    public int informId;//通知id
    //PendingIntent 请求码（以下以 Intent 的其他标识相同为前提，extras 不参与匹配）。
    //相同：使用 FLAG_UPDATE_CURRENT 时复用同一个 PendingIntent，更新 extras，旧通知点击时也使用更新后的参数。
    //不相同：区分为不同的 PendingIntent，各自的点击参数互不覆盖。
    public int informReqCode;
    //true 点击通知自动消失
    public boolean informAutoCancel = true;
    //角标数
    public int badgeCount;
    //==========消息通知=========
    public String msgTitle;
    public String msgContent;
    public String msgTicker;
    //
    public Intent intent;
    //==========大图相关=========
    //大标题
    public String bigTxtTitle;
    //大内容
    public String bigTxtMsg;
    //大图
    public Bitmap bigImg;
    //大图
    public int bigResIdImg;
    //============进度============
    public int progressMax;//最大进度
    public int progress;//当前进度
    //true 不确定 进度的最大值和 当前进度值
    public boolean progressIndeterminate;//进度是确定的
    //=============自定义===============
    //折叠布局。
    public RemoteViews customViews;
    //展开布局，可不设置。
    public RemoteViews customBigViews;
    public int customSmallIcon;//必填
    public String customTicker;
    //=============声音和震动==============
    /**
     * 未显式指定 channelId 时生效；自定义声音和震动仅在对应渠道首次创建时设置。
     * 类型
     * 1 使用默认声音 和 震动
     * 2 使用默认的震动
     * 3 使用默认的震动和声音
     * 4 使用默认的声音、振动、闪光
     * 5 自定义震动模式
     * 6 自定义声音 和 震动
     */
    public int feelingType;
    //自定义声音
    public Uri sound;
    //自定义震动 频率，new long[]{0,0} 为不震动
    public long[] pattern = {100, 500, 400, 500};
}

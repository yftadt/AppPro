package test.app.ui.notification;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.RemoteViews;

//通知栏
public class InformBean {

    //0 消息通知 1 大图 ，2 长文字 3 进度通知 4 自定义通知
    public int informType;
    public int informId;
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
    public RemoteViews customViews;
    public int customSmallIcon;
    public String customTicker;
    //=============声音和震动==============
    /**
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

package test.app.ui.activity.test;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.igexin.sdk.PushManager;
import com.library.baseui.utile.HandlerUtil;
import com.library.baseui.utile.app.ActivityUtile;
import com.library.baseui.utile.file.FileTypeUtil;
import com.library.baseui.utile.file.FileTypeUtil2;
import com.library.baseui.utile.file.FileUtile;
import com.library.baseui.utile.img.ImageLoadingUtile;
import com.library.baseui.utile.time.DateUtile;

import sj.mblog.Logx;
import test.app.ui.activity.R;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import test.app.net.socket.TcpTimeoutExample;
import test.app.ui.activity.MainActivity;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.bean.Test;
import test.app.ui.getui.PushIntentService;
import test.app.ui.getui.PushService;
import test.app.ui.view.text.ExpansionRl;
import test.app.ui.view.down.CodeEditLayout;
import test.app.utiles.WMViewMaanger;
import test.app.utiles.other.DLog;


//测试登录
public class TestStartActivity extends NormalActionBar {


    ImageView testIv;

    ImageView test2Iv;

    ImageView chatLeftIv;

    ImageView chatRightIv;
    private CodeEditLayout codeEditLayout;

    public static String head = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2233479411,2653329671&fm=200&gp=0.jpg";
    public static String docHead = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3004881345,3357642791&fm=200&gp=0.jpg";
    public static String patHead = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1713938902,2373226680&fm=200&gp=0.jpg";
    private View testView;
    //华为手机不显示图
    private String img1 = "https://nbc.vtnbo.com/nbc/msg/image/pro/1751710859071627.JPG";
    private String img2 = "https://nbc.vtnbo.com/nbc/msg/image/pro/17517100642845371.jpg";
    private String img21 = "https://nbc.vtnbo.com/nbc/msg/image/pro/17517100642845371.jpg";
    private String img3 = "https://github.com/PokeAPI/sprites/blob/master/sprites/pokemon/other/official-artwork/1.png";
    //可以显示
    private String img4 = "https://raw.githubusercontent.com/PokeAPI/sprites/refs/heads/master/sprites/pokemon/other/official-artwork/1.png";
    //webp 动图
    private String img5 = "https://nbc.vtnbo.com/nbc/msg/image/pro/17585945529726711.webp";

    String img = img5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_start);
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "开始");
        HandlerUtil.runInMainThread(new Runnable() {
            @Override
            public void run() {
                //Glide.get(TestStartActivity.this).clearDiskCache();
                //Glide.get(TestStartActivity.this).clearMemory();
            }
        });

        //

        testIv = (ImageView) findViewById(R.id.test_iv);

        test2Iv = (ImageView) findViewById(R.id.test_2_iv);

        chatLeftIv = (ImageView) findViewById(R.id.chat_left_iv);
        testView = (View) findViewById(R.id.test_view);


        testView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //视图的可见性发生变化，或者某个视图的大小发生变化 系统会触发这个监听器。
                DLog.e("隐藏：" + (testView.getVisibility() == View.VISIBLE));
            }
        });
        chatRightIv = (ImageView) findViewById(R.id.chat_right_iv);
        //清除缓存
        // ImageLoadingUtile.clear(this, 0);
        // ImageLoadingUtile.clear(this, 1);


        // ImageLoadingUtile.loadingCircularBead(this, head, R.mipmap.default_image, test2Iv);
        // ImageLoadingUtile.loadImageChat(this, patHead, R.mipmap.default_image, chatLeftIv, 0);
        // ImageLoadingUtile.loadImageChat(this, patHead, R.mipmap.default_image, chatRightIv, 1);
        //
        //
         ImageLoadingUtile.loadImageTest(this, img, R.mipmap.ic_launcher, testIv);
        testIv.setOnClickListener(this);
        //
        codeEditLayout = (CodeEditLayout) findViewById(R.id.code_edit_layout);
        //初始化推送
        PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), PushIntentService.class);
        List<Test> list = new ArrayList();

        findViewById(R.id.jtb_btn).setOnClickListener(this);
        findViewById(R.id.card_btn).setOnClickListener(this);
        findViewById(R.id.yc_btn).setOnClickListener(this);
        findViewById(R.id.xs_btn).setOnClickListener(this);
        findViewById(R.id.main_btn).setOnClickListener(this);
        findViewById(R.id.dialog_btn).setOnClickListener(this);
        findViewById(R.id.btn_3d_tv).setOnClickListener(this);
        findViewById(R.id.lang_btn).setOnClickListener(this);
        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.chat_btn).setOnClickListener(this);
        findViewById(R.id.canvas_btn).setOnClickListener(this);
        findViewById(R.id.open_app_btn).setOnClickListener(this);
        findViewById(R.id.delineate_btn).setOnClickListener(this);
        //
        DateUtile.testLog();
        TextView tv = findViewById(R.id.btn_3d_tv);
        Paint paint = tv.getPaint();
        Typeface typeface = paint.getTypeface();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            String name = typeface.getSystemFontFamilyName();
            Logx.d("字体===》" + name);
        }
        Logx.d("字体===》" + typeface.getStyle());
        test23();
        String str = "qwertyuiopasdfghjklzxcvbnm789456123qwertyuiopasdfghjklzxcvbnm789456123qwertyuiopasdfghjklzxcvbnm789456123qwertyuiopasdfghjklzxcvbnm789456123qwer";
        //
        ExpansionRl tvTest3 = (ExpansionRl) findViewById(R.id.tv_test3);
        tvTest3.setMsg(str);

    }

    //复制文件到sd卡
    private void test22() {
        String data = "/data/user/0/com.app.baseui/cache/image_manager_disk_cache/82d4c425e47188afaab829c1394ea038dfc57919cbe47e15d7bbe658475252fd.0";
        data = "/data/user/0/com.app.baseui/cache/image_manager_disk_cache/8da26e106d84ec3018e33d964fc2359d068bb3fd5de59a3574b17df03bf8a5e3.0";
        File file = new File(data);
        FileUtile.writeFile(file);
    }

    //输出文件类型
    private void test23() {
        String data = "/data/user/0/com.app.baseui/cache/image_manager_disk_cache/8da26e106d84ec3018e33d964fc2359d068bb3fd5de59a3574b17df03bf8a5e3.0";
        File file = new File(data);
        String fileType = FileTypeUtil.getMimeType(file);
        Logx.d("文件类型---》" + fileType);
        byte[] bytes = new byte[]{0, 0, 0, 28, 102, 116, 121, 112};
        String str = new String(bytes);
        Logx.d("文件类型---》" + str);
        str = FileTypeUtil2.getFileTypeByHeader(file);
        Logx.d("文件类型---》" + str);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.test_iv) {
            //重新加载图‘
            ImageLoadingUtile.loadImageTest(this, img, R.mipmap.ic_launcher, testIv);
            return;
        }
        if (id == R.id.delineate_btn) {
            //画线
            ActivityUtile.startActivityCommon(DelineateActivity.class);
            return;
        }
        if (id == R.id.open_app_btn) {
            //打开第三方应用
            ActivityUtile.startActivityCommon(OpenAppActivity.class);
            return;
        }
        if (id == R.id.canvas_btn) {
            //画布
            ActivityUtile.startActivityCommon(CanvasActivity.class);
            return;
        }
        if (id == R.id.lang_btn) {
            //多语言
            ActivityUtile.startActivityCommon(LangActivity.class);
            return;
        }
        if (id == R.id.btn_3d_tv) {
            //3D
            ActivityUtile.startActivityCommon(Test3DActivity.class);
            return;
        }
        if (id == R.id.dialog_btn) {
            new TcpTimeoutExample().test();
            //new DialogHint(this).show();
            //View view = findViewById(R.id.yc_btn);
            //new PopupPhotoOption(this).showDown(view);
            //test3();
            return;
        }
        if (id == R.id.xs_btn) {
            testView.setVisibility(View.VISIBLE);
            return;
        }
        if (id == R.id.yc_btn) {
            //testView.setVisibility(View.GONE);
            //Bitmap img = ImageBase64.convertBase64ToBitmap(test);
            return;
        }
        if (id == R.id.jtb_btn) {
              /*ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                boolean isText = cmb.hasPrimaryClip();
                Log.d("剪贴板", "isText=" + isText);
                if (isText) {
                    String txt = cmb.getPrimaryClip().getItemAt(0).getText().toString();
                    Log.d("剪贴板", "txt=" + txt);
                }*/
            // boolean granted = XXPermissions.isGranted(this, Manifest.permission.READ_CLIPBOARD);
            //test();
            //OpenAppManager.wxMiniApp(this);
            return;
        }
        if (id == R.id.main_btn) {
            //到主页
            ActivityUtile.startActivityCommon(MainActivity.class);
            //ActivityUtile.startActivityCommon(MainFragmentActivity.class);
            setStatusIconCollor(false);
            return;
        }
        if (id == R.id.login_btn) {
            ActivityUtile.startActivityCommon(TestLoginActivity.class);
            return;
        }
        if (id == R.id.card_btn) {
            //卡片
            ActivityUtile.startActivityCommon(TestCardActivity.class);
            return;
        }
        if (id == R.id.chat_btn) {
            //会话
            ActivityUtile.startActivityCommon(TestChatActivity.class);
            return;
        }


    }

    private void test() {

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", "content to copy");
        clipboard.setPrimaryClip(clip);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

        }
//<uses-permission android:name="android.permission.READ_CLIPBOARD" />
//<uses-permission android:name="android.permission.WRITE_CLIPBOARD" />
        // if (ContextCompat.checkSelfPermission(this, "") != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_CLIPBOARD", "android.permission.WRITE_CLIPBOARD"}, 100);
        //} else {
        // 权限已经被授予，可以直接访问剪贴板内容
        //}
    }

    private void test1() {
        View view = getWindow().getDecorView().getRootView();
        Dialog dialog = findDialog(view);
        Logx("dialog==null:" + (dialog == null));
    }

    private void test2() {

    }

    private WindowManager windowManager;
    private TextView addView;

    @Override
    protected void onPause() {
        super.onPause();
        if (windowManager != null && addView != null) {
            Logx("view 要删除");
            if (addView.isAttachedToWindow()) {
                Logx("view 正确删除");
                windowManager.removeView(addView);
            }

            windowManager = null;
        }

    }

    private void test3() {
        windowManager = WMViewMaanger.getInstance().getWindowManager(this);
        addView = new TextView(this);
        addView.setText("hahhaahah\nahahahahha\nahhdjlkdfjglkdlfjgl");
        addView.setBackgroundColor(Color.BLACK);
        addView.setTextColor(Color.WHITE);
        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logx("view被点击了");

            }
        });
        Logx("添加view");
        addView.setVisibility(View.GONE);
        windowManager.addView(addView, WMViewMaanger.getInstance().getWindowManagerParams());
    }

    private Dialog findDialog(View view) {
        String tag = "测试：";
        Context context = view.getContext();
        if (context instanceof DialogInterface) {
            Logx(tag + "它是一个dialog");
        }
        if (context instanceof Activity) {
            Logx(tag + "它是一个Activity" + ((Activity) context).getLocalClassName());
        }
        if (context instanceof AppCompatActivity) {
            Logx(tag + "它是一个AppCompatActivity");
        }

        if (view instanceof DialogInterface) {
            return (Dialog) (DialogInterface) view;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            Logx(tag + "viewGroup数量:" + childCount);
            for (int i = 0; i < childCount; i++) {
                Dialog dialog = findDialog(viewGroup.getChildAt(i));
                if (dialog != null) {
                    return dialog;
                }
            }
        }
        return null;
    }


    private void Logx(String tag) {
        Log.d("---->", tag);
    }

}

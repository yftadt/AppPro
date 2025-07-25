package test.app.ui.activity.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;





import java.util.Locale;

import sj.mblog.Logx;
import test.app.lang.LangManager;
import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.activity.databinding.ActivityTestLangBinding;


//测试3D
public class LangActivity extends NormalActionBar {

    private RadioGroup rbgroup;
    private RadioButton rb0;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;
    private RadioButton rb6;
    private RadioButton rb7;
    private ActivityTestLangBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestLangBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "语言");
        //
        // LangManager.initLang(this);
        initViews();
        //test1();
        test3();
    }

    private void initViews() {
        rbgroup = (RadioGroup) findViewById(R.id.rbgroup);
        rb0 = (RadioButton) findViewById(R.id.rb0);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rb3 = (RadioButton) findViewById(R.id.rb3);
        rb4 = (RadioButton) findViewById(R.id.rb4);
        rb5 = (RadioButton) findViewById(R.id.rb5);
        rb6 = (RadioButton) findViewById(R.id.rb6);
        rb7 = (RadioButton) findViewById(R.id.rb7);
    }

    private void test1() {
        //获取当前使用的语言 app有设置会返回app设置的，无设置会返回系统的
        Locale locale = Locale.getDefault();
        String str = locale.toLanguageTag();
        initData(str);

    }

    private void test2() {
        //获取当前使用的语言  取得是保存的缓存
        String str = LangManager.getLangRecord();
        if (TextUtils.isEmpty(str)) {
            str = LangManager.getUseLang();
        }
        initData(str);

    }

    private void test3() {
        //获取当前使用的语言 app有设置会返回app设置的，无设置会返回und
        LocaleListCompat temp = AppCompatDelegate.getApplicationLocales();
        String str = temp.toLanguageTags();
        Logx.d("语言：" + str);
        initData(str);
    }


    private void initData(String str) {
        Logx.d("语言 正在使用的语言：" + str);
        if (TextUtils.isEmpty(str)) {
            //系统
            rbgroup.check(rb0.getId());
        } else {
            if ("und".equals(str)) {
                //系统
                rbgroup.check(rb0.getId());
            }
            if ("zh-CN".equals(str)) {
                //中文简体
                rbgroup.check(rb1.getId());
            }
            if ("zh-TW".equals(str)) {
                //中文繁体
                rbgroup.check(rb2.getId());
            }
            if ("en".equals(str)) {
                //英文
                rbgroup.check(rb3.getId());
            }
        }


        rbgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                if(checkId==R.id.rb0){
                    //系统
                    changeLanguage("", "", false); //中文简体
                    return;
                }
                if(checkId==R.id.rb1){
                    //中文简体
                    changeLanguage("zh", "CN", false); //中文简体
                    return;
                }
                if(checkId==R.id.rb2){
                    //中文繁体
                    changeLanguage("zh", "TW", false);//中文繁体
                    return;
                }
                if(checkId==R.id.rb3){
                    //英文
                    changeLanguage("en", "", false);  //英语---默认系统语言
                    return;
                }


            }
        });
    }

    private void test(String language, String area) {
        //不为空，那么修改app语言，并true是把语言信息保存到sp中，false是不保存到sp中
        Locale newLocale = new Locale(language, area);
        LocaleListCompat localeList = LocaleListCompat.create(newLocale);
        AppCompatDelegate.setApplicationLocales(localeList);//会闪烁，语言切换后最好主动退出，解决闪烁问题
        //recreate();
        //binding.tvMessage.setText(R.string.message);
        //重启app,这一步一定要加上，如果不重启app，可能打开新的页面显示的语言会不正确
       /* Intent intent = new Intent(this, TestStartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/
        //ActivityUtile.startActivityCommon(TestStartActivity.class);
        finish();
    }


    //修改应用内语言设置
    private void changeLanguage(String language, String area, boolean isSave) {
        if (true) {
            test(language, area);
            return;
        }

        //不为空，那么修改app语言，并true是把语言信息保存到sp中，false是不保存到sp中
        Locale newLocale = new Locale(language, area);
        Context context = LangManager.changeAppLanguage(this, newLocale, isSave);
        if (context == this) {
            //重启app,这一步一定要加上，如果不重启app，可能打开新的页面显示的语言会不正确
            Intent intent = new Intent(this, LangActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            recreate();
            finish();
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }
}

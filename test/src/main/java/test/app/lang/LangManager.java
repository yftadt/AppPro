package test.app.lang;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;


import com.library.baseui.utile.file.DataSave;

import java.util.Locale;

import sj.mblog.Logx;
//无效  不用
public class LangManager {
    public static String use_lang_record = "use_lang_record";//记录使用的语言
    public static String use_lang = "";//现在使用的语言


    public static Context attachBaseContext(Context context) {
        Locale locale = getUpdateLocale(context);
        if (locale == null) {
            return context;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            initLang(context, locale);
            return context;
        } else {
            initLangOld(context, locale);
            return context;
        }
    }

    //24 初始化语言系统
    @TargetApi(Build.VERSION_CODES.N)
    private static void initLang(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        context.createConfigurationContext(configuration);
    }

    /**
     * 设置语言
     */
    private static void initLangOld(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);//语言更换生效的代码!
    }

    private static Locale getUpdateLocale(Context context) {
        Locale appLocale = getAppLocale(context);
        String tempLang = appLocale.toLanguageTag();
        String langRecord = getLangRecord();
        Logx.d("语言", " 系统语言：" + tempLang + " 保存的语言：" + langRecord);
        if (!TextUtils.isEmpty(tempLang) && tempLang.equals(langRecord)) {
            return null;
        }
        String[] str = langRecord.split("-");
        if (str == null || str.length < 2) {
            return null;
        }
        Locale locale = new Locale(str[0], str[1]);
        return locale;
    }

    //获取本地应该要使用的多语言
    private static Locale getAppLocale(Context context) {
        //获取应用语言
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = configuration.getLocales().get(0);
        } else {
            locale = configuration.locale;
        }
        use_lang = locale.toLanguageTag();
        return locale;
    }

    public static void initLang(Context context) {
        Locale appLocale = getUpdateLocale(context);
        if (appLocale == null) {
            //不需要更改语言
            Logx.d("语言不需要更改");
            return;
        }
        changeAppLanguage(context, appLocale, false);
    }

    /**
     * 更改应用语言
     *
     * @param
     * @param locale      语言地区
     * @param persistence 是否持久化
     */
    public static Context changeAppLanguage(Context context, Locale locale, boolean persistence) {
        Context tempContext = context;
        Logx.d("语言切换为：" + locale.toLanguageTag());
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Context updatedContext = context.createConfigurationContext(configuration);
            tempContext = updatedContext;
        } else {
            // 对于 API 级别低于 24 的设备，使用 updateConfiguration 方法
            DisplayMetrics metrics = resources.getDisplayMetrics();
            resources.updateConfiguration(configuration, metrics);
        }
        if (persistence) {
            saveLangRecord(locale.toLanguageTag());
        }
        return tempContext;
    }

    private static String langRecord = null;

    private static void saveLangRecord(String str) {
        Logx.d("语言保存：" + str);
        langRecord = str;
        DataSave.stringSave(str, "");
    }

    public static String getLangRecord() {
        if (langRecord == null) {
            langRecord = DataSave.stringGet(use_lang_record);
        }
        return langRecord;
    }

    public static String getUseLang() {
        return use_lang;
    }
}

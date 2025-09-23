# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\android\sdk_install/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#指定代码的压缩级别 0 - 7
-optimizationpasses 5
#是否使用大小写混合
-dontusemixedcaseclassnames
#如果应用程序引入的有jar包，并且想混淆jar包里面的class
#-dontskipnonpubliclibraryclasses
#混淆时是否做预校验（可去掉加快混淆速度）
#-dontpreverify
#混淆时是否记录日志（混淆后生产映射文件 map 类名 -> 转化后类名的映射
-verbose
#淆采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#忽略警告
-ignorewarnings
##  ######## 如果有引用v4包可以添加下面这行 ##########
# # -------------------------------------------
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.** { *; }
#AndroidX混淆开始
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**
#AndroidX混淆结束
###测试
#-keep class com.app.net.manager.** { *; }
-keep class com.app.net.manager.ApiNet { *; }
-keep class  com.app.net.manager.loading.ApiLoading{ *; }


#如果引用了v4或者v7包
-dontwarn android.support.*


# 如果你需要兼容6.0系统，请不要混淆org.apache.http.legacy.jar
-dontwarn android.net.compatibility.**
-dontwarn android.net.http.**
-dontwarn com.android.internal.http.multipart.**
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.**
-keep class android.net.compatibility.**{*;}
-keep class android.net.http.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class org.apache.commons.**{*;}
-keep class org.apache.http.**{*;}
# # -------------------------------------------
# #  ######## jackson混淆   ##########
# # -------------------------------------------
-dontwarn org.codehaus.jackson.**
-keep class org.codehaus.jackson.** {*; }
-keep interface org.codehaus.jackson.** { *; }
-keep public class * extends org.codehaus.jackson.**
-keep class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
# # -------------------------------------------
# #  ######## retrofit2混淆   ##########
# # -------------------------------------------
-dontwarn okio.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# keep okhttp3、okio
-dontwarn okhttp3.**
-keep class okhttp3.** { *;}
-keep interface okhttp3.** { *; }
-dontwarn okio.**
# # -------------------------------------------
# #  ######## EventBus混淆   ##########
# # -------------------------------------------
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

#
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# #  ######## app bean不混淆   ##########
# # -------------------------------------------
-keep class com.app.net.res.** { *; }
-keep class com.app.net.req.** { *; }

## 推送
-keep class com.app.push.** { *; }
# # -------------------------------------------
# #  ######## EvenBus混淆   ##########
# # -------------------------------------------
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# If using AsyncExecutord, keep required constructor of default event used.
# Adjust the class name if a custom failure event type is used.
-keepclassmembers class org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# Accessed via reflection, avoid renaming or removal
-keep class org.greenrobot.eventbus.android.AndroidComponentsImpl

# # -------------------------------------------
# #  ######## bugly混淆   ##########
# # -------------------------------------------
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
# # -------------------------------------------
# #  ######## FCM混淆   ##########
# # -------------------------------------------
-keep public class com.google.firebase.* {*;}

## 设置方法名称不混淆
-keepclassmembers class * {
    public *;
    protected *;
    private *;
    internal *;
}
-keep class kotlin.jvm.internal.* { *; }


#-------------- 去掉所有打印 -------------
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** e(...);
    public static *** i(...);
    public static *** v(...);
    public static *** println(...);
    public static *** w(...);
    public static *** wtf(...);
}
-assumenosideeffects class android.util.Log {
   public static *** d(...);
   public static *** v(...);
}

-assumenosideeffects class android.util.Log {
    public static *** e(...);
    public static *** v(...);
}

-assumenosideeffects class android.util.Log {
    public static *** i(...);
    public static *** v(...);
}

-assumenosideeffects class android.util.Log {
    public static *** w(...);
    public static *** v(...);
}
-assumenosideeffects class java.io.PrintStream {
    public *** println(...);
    public *** print(...);
}
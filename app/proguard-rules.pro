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
#忽略警告 也可以用-ignorewarnings
-ignorewarning
##  ######## 如果有引用v4包可以添加下面这行 ##########
# # -------------------------------------------
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.** { *; }
#如果引用了v4或者v7包
-dontwarn android.support.*
# # -------------------------------------------
# #  ######## greenDao混淆  ##########
# # -------------------------------------------
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use RxJava:
-dontwarn rx.**
# # -------------------------------------------
# #  ######## 支付宝混淆  ##########
# # -------------------------------------------
#-libraryjars libs/alipaySdk-20170407.jar
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-dontwarn com.alipay.**
# # -------------------------------------------
# #  ######## 个推混淆开始  ##########
# # -------------------------------------------
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }
-dontwarn com.igexin.**
# # -------------------------------------------
# #  ######## bmob混淆开始  ##########
# # -------------------------------------------
#-libraryjars libs/bmobos.jar
#-libraryjars libs/BmobSDK_V3.4.7_0527.jar
-keepattributes Signature,*Annotation*
# keep BmobSDK
-dontwarn cn.bmob.v3.**
-keep class cn.bmob.v3.** {*;}

# 确保JavaBean不被混淆-否则gson将无法将数据解析成具体对象
-keep class * extends cn.bmob.v3.BmobObject {
    *;
}
-keep class com.example.bmobexample.bean.BankCard{*;}
-keep class com.example.bmobexample.bean.GameScore{*;}
-keep class com.example.bmobexample.bean.MyUser{*;}
-keep class com.example.bmobexample.bean.Person{*;}
-keep class com.example.bmobexample.file.Movie{*;}
-keep class com.example.bmobexample.file.Song{*;}
-keep class com.example.bmobexample.relation.Post{*;}
-keep class com.example.bmobexample.relation.Comment{*;}

# keep BmobPush
-dontwarn  cn.bmob.push.**
-keep class cn.bmob.push.** {*;}

# keep okhttp3、okio
-dontwarn okhttp3.**
-keep class okhttp3.** { *;}
-keep interface okhttp3.** { *; }
-dontwarn okio.**

# keep rx
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

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
# #  ######## 城云视频混淆  ##########
# # -------------------------------------------
#-libraryjars libs/ccisipsdk2.jar
-keep class com.cci.sipsdk.** { *; }
-keep class com.company.** { *; }
-keep class com.huawei.** { *; }
-keep class com.radaee.** { *; }
-keep class common.** { *; }
-keep class confctrl.** { *; }
-keep class corpsdk.** { *; }
-keep class imssdk.** { *; }
-keep class object.** { *; }
-keep class tupsdk.** { *; }

-dontwarn com.cci.**
-dontwarn com.company.**
-dontwarn com.huawei.**
-dontwarn com.radaee.**
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
# # -------------------------------------------
# #  ######## EventBus混淆   ##########
# # -------------------------------------------
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
# # -------------------------------------------
# #  ######## butterknife混淆   ##########
# # -------------------------------------------
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
# # -------------------------------------------
# #  ######## app混淆   ##########
# # -------------------------------------------
-keep class com.app.net.res.** { *; }
-keep class com.app.net.req.** { *; }
-keep class com.app.ui.getui.PushVo{ public *;}
#-libraryjars libs/armeabi/libgetuiext2.so
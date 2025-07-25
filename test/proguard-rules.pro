# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\sdk_install/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
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
-ignorewarnings
##  ######## 如果有引用v4包可以添加下面这行 ##########
# # -------------------------------------------

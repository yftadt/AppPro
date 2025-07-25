package com.library.baseui.utile.app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;

import com.scottyab.rootbeer.RootBeer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import sj.mblog.Logx;

/**
 * 检查设备安全性
 */
public class DeviceSafe {
    /**
     * 检测开发者模式是否启用
     *
     * @param context
     * @return
     */
    public static boolean isDeveloperModeEnabled(Context context) {
        return Settings.Secure.getInt(
                context.getContentResolver(),
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
    }

    /**
     * 检测ADB调试模式是否启用
     *
     * @param context
     * @return
     */
    public static boolean isAdbEnabled(Context context) {
        return Settings.Secure.getInt(
                context.getContentResolver(),
                Settings.Global.ADB_ENABLED, 0) != 0;
    }

    /**
     * 检测设备是否已Root
     *
     * @param context
     * @return
     */
    public static boolean isDeviceRooted(Context context) {
        // 检查常见路径中的su二进制文件
        String[] locations = {
                "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/",
                "/system/sbin/", "/usr/bin/", "/vendor/bin/"
        };
        for (String location : locations) {
            if (new File(location + "su").exists()) {
                return true;
            }
        }
        // 尝试执行su命令
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String outputLine;
            while ((outputLine = reader.readLine()) != null) {
                if (outputLine.contains("su")) {
                    return true; // `su`命令可执行
                }
            }
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                if (errorLine.contains("not found") || errorLine.contains("Permission denied")) {
                    return false; // `su`命令不可执行
                }
            }
        } catch (IOException e) {
            // 处理异常
            Logx.d("root 检查异常：" + e.getMessage());
        } finally {
            if (process != null) {
                process.destroy();
            }
        }

        // 检查系统属性ro.build.tags
        try {
            Process processTags = Runtime.getRuntime().exec("getprop ro.build.tags");
            BufferedReader readerTags = new BufferedReader(new InputStreamReader(processTags.getInputStream()));
            String line = readerTags.readLine();
            if (line != null && (line.contains("test-keys") || line.contains("release-keys"))) {
                return false; // 开发者模式或其他标志
            }
        } catch (IOException e) {
            // 处理异常
        }
        // 检查常见的root管理应用
        String[] knownRootAppsPackages = {
                "com.koushikdutta.superuser", // SuperSU
                "com.topjohnwu.magisk",        // Magisk
                "eu.chainfire.supersu"         // SuperSU (alternative)
        };
        PackageManager packageManager = context.getPackageManager();
        for (String packageName : knownRootAppsPackages) {
            try {
                packageManager.getPackageInfo(packageName, 0);
                return true; // 发现root管理应用
            } catch (PackageManager.NameNotFoundException e) {
                // 未发现root管理应用
            }
        }
        return false; // 设备未Root
    }

    /**
     * 使用RootBeer检测设备是否已Root：
     *
     * @param context
     * @return
     */

    public static boolean isDeviceRooted2(Context context) {
        RootBeer rootBeer = new RootBeer(context);
        return rootBeer.isRooted();
    }

    public static void setCheck(Context context) {
        boolean isRooted = isDeviceRooted(context);
        boolean isRooted2 = isDeviceRooted2(context);
        boolean isDeveloperModeEnabled = isDeveloperModeEnabled(context);
        boolean isAdbEnabled = isAdbEnabled(context);
        String message = "您的设备状态正常";
        if (isRooted || isRooted2) {
            String root1 = "";
            String root2 = "";
            if (isRooted) {
                root1 = "(检查模式1)";
            }
            if (isRooted2) {
                root2 = "(检查模式2)";
            }
            if (isDeveloperModeEnabled) {
                if (isAdbEnabled) {
                    message = "您的设备已进入开发者模式并且已root" + root1 + root2 + "，同时已开启ADB调试模式，这可能带来严重安全风险。请在“设置” -> “开发者选项”中关闭开发者模式并取消设备root，同时关闭ADB调试模式。";
                } else {
                    message = "您的设备已进入开发者模式并且已root" + root1 + root2 + "，这可能带来安全风险。请在“设置” -> “开发者选项”中关闭开发者模式并取消设备root。";
                }
            } else {
                if (isAdbEnabled) {
                    message = "您的设备已root" + root1 + root2 + "并且已开启ADB调试模式，这可能带来安全风险。请取消设备root并关闭ADB调试模式。";
                } else {
                    message = "您的设备已root" + root1 + root2 + "，这可能带来安全风险。请取消设备root。";
                }
            }
        } else {
            if (isDeveloperModeEnabled) {
                if (isAdbEnabled) {
                    message = "您的设备已进入开发者模式并且已开启ADB调试模式，这可能带来安全风险。请在“设置” -> “开发者选项”中关闭开发者模式和ADB调试模式。";
                } else {
                    message = "您的设备已进入开发者模式，这可能带来安全风险。请在“设置” -> “开发者选项”中关闭开发者模式。";
                }
            } else {
                if (isAdbEnabled) {
                    message = "您的设备已开启ADB调试模式，这可能带来安全风险。请关闭ADB调试模式。";
                } else {
                    message = "您的设备状态正常。";
                }
            }
        }
        Logx.d("检查结果：" + message);
    }
}

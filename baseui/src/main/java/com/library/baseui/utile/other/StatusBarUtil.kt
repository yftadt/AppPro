package com.library.baseui.utile.other

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

/**
 * 状态栏工具类
 *
 * @author BJ
 * @date 2019/7/9
 */
object StatusBarUtil {
    /**可用
     * 设置全屏
     */
    fun setBarFull(activity: Activity) {
        //表示5.0
        var option = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        activity.window.decorView.systemUiVisibility = option
    }

    /**
     * 全透状态栏
     */
    fun setStatusBarFullTransparent(activity: Activity, isTranslate: Boolean = false) {
        //表示5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (isTranslate) {
                window.statusBarColor = Color.TRANSPARENT
            }
        } else { //19表示4.4
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    /**
     * 半透明状态栏
     */
    fun setHalfTransparent(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //21表示5.0
            val decorView = activity.window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else //19表示4.4
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //虚拟键盘也透明
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }


    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true
     */
    fun setFitSystemWindow(fitSystemWindow: Boolean, activity: Activity) {
        val contentViewGroup = (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        contentViewGroup.fitsSystemWindows = fitSystemWindow
    }

    /**
     * 黑色字体
     */
    fun setStatusColor(activity: Activity, isTranslate: Boolean, isDarkText: Boolean, bgColor: Int) {
        //如果系统为6.0及以上，就可以使用Android自带的方式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity.window
            val decorView = window.decorView
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) //可有可无
            decorView.systemUiVisibility = (if (isTranslate) View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN else 0) or if (isDarkText) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
            window.statusBarColor = if (isTranslate) Color.TRANSPARENT else ContextCompat.getColor(activity, bgColor) //Android5.0就可以用
        }
    }

    /**
     * 设置沉浸式状态栏
     * 应在setContentView之后调用
     */
    fun immerseStatusBar(activity: Activity, lightMode: Boolean = false) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //清除
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)//需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            activity.window.statusBarColor = Color.TRANSPARENT
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            var ui = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (lightMode) {
                ui = ui or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            activity.window.decorView.systemUiVisibility = ui
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    /**可用
     * 设置沉浸式状态栏颜色
     * 应在setContentView之后调用
     */
    fun setStatusBarColor(activity: Activity, isTranslucent: Boolean, @ColorInt color: Int, lightMode: Boolean = false) {
        if (isTranslucent) {
            val window: Window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.statusBarColor = color
        }
        var ui: Int = activity.window.decorView.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ui = if (lightMode) {
                ui or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                ui and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
        activity.window.decorView.systemUiVisibility = ui
    }
    //获取状态栏高度
    fun getStatusBarHeight(context:Context): Int {
        val resources: Resources =  context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
}
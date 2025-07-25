package test.app.open

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri



abstract class BaseManager {


    private var act: Activity? = null
    fun setActivity(act: Activity): BaseManager {
        this.act = act
        return this
    }

    abstract fun openApp(url: String)
    protected fun getActivity(): Activity? {
        return act
    }

    /**
     * 打开应用
     */
    fun openOtherApp(url: String) {
        var act = getActivity()
        if (act == null || act.isDestroyed) {
            return
        }
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory("android.intent.category.BROWSABLE")
        act.startActivity(intent)
    }

    /**
     * 检查应用是否安装
     */
    fun isCheckAppInstall(act: Activity, packageName: String?): Boolean {
        if (packageName == null || "" == packageName) return false
        try {
            //手机已安装，返回true
            act.packageManager.getPackageInfo(packageName, 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

}
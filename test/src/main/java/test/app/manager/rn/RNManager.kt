/*
package com.sw.swzs.manager.rn

import android.app.Activity

import sj.mblog.Logx
import java.util.HashMap


class RNManager {
    private val mapClose = HashMap<String, RnBean>()
    private val mapResume = HashMap<String, RnBean>()

    fun put(kye: String, rnBean: RnBean) {
        when (rnBean.type) {
            "1" -> mapClose[kye] = rnBean
            "2" -> mapResume[kye] = rnBean
        }

    }

    fun del(key: String) {
        mapClose.remove(key)
        mapResume.remove(key)
    }

    fun onUpdateToClose(key: String) {
        if (mapClose.size == 0) {
            return
        }
        Logx.d("RN 回调 获取key=" + key)
        var bean = mapClose[key] ?: return
        onUpdate(key, bean)
        mapClose.remove(key)
    }

    fun onUpdateToResume(key: String) {
        if (mapResume.size == 0) {
            return
        }
        Logx.d("RN 回调 获取到key=" + key)
        var bean = mapResume[key] ?: return
        onUpdate(key, bean)
        mapResume.remove(key)
    }

    private fun onUpdate(key: String, bean: RnBean) {
        try {
            Logx.d("RN 回调 ")
            bean.onBack.invoke(bean.type)
        } catch (e: Exception) {
            Logx.d("RN 回调 获取到key=" + key + " 错误信息：" + e.message)
        }
    }

    //true 热更新
    private var isHotUpdate = true

    fun geReactInstanceManager(act: Activity): ReactInstanceManager {
        if (!isHotUpdate) {
            return getLocalReactInstanceManager(act)
        } else {
            return getSelfReactInstanceManager(act)
        }
    }

    private fun getLocalReactInstanceManager(act: Activity): ReactInstanceManager {
        val packages: List<ReactPackage> = PackageList(act.application).packages
        var manager =
            ReactInstanceManager.builder()
                .setApplication(act.application).setCurrentActivity(act)
                .setBundleAssetName("index.android.bundle")
                .setJSMainModulePath("index")
                .addPackages(packages).addPackage(
                    HNKJNativePackage()
                ).addPackage(RNManagerPackage())
                .setUseDeveloperSupport((act as BaseActivity<*>).isRnDebug())
                .setInitialLifecycleState(
                    LifecycleState.RESUMED
                ).build()
        return manager
    }


    private fun getSelfReactInstanceManager(act: Activity): ReactInstanceManager {
        val packages: List<ReactPackage> = PackageList(act.application).packages
        var manager =
            ReactInstanceManager.builder()
                .setApplication(act.application).setCurrentActivity(act)
                //.setBundleAssetName("index.android.bundle")
                //调用setJSBundleFile 之后不要调用setBundleAssetName
                .setJSBundleFile(
                    RNUpdateManager.getInstance().getRunPath("assets://index.android.bundle")
                )
                .setJSMainModulePath("index")
                .addPackages(packages).addPackage(
                    HNKJNativePackage()
                ).addPackage(RNManagerPackage())
                .setUseDeveloperSupport((act as BaseActivity<*>).isRnDebug())
                .setInitialLifecycleState(
                    LifecycleState.RESUMED
                ).build()
        return manager
    }

    private object ManagerHolder {
        val instance = RNManager()
    }

    companion object {
        @JvmStatic
        val instance: RNManager
            get() = ManagerHolder.instance
    }
}*/

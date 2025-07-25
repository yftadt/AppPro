package test.app.open

import com.library.baseui.utile.toast.ToastUtile

import sj.mblog.Logx
import test.app.open.BaseManager
import java.util.Date


class OpenPDDManager : BaseManager() {


    override fun openApp(url: String) {
        var appName = "拼多多"
        var appPage = "com.xunmeng.pinduoduo"
        var act = getActivity()
        if (act == null || act.isDestroyed) {
            return
        }
        var isInstall = isCheckAppInstall(act, appPage)
        if (!isInstall) {
            ToastUtile.showToast(appName + "尚未安装")
            openOtherApp(url)
        } else {
            openOtherAppUrlCheck(url)
        }
    }

    private fun openOtherAppUrlCheck(url: String) {
        //貌似全部是这种格式
        var urltag = "pinduoduo://com.xunmeng.pinduoduo/"
        var temp = url
        if (!url.contains(urltag)) {
            var str = url.split(".com/")
            if (str.size == 2) {
                temp = urltag + str[1]
            }
        }
        //如果网址不变：拼多多打开目标页面以后 再返回，在打开目标页面时不会动，只会停留在主页
        if (temp.contains("&")) {
            var argument = "&=argument" + Date().time
            temp += argument
        }
        Logx.d("打开拼多多", temp)
        openOtherApp(temp)
    }

}
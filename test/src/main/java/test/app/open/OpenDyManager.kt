package test.app.open

 import com.library.baseui.utile.toast.ToastUtile
 import test.app.open.BaseManager


class OpenDyManager : BaseManager() {


    override fun openApp(url: String) {
        var appName = "抖音"
        var appPage = "com.ss.android.ugc.aweme"
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
        //
        openOtherApp(url)
    }

}
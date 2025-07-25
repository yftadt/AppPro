package test.app.open

import com.library.baseui.utile.toast.ToastUtile
import sj.mblog.Logx

import java.net.URLEncoder


class OpenJDManager : BaseManager() {


    override fun openApp(url: String) {
        var appName = "京东"
        var appPage = "com.jingdong.app.mall"

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
        var temp = url
        if (!temp.startsWith("openApp.jdMobile:")) {
            var str =
                "{\"category\":\"jump\",\"des\":\"m\",\"sourceValue\":\"babel-act\",\"sourceType\":\"babel\",\"url\":\"" + url + "\",\"M_sourceFrom\":\"h5auto\",\"msf_type\":\"auto\"}"
            /*var map = HashMap<String, String>()
            map["category"] = "jump"
            map["des"] = "m"
            map["sourceValue"] = "babel-act"
            map["sourceType"] = "babel"
            map["url"] = url
            map["M_sourceFrom"] = "h5auto"
            map["msf_type"] = "auto"*/
            var encode = URLEncoder.encode(str)
            temp = "openApp.jdMobile://virtual?params=" + encode
        }
        Logx.d("打开京东", url)
        Logx.d("打开京东", temp)
        openOtherApp(temp)
    }

}
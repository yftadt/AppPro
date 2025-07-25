package test.app.open

import com.library.baseui.utile.toast.ToastUtile
import test.app.open.BaseManager


class OpenKSManager : BaseManager() {


    override fun openApp(url: String) {
        var appName = "快手"
        var appPage = "com.smile.gifmaker"
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
        //var urltag = "kwai://webview?"
        //var temp = URLEncoder.encode(url)
        //urltag += temp
        //urltag="kwai://webview?url=https%3A%2F%2Fapp.kwaixiaodian.com%2Fweb%2Fkwaishop-goods-detail-page-app%3Fcc%3Dshare_copylink%26kpf%3DIPHONE%26fid%3D3374198015%26shareMode%3Dapp%26shareMethod%3Dtoken%26tokenLogType%3DdiversitySeparatorLongToken%26kpn%3DKUAISHOU%26subBiz%3DKS_FENXIAO%26shareId%3D18213656074936%26shareToken%3DX-9WrHwXcv8o61xc%26shareObjectId%3D23453218459554%26id%3D23453218459554&tokenBlock=1&shareId=18213656074936&subBiz=KS_FENXIAO&openFrom=%7B%22url%22%3A%22https%3A%2F%2F30cza.xmmicvfxi.com%2Fweb%2Fkwaishop-goods-detail-page-app%3Fcc%3Dshare_copylink%26kpf%3DIPHONE%26fid%3D3374198015%26shareMode%3Dapp%26shareMethod%3Dtoken%26tokenLogType%3DdiversitySeparatorLongToken%26kpn%3DKUAISHOU%26subBiz%3DKS_FENXIAO%26shareId%3D18213656074936%26shareToken%3DX-9WrHwXcv8o61xc%26shareObjectId%3D23453218459554%26id%3D23453218459554%22%2C%22cc%22%3A%22share_copylink%22%2C%22fid%22%3A%223374198015%22%2C%22did%22%3A%22web_734413a6a03840b7965fa28fd95226a5%22%2C%22from%22%3A%22android%22%2C%22channel%22%3A%22share%22%2C%22position%22%3A%22%E6%89%93%E5%BC%80%E5%BF%AB%E6%89%8BApp%22%7D"
        //Logx.d("打开快手", urltag)
        openOtherApp(url)
    }

}
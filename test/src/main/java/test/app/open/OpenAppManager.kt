package test.app.open

import android.app.Activity
import android.content.Context


public class OpenAppManager {


    fun openApp(act: Activity, functionKey: String, url: String) {
        var manger: BaseManager? = null
        when (functionKey) {
            "OPEN_PDDHD", "OPEN_PDDPT", "OPEN_PDDPT_TEST" -> {
                //拼多多
                manger = OpenPDDManager()
            }

            "OPEN_TBHD" -> {
                //研淘宝
                manger = OpenTBManager()

            }

            "OPEN_JDHD" -> {
                //京东
                manger = OpenJDManager()

            }

            "OPEN_KS" -> {
                //快手
                manger = OpenKSManager()
            }
            "OPEN_DY" -> {
                //抖音
                manger = OpenDyManager()
            }
        }
        manger?.setActivity(act)
        manger?.openApp(url)

    }

    //淘口令打开
    fun openAppKL(act: Activity, functionKey: String, url: String) {
        when (functionKey) {
            "OPEN_TBHD" -> {
                var manger = OpenTBManager()
                manger.setActivity(act)
                manger.openTaoBaoApp()
            }

        }
    }

    private object ManagerHolder {
        val instance = OpenAppManager()
    }

    fun wxApp(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage("com.tencent.mm")
        context.startActivity(intent)
    }

    //打开微信小程序
    fun wxMiniApp(con: Context?) {
        //法大大：appid=wx76f28100d5710338
        // 原始ID=gh_22914c355385
        val miniAppId = "wx76f28100d5710338"
        val path = ""
        //要打开的页面
        /*val api = WXAPIFactory.createWXAPI(con, otherConfig.wechat.appId)

        val req = WXLaunchMiniProgram.Req()

        req.userName = "gh_e2511566f370" // 填小程序原始id

        req.path = "pages/hpSpecialist/hlbeSpecialist" ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。

        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE // 可选打开 开发版，体验版和正式版

        api.sendReq(req)*/
    }
    companion object {
        @JvmStatic
        val instance: OpenAppManager
            get() = ManagerHolder.instance
    }
}
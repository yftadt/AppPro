package test.app.open

import android.content.Intent
import android.net.Uri
import com.library.baseui.utile.toast.ToastUtile
import sj.mblog.Logx

import test.app.open.BaseManager


class OpenTBManager : BaseManager() {

    //打开淘宝
    fun openTaoBaoApp() {
        var act = getActivity()
        if (act == null || act.isDestroyed) {
            return
        }
        var intent = act.packageManager.getLaunchIntentForPackage("com.taobao.taobao")
        intent?.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent?.addCategory("android.intent.category.BROWSABLE")
        act.startActivity(intent)
    }


    override fun openApp(url: String) {
        var appName = "淘宝"
        var appPage = "com.taobao.taobao"
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
        //tbopen://m.taobao.com/tbopen/index.html?h5Url=https%3A%2F%2Fuland.taobao.com%2Fcoupon%2Fedetail%3Fe%3D9nO3TO85rxClhHvvyUNXZfh8CuWt5YH5OVuOuRD5gLJMmdsrkidbOWBzzpT26idJAsxzDOnNI6u91Rs23934%252BR3IIVemz1lo%252BxM8Ws8c3mAEBParLnxZKgF%252BCrgLwCaEk3toOSlQXmUUcLJJYP0Uq7Kr%252FrPaBPPmT%252F1mTtwPHxT0fJdlTnMtJ7zyLDEH%252BRdaeOx3QABhn03ZWB8yRYykDoTdig4t62Y8b%252FQFo4AOnZegJbXyzB1Cwsi60bBTauYG0g9sJZIZMaKyq%252F6z2gTz5rcZelJt%252Bzjy3e0yoyBTEcvSbhe62Zg%252BOSF%252BB1UWTwNemDCSbDT2I2q5ydwAC3myF1Vusqqnhify%26traceId%3D21050db617261043614415775e1eca%26relationId%3D3111129563%26union_lens%3DlensId%3ATAPI%401726104361%40216698cc_1225_191e3d65a3b_a46d%4001%26activityId%3D0e2e1d44593d4c3298b5849ee4ad7788%26slk_gid%3Dgid_er_er%257Cgid_er_af_pop%257Cgid_er_sidebar_0&action=ali.open.nav&module=h5&bootImage=0&slk_sid=tjZpHwQ/kAcCAWoGPCAb4EpY_1726107373607&slk_t=1726107373626&slk_gid=gid_er_er%7Cgid_er_af_pop%7Cgid_er_sidebar_0&afcPromotionOpen=false&bc_fl_src=h5_huanduan&source=slk_dp
        //淘宝商品 可打开
        //https://uland.taobao.com/coupon/edetail?e=9nO3TO85rxClhHvvyUNXZfh8CuWt5YH5OVuOuRD5gLJMmdsrkidbOWBzzpT26idJAsxzDOnNI6u91Rs23934+R3IIVemz1lo+xM8Ws8c3mAEBParLnxZKgF+CrgLwCaEk3toOSlQXmUUcLJJYP0Uq7Kr/rPaBPPmT/1mTtwPHxT0fJdlTnMtJ7zyLDEH+RdaeOx3QABhn03ZWB8yRYykDoTdig4t62Y8b/QFo4AOnZegJbXyzB1Cwsi60bBTauYG0g9sJZIZMaKyq/6z2gTz5rcZelJt+zjy3e0yoyBTEcvSbhe62Zg+OSF+B1UWTwNemDCSbDT2I2q5ydwAC3myF1Vusqqnhify&traceId=21050db617261043614415775e1eca&relationId=3111129563&&union_lens=lensId:TAPI@1726104361@216698cc_1225_191e3d65a3b_a46d@01&activityId=0e2e1d44593d4c3298b5849ee4ad7788
        if (url.contains("https://uland.taobao.com/coupon/edetail?e=")) {
            var tmp = "tbopen://m.taobao.com/tbopen/index.html?h5Url=" + url
            openOtherApp(tmp)
            return
        }
        //淘宝活动  打不开对应的页面  可能是其它一些页面
        //https://s.click.taobao.com/t?union_lens=lensId%3AMAPI%401726116051%402106c218_134d_191e488b9b6_a50e%4001%40eyJmbG9vcklkIjo4ODYzOH0ie%3BeventPageId%3A20150318020016122&e=m%3D2%26s%3DGE5G9S4lcTVw4vFB6t2Z2iperVdZeJviU%2F9%2F0taeK29yINtkUhsv0Ay5axuvNrFSuMnR%2Bo32kurLePpVa6ChWFPukb9BYFwUwW1KZDrTzIK%2FYp4T7sV6mfYuc71rx11iAGIx0oe2X2hZfJ7ZQxC1%2Fb%2BmvmXqUq2iEBnEBk3xaGylLmcSHKfaX7mF0vI1ZsIL81t6POd4ujeDUcZf5l6E27qdmmTA%2BxZw9ie9yII59KjO54LQ%2FVw1L%2B3qDFf3G11Zj5QwTervQtmHWWrZt0BFT261pPB3rRJ3qnEMuznJbCUKTfW%2BjcWf2gKMM%2B8LpG6sTdrdtBL5HigCvZuBJPx5YLf2anhEob7h2o96oyOcLw9nHisTXG%2FPFJXdKCvVsmnwFkdMrR0JdyLkw1nwChX752ZeChxABkvxz6GXxc3tQdeIfnrg1PVyhr%2FVrGlEZXJuF1IWp%2BDTE8%2B0NAR5Gh2I8ww9zdxU5aWKK%2F%2BZ27JO57%2F2VAhKYa2CXONz2L0RiKXtH95eV4i%2BDlZa55fucyyW0XnWwnbNEyQPG%2FzIQH4iAgqTZv4MXcoP01rvmE1KEcmhS1GsXmAy%2BzXw519dfpMetVolN5luXW3KYmEJmtdoy7zkE2ereXiuWdxS2t6QltALIYULNg46oBA%3D', longUrl='https://s.click.taobao.com/t?union_lens=lensId%3AMAPI%401726116051%402106c218_134d_191e488b9b6_a50e%4001%40eyJmbG9vcklkIjo4ODYzOH0ie%3BeventPageId%3A20150318020016122&e=m%3D2%26s%3DGE5G9S4lcTVw4vFB6t2Z2iperVdZeJviU%2F9%2F0taeK29yINtkUhsv0Ay5axuvNrFSuMnR%2Bo32kurLePpVa6ChWFPukb9BYFwUwW1KZDrTzIK%2FYp4T7sV6mfYuc71rx11iAGIx0oe2X2hZfJ7ZQxC1%2Fb%2BmvmXqUq2iEBnEBk3xaGylLmcSHKfaX7mF0vI1ZsIL81t6POd4ujeDUcZf5l6E27qdmmTA%2BxZw9ie9yII59KjO54LQ%2FVw1L%2B3qDFf3G11Zj5QwTervQtmHWWrZt0BFT261pPB3rRJ3qnEMuznJbCUKTfW%2BjcWf2gKMM%2B8LpG6sTdrdtBL5HigCvZuBJPx5YLf2anhEob7h2o96oyOcLw9nHisTXG%2FPFJXdKCvVsmnwFkdMrR0JdyLkw1nwChX752ZeChxABkvxz6GXxc3tQdeIfnrg1PVyhr%2FVrGlEZXJuF1IWp%2BDTE8%2B0NAR5Gh2I8ww9zdxU5aWKK%2F%2BZ27JO57%2F2VAhKYa2CXONz2L0RiKXtH95eV4i%2BDlZa55fucyyW0XnWwnbNEyQPG%2FzIQH4iAgqTZv4MXcoP01rvmE1KEcmhS1GsXmAy%2BzXw519dfpMetVolN5luXW3KYmEJmtdoy7zkE2ereXiuWdxS2t6QltALIYULNg46oBA%3D
        //url = "https://s.click.taobao.com/t?union_lens=lensId%3AMAPI%401726109051%4021084f6d_1426_191e41deb01_7766%4001%40eyJmbG9vcklkIjo4ODYzOH0ie%3BeventPageId%3A20150318020016122&e=m%3D2%26s%3DcobKq190EBNw4vFB6t2Z2iperVdZeJviU%2F9%2F0taeK29yINtkUhsv0BZZsUZROQ4DuMnR%2Bo32kurLePpVa6ChWFPukb9BYFwUwW1KZDrTzIK%2FYp4T7sV6mfYuc71rx11iAGIx0oe2X2hZfJ7ZQxC1%2Fb%2BmvmXqUq2iEBnEBk3xaGylLmcSHKfaX7mF0vI1ZsIL81t6POd4ujeDUcZf5l6E27qdmmTA%2BxZw9ie9yII59KjO54LQ%2FVw1L%2B3qDFf3G11Zj5QwTervQtmHWWrZt0BFT261pPB3rRJ3qnEMuznJbCUKTfW%2BjcWf2gKMM%2B8LpG6sTdrdtBL5HigCvZuBJPx5YLf2anhEob7h2o96oyOcLw9nHisTXG%2FPFJXdKCvVsmnwFkdMrR0JdyLkw1nwChX752ZeChxABkvxz6GXxc3tQdeIfnrg1PVyhr%2FVrGlEZXJuF1IWp%2BDTE8%2B0NAR5Gh2I8ww9zdxU5aWKK%2F%2BZ27JO57%2F2VAhKYa2CXONz2L0RiKXtH95eV4i%2BDlZa55fucyyW0XnWwnbNEyQPG%2FzIQH4iAgqTZv4MXcoP0wGOkq3GdzR17cn%2BWPnHzd%2F0QYPr7Dt2TtphjAusosNfYmEJmtdoy7zkE2ereXiuWdxS2t6QltALIYULNg46oBA%3D";
        if (url.contains("https://s.click.taobao.com/t?union_lens=")) {
            var tmp = "tbopen://m.taobao.com/tbopen/index.html?h5Url=" + url
            openOtherApp(tmp)
            return
        }
        //淘宝活动 可打开
        //https://mo.m.taobao.com/union2/page_
        if (url.contains("https://mo.m.taobao.com/union2/page_")) {
            var tmp = "tbopen://m.taobao.com/tbopen/index.html?h5Url=" + url
            openOtherApp(tmp)
            return
        }
        //指定到淘宝详情页
        //https://detail.m.tmall.com/item.htm?id=788148159889&ali_trackid=2:mm_6780106115_3113200276_115732600203:1725866250553_556439609_0&union_lens=lensId:TAPI@1725866249@213dcdf8_12a2_191d5a50edb_40b8@01;recoveryid:556439609_0@1725866250553&ak=33954624&e=9NGspF9NvNT-AUPYJvYe3BevvGA4rTDc1n2u1uaGfFTcacS4cJfQIiobM-nNOdaydmrkvzNNNQArcHC8ppjTrg5_BV1nUcFko-yGanSDSxRKR9TPuL-YQ3_HbbipkjQPtvwJFnLcSqzaOg1oaBnuD2ArsTO3s7t31m1UR_MnIORcWZ_OWTAmRznpmhTObRx2eAkx3bKOzfvern_qxR1skoZgFxumDJcicRTtk4BiF6TP-uhOejA3EaEBQCjbjY7hcWasuMqhAosuvYFSDoPZJMjXTAPLLoomyV8Ylm_E-ywYO3tDIyMBGfQa7PDPM8jeU2L8UzON8CiA2pX5rAlTGoJFBNEZHclaTkt51SWH85iQRTINp-Xug2B7NOh_nL_K-6eNUz8Lz0a0pX_HkMXOBQSL9ak6wSaqbQe-I9RBfdbuRhSl_eppYlRriIGc1c-QakZAjYe9zgKaDfom0pZhm3L8IXyWPmkgxg5p7bh-FbQ&type=2&tk_cps_param=6780106115&tkFlag=0&tk_cps_ut=2&bxsign=tbkM5a4APNwidKyVsqa5IV-s0aUrDgu73Ov6qn2DiFp5rwmihTfDb3YG26mjkQG5uYbtBJFjA0PPtp73plOz_27klQ_yz0xTkMe_W_w8idHn75ULqmKnIKWfboOloY1fPc2cy3n3LXf9IGC4qTYFc7mA4fX7Hi4s4XI76zE5UdYnVQPE6x_-Fvszd5gQ2Fv7dFy
        if (url.contains("detail.m.tmall.com/item.htm?id=")) {
            openTaoBaoDetailApp(url)
            return
        }
        //https://h5.m.taobao.com/awp/core/detail.htm?id=746165672629&ali_trackid=2:mm_6780106115_3113200276_115732600203:1725865750519_557671852_0&union_lens=lensId:TAPI@1725865749@216621fd_1204_191d59d6d0b_b5a9@01;recoveryid:557671852_0@1725865750519&ak=33954624&e=JblaQoNQQ0v-AUPYJvYe3BevvGA4rTDc1n2u1uaGfFTcacS4cJfQIiobM-nNOdaydmrkvzNNNQArcHC8ppjTrg5_BV1nUcFko-yGanSDSxRKR9TPuL-YQ3_HbbipkjQPtvwJFnLcSqzaOg1oaBnuD2ArsTO3s7t31m1UR_MnIOR0BN6zV9-tnjnpmhTObRx2MWco0HG1Q4XnrKGKd0ehGP69padEnnJQVAMwbi3MaSiaSLd4xpsAcceHC5WDJMk6l01xbjNkOJ1KrxiU1dqORJpGmigrKP4pFd3o_0ICOHocXm6dIVhsxR_mfi-zkCmMGYCaxlfctniDa7BZNRT7PUbNeBVKLupQ3930lyqI4HYmw20OR8LvT7pkMpzwp2CjVgZi4QVox_hw5aDX8UCcVk3FiG1R1RpWB5ceVpH-JWaCh7V_Jqu93Ugdq5WLZHVHNvz8hDlQWghar-lV7wZHDIWy5R__vFO06Fc1G4iDUnrGJe8N_wNpGw&type=2&tk_cps_param=6780106115&tkFlag=0&tk_cps_ut=1&bxsign=tbkk5kkWL9g8HMl_fH6_gVmX0bZjpnR96z-hlaq_riiqlhn78qNLZVft1VLhVrdN6Gua1cbwtacEVW-k-HOalvDZfW8iZVab0vgup-oprPobR0uQQSfen2vk7eI6Rg8aBka3VQi2NV0xUY4h0er4M7f_sbn0fMgrbVS73WquHuuSg9uvLh2bJEf78rnf-K2_Kaa
        if (url.contains("h5.m.taobao.com/awp/core/detail.htm?id=")) {
            openTaoBaoDetailApp(url)
            return
        }
        //开头是："tbopen://" 或者是其它网页
        openOtherApp(url)

    }


    //到淘宝详情页
    fun openTaoBaoDetailApp(url: String) {
        var act = getActivity()
        if (act == null || act.isDestroyed) {
            return
        }
        Logx.d("url--->捕获", url)
        val uri = Uri.parse(url)
        var intent = Intent()
        intent.data = uri
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory("android.intent.category.BROWSABLE")
        intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity")
        act.startActivity(intent)
    }

}
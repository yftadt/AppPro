package test.app.ui.activity.test.img

import android.os.Bundle

import com.library.baseui.utile.toast.ToastUtile


import media.library.able.MediaResIbl
import media.library.images.config.entity.MediaEntity
import media.library.manager.GoogleImgVideo
import org.greenrobot.eventbus.EventBus
import test.app.ui.activity.R
import test.app.ui.activity.action.BaseBarActivity1
import test.app.ui.activity.databinding.ActImgOptBinding
import test.app.ui.event.EventImgData
import test.app.utiles.permissi.PermissionUtile

//google相册
class ImgOptAct : BaseBarActivity1() {

    private  var  binding: ActImgOptBinding?=null

    //1 选择头像 2 多选（照片和视频） 3 单选一张照片  4多选（照片 含GIF）
    private var type = ""

    //1：使用EventBus回调
    private var backType = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActImgOptBinding.inflate(getLayoutInflater())
        setContentView(binding!!.getRoot())
        type = getStringExtra("arg0")
        backType = getStringExtra("arg1")
        setImmersion()
        GoogleImgVideo.getInstance().initPickMedia(this)
        GoogleImgVideo.getInstance().initPickMultipleMedia(this, 9)

        PermissionUtile.setCheckPermission(this, 4, object : test.app.utiles.permissi.OnPMCallback {

            override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                onAcquiredAll()
            }

            override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                ToastUtile.showToast("没有权限")
                finish()
            }

            override fun onAcquiredAll() {
                setView()
            }

            override fun onError() {
            }
        })


        setClick()
    }



    private fun setView() {
        when (type) {
            "1" -> {
                //1 选择头像

                GoogleImgVideo.getInstance().setSingleChoice(2)
            }

            "2" -> {
                //2 多选（照片和视频）

            }

            "3" -> {
                // 3 单选一张照片
                GoogleImgVideo.getInstance().setSingleChoice(2)
            }

            "4" -> {
                //4多选（照片 含GIF）
                GoogleImgVideo.getInstance().setMultipleChoice(2)
            }

        }


    }

    private fun setClick() {
        GoogleImgVideo.getInstance().setMediaResIbl(object : MediaResIbl {
            override fun onMediaRes(res: ArrayList<MediaEntity>) {
                imgs = res
                setRes()
            }

            override fun onDismiss() {
                finish()
            }

        })
    }

    private var imgs = ArrayList<MediaEntity>()
    private fun setRes() {
            var event = EventImgData(this)
            event.type = type
            event.imgs = imgs
            EventBus.getDefault().post(event)
            finish()
     }

    override fun onDestroy() {
        GoogleImgVideo.getInstance().onDestroy()
        super.onDestroy()
    }

    override fun finish() {
        super.finish()
        // 无进入动画，底部退出
        overridePendingTransition(R.anim.no_animation, R.anim.activity_bottom_out)
    }



}

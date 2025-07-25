package com.app.ui.activity


import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ui.activity.base.RootActivity
import com.app.ui.activity.databinding.ActMainBinding
import com.library.baseui.utile.app.ActivityUtile

import sj.mblog.Logx
import test.app.ui.activity.test.TestStartActivity


public class MainAct : RootActivity<ActMainBinding>() {

    override fun getLayoutBinding(inflater: LayoutInflater): ActMainBinding {
        return ActMainBinding.inflate(inflater)
    }

    override fun onInitView() {
        setImmersion()

        setView()
        setClick()
        setData()
        ActivityUtile.startActivityCommon(TestStartActivity::class.java)
    }

    private fun setView() {

    }

    private fun setData() {


    }


    private val PRESS_BACK_TIME: Long = (2 * 1000).toLong()

    /**
     * 上次按返回的时间
     */
    private var lastPressBackTime: Long = 0

    override fun onBackPressed() {
        val currentPressBackTime = System.currentTimeMillis()
        if (currentPressBackTime - lastPressBackTime < PRESS_BACK_TIME) {
            moveTaskToBack(true)
        } else {
            //ToastUtile.showToast("再按一次退出")
        }
        lastPressBackTime = currentPressBackTime
    }

    private fun setClick() {

    }

}
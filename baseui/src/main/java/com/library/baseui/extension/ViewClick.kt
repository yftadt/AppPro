package com.library.baseui.extension

import android.view.View
import android.widget.Checkable
import com.library.baseui.R


//兼容点击事件设置为this的情况
fun <T : View> T.setSingleClick(time: Long = 800,onClickListener: View.OnClickListener ) {
    setOnClickListener {
        val curTime = System.currentTimeMillis()
        if (curTime - lastClickTime > time || this is Checkable) {
            lastClickTime = curTime
            onClickListener.onClick(this)
        }
    }
}

var <T : View> T.lastClickTime: Long
    set(value) = setTag(R.id.single_click_tag, value)
    get() = getTag(R.id.single_click_tag) as? Long ?: 0


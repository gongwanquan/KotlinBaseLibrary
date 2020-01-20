package com.dms.baselibrary.base.activity

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.dms.baselibrary.R

import com.jaeger.library.StatusBarUtil
import com.dms.baselibrary.widget.TitleBar

abstract class BaseTitleActivity : BaseActivity() {

    protected val mTitleBar: TitleBar by lazy {
        TitleBar(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBar()
        setTitleBar()
        super.onCreate(savedInstanceState)
    }

    open fun setStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.color_title_bar_bg), 0)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    open fun setTitleBar() {
        if (null != supportActionBar) {
            mTitleBar.attachActivity(this)
        }
    }
}
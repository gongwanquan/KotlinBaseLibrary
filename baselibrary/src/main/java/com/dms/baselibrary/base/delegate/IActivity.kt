package com.dms.baselibrary.base.delegate

import android.os.Bundle

interface IActivity {
    fun getLayoutRes(): Int

    fun initVar(extras: Bundle)

    fun initView(savedInstanceState: Bundle?)

    fun initData()

    fun useEventBus(): Boolean
}
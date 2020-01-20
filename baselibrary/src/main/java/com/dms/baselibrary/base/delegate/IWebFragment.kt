package com.dms.baselibrary.base.delegate

import android.os.Bundle

interface IWebFragment {

    fun getLayoutRes(): Int

    fun initVar(extras: Bundle)

    fun initView(savedInstanceState: Bundle?)
}
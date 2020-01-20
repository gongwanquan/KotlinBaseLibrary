package com.dms.baselibrary.mvp.view


import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.dms.baselibrary.http.error.NetError

interface IView : LifecycleOwner {

    fun getCtx(): Context?

    fun showLoading()

    fun hideLoading()

    fun showToast(msg: String?)

    fun showError(netError: NetError)
}
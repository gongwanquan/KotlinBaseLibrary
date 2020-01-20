package com.dms.baselibrary.base.activity

import android.content.Context
import com.blankj.utilcode.util.ToastUtils
import com.dms.baselibrary.http.error.NetError
import com.dms.baselibrary.mvp.presenter.IPresenter
import com.dms.baselibrary.mvp.view.IView
import com.dms.baselibrary.widget.LoadingDialog

abstract class BaseMvpActivity<V : IView, P : IPresenter<V>> : BaseTitleActivity(), IView {
    private val mLoadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }

    abstract fun createPresenter(): P

    protected val mPresenter: P by lazy {
        createPresenter().apply {
            this.onAttach(this@BaseMvpActivity as V)
        }
    }

    override fun getCtx(): Context {
        return this
    }

    override fun showToast(msg: String?) {
        ToastUtils.showShort(msg)
    }

    override fun showLoading() {
        mLoadingDialog.show()
    }

    override fun hideLoading() {
        mLoadingDialog.dismiss()
    }

    override fun showError(netError: NetError) {
        showToast(netError.message)
    }
}
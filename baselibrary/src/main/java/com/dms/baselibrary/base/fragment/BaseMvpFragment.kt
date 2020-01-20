package com.dms.baselibrary.base.fragment

import android.content.Context
import com.blankj.utilcode.util.ToastUtils
import com.dms.baselibrary.http.error.NetError
import com.dms.baselibrary.mvp.presenter.IPresenter
import com.dms.baselibrary.mvp.view.IView
import com.dms.baselibrary.widget.LoadingDialog
import kotlin.properties.Delegates

abstract class BaseMvpFragment<V : IView, P : IPresenter<V>> : BaseFragment(), IView {

    private var mContext: Context by Delegates.notNull<Context>()

    private val mLoadingDialog: LoadingDialog? by lazy {
        context?.let {
            LoadingDialog(it)
        }
    }

    abstract fun createPresenter(): P

    protected val mPresenter: P by lazy {
        createPresenter().apply {
            this.onAttach(this@BaseMvpFragment as V)
        }
    }

    override fun getCtx(): Context? {
        return context
    }

    override fun showLoading() {
        mLoadingDialog?.show()
    }

    override fun hideLoading() {
        mLoadingDialog?.dismiss()
    }

    override fun showToast(msg: String?) {
        ToastUtils.showShort(msg)
    }

    override fun showError(netError: NetError) {
        showToast(netError.message)
    }
}
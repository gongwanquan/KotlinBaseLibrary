package com.dms.baselibrary.base.dialog

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.dms.baselibrary.base.delegate.IDialog
import org.greenrobot.eventbus.EventBus

abstract class BaseDialog : DialogFragment(), IDialog {
    private var mRootView: View? = null

    override fun getContext(): Context? {
        return super.getContext()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (null == mRootView) {
            mRootView = inflater.inflate(getLayoutRes(), container, false)
        } else {
            val viewGroup = mRootView?.parent as ViewGroup
            viewGroup.removeView(mRootView)
        }

        arguments?.let { initVar(it) }

        initView(savedInstanceState)

        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    override fun onStart() {
        super.onStart()
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onResume() {
        super.onResume()
        initDialog()
    }

    override fun onStop() {
        super.onStop()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun useEventBus(): Boolean {
        return false
    }

    override fun initDialog() {
        dialog.window?.attributes?.gravity = getGravity()

        dialog.window?.setLayout(
                ScreenUtils.getScreenWidth() - getMarginLeftRight() * 2,
                WindowManager.LayoutParams.WRAP_CONTENT)

        dialog.setCanceledOnTouchOutside(isTouchCancel())
    }

    override fun getGravity(): Int {
        return Gravity.CENTER
    }

    override fun getMarginLeftRight(): Int {
        return SizeUtils.dp2px(15f)
    }

    override fun isTouchCancel(): Boolean {
        return true
    }

}
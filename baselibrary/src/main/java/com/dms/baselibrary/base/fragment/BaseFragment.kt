package com.dms.baselibrary.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dms.baselibrary.base.delegate.IFragment
import org.greenrobot.eventbus.EventBus

abstract class BaseFragment : Fragment(), IFragment {
    private var mRootView: View? = null

    private var mIsViewCreated: Boolean = false

    private var mIsVisibleToUser: Boolean = false

    var mIsDataInitiated: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (null == mRootView) {
            mRootView = inflater.inflate(getLayoutRes(), container, false)
        } else {
            val viewGroup = mRootView?.parent as ViewGroup
            viewGroup.removeView(mRootView)
        }

        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { initVar(it) }
        initView(savedInstanceState)
        initData()

        mIsViewCreated = true
        prepareLoadData()
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mIsVisibleToUser = isVisibleToUser
        prepareLoadData()
    }

    override fun onStart() {
        super.onStart()
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    private fun prepareLoadData() {
        if (mIsViewCreated && mIsVisibleToUser && !mIsDataInitiated) {
            mIsDataInitiated = true
            onLazyLoad()
        }
    }

    override fun onLazyLoad() {

    }

    override fun useEventBus(): Boolean {
        return false
    }
}
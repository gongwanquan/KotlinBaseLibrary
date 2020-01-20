package com.dms.baselibrary.base.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.dms.baselibrary.base.delegate.IActivity
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : AppCompatActivity(), IActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //禁止截屏
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        setContentView(getLayoutRes())
        intent?.extras?.let { initVar(it) }
        initView(savedInstanceState)
        initData()
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

    override fun useEventBus(): Boolean {
        return false
    }


}
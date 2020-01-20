package com.dms.baselibrary.mvp.presenter


import androidx.annotation.UiThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.dms.baselibrary.mvp.view.IView


interface IPresenter<V : IView> : LifecycleObserver {

    @UiThread
    fun onAttach(view: V)

    @UiThread
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDetach()
}
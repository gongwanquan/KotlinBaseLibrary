package com.dms.baselibrary.image

import android.content.Context

interface ILoader<T : Config> {

    fun loadImg(context: Context, config: T)

    fun loadCallBack(context: Context, callback: LoadCallback, config: T)

    fun clear(context: Context, config: T)

}
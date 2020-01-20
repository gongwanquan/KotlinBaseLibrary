package com.dms.baselibrary.image

import android.graphics.drawable.Drawable

interface LoadCallback {
    abstract fun onLoadFailed()

    abstract fun onLoadReady(drawable: Drawable)
}
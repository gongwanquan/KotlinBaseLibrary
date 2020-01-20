package com.dms.baselibrary.image

import android.widget.ImageView

open class Config(var imgSource: Any?,
             var targetView: ImageView,
             var holderImg: Int = 0,
             var errorImg: Int = 0,
             var skipCache: Boolean = false,
             var isCircleImg: Boolean = false,
             var cornerRadius: Int = 0,
             var imgSize: Size? = null)
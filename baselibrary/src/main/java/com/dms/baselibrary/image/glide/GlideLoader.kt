package com.dms.baselibrary.image.glide

import android.content.Context
import android.graphics.drawable.Drawable
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dms.baselibrary.image.Config
import com.dms.baselibrary.image.ILoader
import com.dms.baselibrary.image.LoadCallback

class GlideLoader : ILoader<Config> {

    override fun loadImg(context: Context, config: Config) {
        GlideApp.with(context).load(config.imgSource)
                .apply(getOptions(config))
                .into(config.targetView)
    }

    override fun loadCallBack(context: Context, callback: LoadCallback, config: Config) {
        GlideApp.with(context).load(config.imgSource)
                .apply(getOptions(config))
                .into(object : CustomTarget<Drawable>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        callback.onLoadReady(resource)
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        callback.onLoadFailed()
                    }

                })


    }

    override fun clear(context: Context, config: Config) {
        GlideApp.get(context).clearMemory()
        GlideApp.get(context).clearDiskCache()
    }

    private fun getOptions(config: Config): RequestOptions {
        var options = RequestOptions()
                .skipMemoryCache(config.skipCache)


        if (config.holderImg > 0) {
            options.placeholder(config.holderImg)
        }

        if (config.errorImg > 0) {
            options.error(config.errorImg)
        }

        if (config.isCircleImg) {
            options.transform(CircleCrop())
        } else if (config.cornerRadius > 0) {
            options.transform(RoundedCorners(SizeUtils.dp2px(config.cornerRadius.toFloat())))
        }

        config.imgSize?.let {
            options.override(it.width, it.height)
        }

        return options
    }

}
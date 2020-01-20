package com.dms.baselibrary.base.fragment.x5_web

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler

import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient



class X5WebView @SuppressLint("SetJavaScriptEnabled")
constructor(arg0: Context?, arg1: AttributeSet?) : WebView(arg0, arg1) {

    private val client = object : WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            handler?.proceed()
        }
    }

    init {
        this.webViewClient = client
        initWebViewSettings()
        this.view.isClickable = true
    }

    private fun initWebViewSettings() {
        settings.javaScriptEnabled = true

        // 设置可以支持缩放
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        // 设置不出现缩放工具
        settings.displayZoomControls = false

        //自适应屏幕
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

        //缓存设置
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.setAppCacheEnabled(true)
        settings.setAppCacheMaxSize(java.lang.Long.MAX_VALUE)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
    }

}

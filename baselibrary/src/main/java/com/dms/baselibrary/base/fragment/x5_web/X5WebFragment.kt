package com.dms.baselibrary.base.fragment.x5_web

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.FrameLayout
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.dms.baselibrary.R
import com.tencent.smtt.sdk.*
import com.dms.baselibrary.base.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_x5_web.*
import java.util.HashMap

class X5WebFragment : BaseFragment() {

    private var mLoadPath: String? = null

    private var mSupportZoom: Boolean? = null

    private var mReaderView: TbsReaderView? = null

    private var mWebView: X5WebView? = null

    companion object {
        fun createInstance(path: String?, supportZoom: Boolean = true): X5WebFragment {
            val fragment = X5WebFragment()

            val bundle = Bundle()
            bundle.putString("extra_load_path", path)
            bundle.putBoolean("extra_support_zoom", supportZoom)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onDestroy() {
        mReaderView?.onStop()

        mWebView?.let {
            it.stopLoading()
            it.removeAllViews()
            it.destroy()
            mWebView = null
        }

        super.onDestroy()
    }


    override fun getLayoutRes(): Int {
        return R.layout.fragment_x5_web
    }

    override fun initVar(extras: Bundle) {
        mLoadPath = extras.getString("extra_load_path")
        mSupportZoom = extras.getBoolean("extra_support_zoom")
    }

    override fun initView(savedInstanceState: Bundle?) {

        mLoadPath?.let {
            if (RegexUtils.isURL(it)) {
                openByWebView(it)
            } else if (isPictureFile(it)) {
                openByWebView("file://$it")
            } else if (!openByReaderView(it)) {
                openByFileReader(it)
            }

        }
    }

    override fun initData() {

    }

    private fun isPictureFile(loadPath: String): Boolean {
        if (loadPath.endsWith("jpg")
                || loadPath.endsWith("jpeg")
                || loadPath.endsWith("png")
                || loadPath.endsWith("gif")
                || loadPath.endsWith("bmp")
                || loadPath.endsWith("webp")
                || loadPath.endsWith("svg")) {
            return true
        }

        return false
    }

    private fun openByWebView(loadPath: String) {
        mWebView = X5WebView(context, null)
        if (mSupportZoom == false) {
            mWebView?.settings?.builtInZoomControls = false
            mWebView?.settings?.setSupportZoom(false)
        } else {
            mWebView?.settings?.builtInZoomControls = true
            mWebView?.settings?.setSupportZoom(true)
        }


        mWebView?.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(webView: WebView?, newProgress: Int) {
                super.onProgressChanged(webView, newProgress)
                if (newProgress == 100) {
                    load_progress_pb.visibility = View.GONE
                } else {
                    load_progress_pb.visibility = View.VISIBLE
                    load_progress_pb.progress = newProgress
                }
            }

            override fun onReceivedTitle(webView: WebView?, title: String?) {
                super.onReceivedTitle(webView, title)

            }
        }

        content_layout.addView(mWebView, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT))


        mWebView?.loadUrl(loadPath)
    }

    private fun openByReaderView(loadPath: String): Boolean {
        mReaderView = TbsReaderView(context!!, null)
        val canOpenFile = mReaderView?.preOpen(loadPath.substring(loadPath.lastIndexOf(".") + 1), false)
                ?: false
        if (canOpenFile) {
            content_layout.addView(mReaderView, FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT))

            val bundle = Bundle()
            bundle.putString("filePath", loadPath)
            bundle.putString("tempPath", Environment.getExternalStorageDirectory().path)
            mReaderView?.openFile(bundle)

            return true
        }

        return false
    }

    private fun openByFileReader(loadPath: String) {
        val params = HashMap<String, String>()
        params["local"] = "false"
        QbSdk.getMiniQBVersion(context)
        val openType = QbSdk.openFileReader(context, loadPath, params) { activity!!.finish() }
        if (openType !in 1..3) {
            ToastUtils.showShort(context?.getString(R.string.file_type_cannot_view))
        }
    }

    fun goBack(): Boolean {
        if (mWebView?.canGoBack() == true) {
            mWebView?.goBack()
            return true
        }

        return false
    }
}
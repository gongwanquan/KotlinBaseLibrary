package com.dms.baselibrary.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.blankj.utilcode.util.ScreenUtils
import com.dms.baselibrary.R
import kotlinx.android.synthetic.main.dialog_confirm.*

class ConfirmDialog(context: Context) : Dialog(context) {

    private var mContentTv: TextView? = null

    private var mContentStr: String? = null

    private var mConfirmListener: ConfirmListener? = null

    fun setContentStr(contentStr: String) {
        mContentStr = contentStr
    }

    fun setConfirmListener(confirmListener: ConfirmListener) {
        mConfirmListener = confirmListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm)

        val window = window
        val lp = window!!.attributes
        lp.gravity = Gravity.CENTER
        lp.width = (ScreenUtils.getScreenWidth() * 0.8).toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp

        mContentTv = findViewById(R.id.content_tv)
        confirm_tv.setOnClickListener {
            mConfirmListener?.onConfirm()

            dismiss()
        }
        cancel_tv.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        if (!TextUtils.isEmpty(mContentStr)) {
            mContentTv!!.text = mContentStr
        }
    }


    interface ConfirmListener {
        fun onConfirm()
    }
}
package com.dms.baselibrary.widget


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.dms.baselibrary.R

class LoadingDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)

        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

        setCanceledOnTouchOutside(false)
    }
}
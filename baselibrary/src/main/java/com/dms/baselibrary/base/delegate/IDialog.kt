package com.dms.baselibrary.base.delegate


interface IDialog : IActivity {
    fun initDialog()

    fun getGravity(): Int

    fun getMarginLeftRight(): Int

    fun isTouchCancel(): Boolean
}
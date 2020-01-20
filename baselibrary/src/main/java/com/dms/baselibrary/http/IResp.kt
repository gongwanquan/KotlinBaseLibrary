package com.dms.baselibrary.http

interface IResp<T> {
    fun isSuccess(): Boolean

    fun getCode(): Int

    fun getMessage(): String

    fun getData(): T?
}
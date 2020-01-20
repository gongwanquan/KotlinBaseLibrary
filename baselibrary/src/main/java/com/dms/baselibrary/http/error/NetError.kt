package com.dms.baselibrary.http.error



class NetError(override val message: String? = "", override val cause: Throwable?, val type: ErrorType) : Throwable(message, cause)
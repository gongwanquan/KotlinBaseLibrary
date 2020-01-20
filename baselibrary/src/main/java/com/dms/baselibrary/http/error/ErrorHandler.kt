package com.dms.baselibrary.http.error

import android.content.Context
import com.dms.baselibrary.R
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorHandler {

    fun handle(context: Context?, t: Throwable): NetError {
        if (t is NetError) {
            return t

        } else if (t is ConnectException || t is SocketTimeoutException || t is UnknownHostException) {
            return NetError(context?.getString(R.string.error_type_connect), t, ErrorType.CONNECT)

        } else if (t is JsonParseException || t is MalformedJsonException) {
            return NetError(context?.getString(R.string.error_type_data), t, ErrorType.DATA_PARSE)

        } else if (t is HttpException) {

            return when (t.code()) {
                401 -> NetError(context?.getString(R.string.error_type_login), t, ErrorType.NOT_LOGIN)

                403 -> NetError(context?.getString(R.string.error_type_auth), t, ErrorType.AUTH)

                else -> NetError(context?.getString(R.string.error_type_server), t, ErrorType.SERVER)
            }
        }

        return NetError(context?.getString(R.string.error_type_other), t, ErrorType.OTHER)
    }
}
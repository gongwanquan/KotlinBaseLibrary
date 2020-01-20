package com.dms.baselibrary.http


import me.jessyan.progressmanager.ProgressManager
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {
        val CONFIG = Config()

        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiClient()
        }
    }

    val mOkHttpClient: OkHttpClient =
            OkHttpClient().newBuilder().apply {

                connectTimeout(CONFIG.connectTimeout, TimeUnit.MILLISECONDS)
                readTimeout(CONFIG.readTimeout, TimeUnit.MILLISECONDS)
                writeTimeout(CONFIG.writeTimeout, TimeUnit.MILLISECONDS)

                CONFIG.logger?.let {
                    val logInterceptor = HttpLoggingInterceptor(it)
                    logInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(logInterceptor)
                }

                CONFIG.interceptors?.let {
                    for (interceptor in it) {
                        addInterceptor(interceptor)
                    }
                }

                CONFIG.cookieJar?.let {
                    cookieJar(it)
                }

                if (CONFIG.multiUrlEnable) {
                    RetrofitUrlManager.getInstance().with(this)
                }

                if (CONFIG.progressEnable) {
                    ProgressManager.getInstance().with(this)
                }

                CONFIG.clientBuildListener?.onClientBuild(this)

            }.build()


    val mRetrofit: Retrofit = Retrofit.Builder().apply {
        CONFIG.baseUrl?.let {
            baseUrl(it)
        }
        client(mOkHttpClient)
        addConverterFactory(GsonConverterFactory.create())
    }.build()


    class Config {
        var baseUrl: String? = null

        var connectTimeout: Long = 10_000

        var readTimeout: Long = 30_000

        var writeTimeout: Long = 30_000

        var logger: HttpLoggingInterceptor.Logger? = null

        var interceptors: List<Interceptor>? = null

        var cookieJar: CookieJar? = null

        var multiUrlEnable: Boolean = false

        var progressEnable: Boolean = false

        var clientBuildListener: HttpClientBuildListener? = null
    }

    interface HttpClientBuildListener {
        fun onClientBuild(builder: OkHttpClient.Builder)
    }
}
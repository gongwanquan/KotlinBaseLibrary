package com.dms.baselibrary.mvp.presenter


import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.PermissionUtils
import com.dms.baselibrary.http.error.ErrorType
import com.dms.baselibrary.http.IResp
import com.dms.baselibrary.http.error.ErrorHandler
import com.dms.baselibrary.http.error.NetError
import com.dms.baselibrary.mvp.view.IView
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.dms.baselibrary.R


open class BasePresenter<V : IView> : IPresenter<V>, CoroutineScope by MainScope() {

    lateinit var mView: V

    override fun onAttach(view: V) {
        mView = view
        mView.lifecycle.addObserver(this)
    }

    override fun onDetach() {
        cancel()
    }

    fun <T : IResp<*>> doApi(apiBlock: suspend () -> T,
                             dataBlock: (data: T) -> Unit,
                             errorBlock: (error: NetError) -> Boolean = { false },
                             showLoading: Boolean = true) {
        launchByTryCatch(
                {
                    if (showLoading) {
                        mView.showLoading()
                    }

                    val response = apiBlock()
                    if (response.isSuccess()) {
                        dataBlock(response)
                    } else {
                        throw NetError(response.getMessage(), null, ErrorType.BUSINESS)
                    }
                },
                {
                    val error = ErrorHandler.handle(mView.getCtx(), it)

                    if (!errorBlock(error)) {
                        mView.showError(error)
                    }
                },
                {
                    if (showLoading) {
                        mView.hideLoading()
                    }
                }
        )
    }

    fun downloadFile(apiBlock: suspend () -> Response<ResponseBody>, destDir: String, dataBlock: (file: File) -> Unit, errorBlock: (error: NetError) -> Boolean = { false }) {
        launchByTryCatch(
                {
                    mView.showLoading()

                    val isGranted = requestPermission(PermissionConstants.STORAGE)
                    if (isGranted) {
                        val response = apiBlock()
                        val hasFile = response.headers().get("hasFile")?.toBoolean() ?: true
                        if (!hasFile) {
                            mView.showToast(mView.getCtx()?.getString(R.string.file_not_exist))
                        } else {
                            val destFile = saveFile(destDir, response)
                            dataBlock(destFile)
                        }
                    }
                },
                {
                    var error: NetError = ErrorHandler.handle(mView.getCtx(), it)

                    if (!errorBlock(error)) {
                        mView.showError(error)
                    }
                },
                {
                    mView.hideLoading()
                }
        )
    }

    private suspend fun requestPermission(vararg permissions: String): Boolean =
            suspendCoroutine {
                PermissionUtils.permission(*permissions)
                        .callback(object : PermissionUtils.SimpleCallback {
                            override fun onGranted() {
                                it.resume(true)
                            }

                            override fun onDenied() {
                                it.resume(false)
                            }

                        }).request()
            }

    private suspend fun saveFile(destDir: String, response: Response<ResponseBody>): File {
        return async(Dispatchers.IO) {
            val dir = File(destDir)
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val contentDisposition = response.headers().get("Content-disposition")?.trim()

            val fileName = contentDisposition
                    ?.replace("attachment", "")
                    ?.replace("filename=", "")
                    ?.replace(";", "")
                    ?.replace("\"", "")
                    ?.trim()
            val targetFile = File(destDir, fileName)

            if (FileIOUtils.writeFileFromIS(targetFile, response.body()?.byteStream())) {
                return@async targetFile
            } else {
                throw IOException()
            }
        }.await()
    }


    private fun launchByTryCatch(tryBlock: suspend () -> Unit,
                                 catchBlock: suspend (t: Throwable) -> Unit,
                                 finallyBlock: suspend () -> Unit) {
        launch {
            try {
                tryBlock()
            } catch (t: Throwable) {
                catchBlock(t)
            } finally {
                finallyBlock()
            }
        }
    }


}
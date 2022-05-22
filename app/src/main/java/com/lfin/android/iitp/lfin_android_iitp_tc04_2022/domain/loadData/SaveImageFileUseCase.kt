package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.loadData

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCase
import kotlinx.coroutines.Dispatchers
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

/**
 * Server로부터 QueryPlan을 다운받아 DB에 저장
 */
class SaveImageFileUseCase @Inject constructor(
) : UseCase<SaveImageFileUseCase.Param, Boolean>(Dispatchers.IO) {
    data class Param(
        val body: ResponseBody?,
        val imgPath: File,
    )

    override suspend fun execute(param: Param): Boolean {
        if (param.body == null)
            return false
        try {
            Log.d("DownloadImage", "Reading and writing file")
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null

            try {

                Log.d(SetImageFileUseCase.TAG, "이미지 파일 저장경로: $param.imgPath")
                inputStream = param.body.byteStream()
                var bitmap = BitmapFactory.decodeStream(inputStream)
                outputStream = FileOutputStream(param.imgPath)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                Log.d(SetImageFileUseCase.TAG, "saveBitmap End")
            } catch (e: IOException) {
                Log.d("saveFile", e.toString())
                return false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
            return true
        } catch (e: IOException) {
            Log.d("saveFile", e.toString())
            return false
        }
        return true
    }

}
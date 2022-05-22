package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.loadData

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.ImageFileEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.ImageFileRepository
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class SetImageFileUseCase  @Inject constructor(
    private val imageFileRepository: ImageFileRepository
){
    companion object {
        val TAG: String = SetImageFileUseCase::class.java.simpleName
    }
    suspend fun setImageFileList(entityList: List<ImageFileEntity>){
        imageFileRepository.insertAllImageFile(entityList)
    }

    /**
     * 이미지를 내부저장소에 저장
     */
    fun saveImageFile(body: ResponseBody?, imgPath: File): Boolean {
        if (body == null)
            return false
        try {
            Log.d("DownloadImage", "Reading and writing file")
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null

            try {

                Log.d(TAG, "이미지 파일 저장경로: $imgPath")
                inputStream = body.byteStream()
                var bitmap = BitmapFactory.decodeStream(inputStream)
                outputStream = FileOutputStream(imgPath)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                Log.d(TAG, "saveBitmap End")
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
    }
}
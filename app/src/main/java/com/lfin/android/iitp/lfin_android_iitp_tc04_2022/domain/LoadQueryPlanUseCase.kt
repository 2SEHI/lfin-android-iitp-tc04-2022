package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.ImageFileEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.ImageFileRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadQueryPlanUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val queryPlanRepository: QueryPlanRepository,
    private val imageFileRepository: ImageFileRepository
) {
    companion object {
        val TAG: String = LoadQueryPlanUseCase::class.java.simpleName
    }


    // 1. QueryPlan 가져오기
    // 2. 이미지 저장 중
    suspend fun loadTestData() {
        // QueryPlan 모두 가져오기
        queryPlanRepository.insertAllQueryPlan()

        // query_plan 테이블의 파일명을 image_file 테이블에 저장
        val baseFileList = queryPlanRepository.getAllBaseFile()
        Log.d(TAG, "baseFileList : ${baseFileList.size}")
        val baseEntityList: List<ImageFileEntity> = stringToImageFileEntity(baseFileList)
        imageFileRepository.insertAllImageFile(baseEntityList)

        val queryFileList = queryPlanRepository.getAllQueryFile()
        Log.d(TAG, "queryFileList : ${queryFileList.size}")
        val queryEntityList: List<ImageFileEntity> = stringToImageFileEntity(queryFileList)
        imageFileRepository.insertAllImageFile(queryEntityList)

        // 이미지 파일 디바이스에 저장
        downloadImageFile()
    }

    private fun stringToImageFileEntity(fileNameList: List<String>): ArrayList<ImageFileEntity> {
        var oList: ArrayList<ImageFileEntity> = arrayListOf()
        for (fileName in fileNameList) {
            oList.add(ImageFileEntity(fileName))
        }
        return oList
    }

    suspend fun downloadImageFile(): Boolean {
        var result = true
        val imgDir = File(context.filesDir.toString() + File.separator + Constants.IMAGE_DIR)
        // 이미지 저장 디렉토리가 없는 경우 디렉토리 생성
        if (!imgDir.exists()) {
            imgDir.mkdirs()
        }
        val imageFileList = imageFileRepository.getAllImageFile()
        var responseBody: ResponseBody? = null
        for (imageFile in imageFileList) {
            Log.d(TAG, "imageFileName : ${imageFile}")
            val imgPath = File(imgDir, imageFile)
            // 동일한 이미지 파일이 존재하는 경우 삭제
            if (imgPath.exists()) {
                imgPath.delete()
            }
            try {
                responseBody = imageFileRepository.loadImageFile(imageFile)
                // 서버에서 다운받은 이미지를 디바이스에 저장
                result = saveImageFile(responseBody, imgPath)

            } catch (e: Exception) {
                Log.d("onResponse", "There is an error")
                e.printStackTrace()
                return false
            }
        }
        return result
    }


    /**
     * 이미지를 내부저장소에 저장
     */
    private fun saveImageFile(body: ResponseBody?, imgPath: File): Boolean {
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
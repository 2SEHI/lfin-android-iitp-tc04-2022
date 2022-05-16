package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain

import android.content.Context
import android.util.Log
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.ImageFileEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadMetaDataUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getQueryPlanUseCase: GetQueryPlanUseCase,
    private val setQueryPlanUseCase: SetQueryPlanUseCase,
    private val getImageFileUseCase: GetImageFileUseCase,
    private val setImageFileUseCase: SetImageFileUseCase,
) {
    companion object {
        val TAG: String = LoadMetaDataUseCase::class.java.simpleName
    }

    // 1. QueryPlan 가져오기
    // 2. 이미지 저장 중
    fun loadMetaData() = flow {
        // QueryPlan 서버에서 가져온 후 DB저장
        setQueryPlanUseCase.setQueryPlan()
        emit("base 이미지 파일 목록 가져오는 중")

        val baseEntityList: List<ImageFileEntity> =
            stringToImageFileEntity(getQueryPlanUseCase.baseFileList)
        setImageFileUseCase.setImageFileList(baseEntityList)

        emit("query 이미지 파일 목록 가져오는 중")
        val queryEntityList: List<ImageFileEntity> =
            stringToImageFileEntity(getQueryPlanUseCase.queryFileList)
        setImageFileUseCase.setImageFileList(queryEntityList)

        emit("이미지 파일 저장 중")
        Log.d(TAG, "이미지 파일 목록 개수: ${getImageFileUseCase.imageFileList}")
        downloadImageFile(getImageFileUseCase.imageFileList)
    }

    private fun stringToImageFileEntity(fileNameList: List<String>): ArrayList<ImageFileEntity> {
        var oList: ArrayList<ImageFileEntity> = arrayListOf()
        for (fileName in fileNameList) {
            oList.add(ImageFileEntity(fileName))
        }
        return oList
    }

    private suspend fun downloadImageFile(imageFileList: List<String>) {

        var result = true
        val imgDir = File("${context.filesDir}${File.separator}${Constants.IMAGE_DIR}")
        // 이미지 저장 디렉토리가 없는 경우 디렉토리 생성
        if (!imgDir.exists()) {
            Log.d(TAG, "이미지 디렉토리 생성: $imgDir")
            imgDir.mkdirs()
        }
        var responseBody: ResponseBody? = null
        for (imageFile in imageFileList) {
            Log.d(TAG, "imageFileName : $imageFile")
            val imgPath = File(imgDir, imageFile)
            // 동일한 이미지 파일이 존재하는 경우 삭제
            if (imgPath.exists()) {
                imgPath.delete()
            }
            try {
                responseBody = getImageFileUseCase.loadBitmap(imageFile)
                // 서버에서 다운받은 이미지를 디바이스에 저장
                result = setImageFileUseCase.saveImageFile(responseBody, imgPath)

            } catch (e: Exception) {
                Log.d("onResponse", "There is an error")
                e.printStackTrace()
//                return false
            }
        }
//        return result
    }

}
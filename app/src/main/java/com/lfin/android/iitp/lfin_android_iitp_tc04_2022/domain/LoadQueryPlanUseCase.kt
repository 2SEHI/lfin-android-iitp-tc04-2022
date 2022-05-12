package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain

import android.util.Log
import retrofit2.Retrofit
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.api.NetworkApi
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.ImageFileEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.ImageFileRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepositoryImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadQueryPlanUseCase @Inject constructor(
    private val queryPlanRepository: QueryPlanRepository,
    private val imageFileRepository: ImageFileRepository
) {
    companion object {
        val TAG: String = LoadQueryPlanUseCase::class.java.simpleName
    }

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
        getAllImageFile()
    }

    // image_file 테이블에서 파일전체검색
    fun getAllImageFile() {
        Log.d(TAG, "getAllImageFile()")
        val imageFileList = imageFileRepository.getAllImageFile()
        for (imageFile in imageFileList) {
            Log.d(TAG, "imageFileName : ${imageFile}")
        }
    }

    fun stringToImageFileEntity(fileNameList: List<String>): ArrayList<ImageFileEntity> {
        var oList: ArrayList<ImageFileEntity> = arrayListOf()
        for (fileName in fileNameList) {
            oList.add(ImageFileEntity(fileName))
        }
        return oList
    }
}
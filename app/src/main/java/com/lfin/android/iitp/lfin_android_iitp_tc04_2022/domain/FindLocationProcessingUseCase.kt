package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.OpenCVAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.decodeBase64
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FindLocationProcessingUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getQueryPlanUseCase: GetQueryPlanUseCase,
) : UseCase<FindLocationProcessingUseCase.Param, Boolean>(Dispatchers.IO) {
    data class Param(
        val queryPlanEntity: QueryPlanEntity
    )

    companion object {
        val TAG: String = FindLocationProcessingUseCase::class.java.simpleName
    }

    override suspend fun execute(param: Param): Boolean {
        val imgDir = File("${context.filesDir}${File.separator}${Constants.IMAGE_DIR}")
        val bImagePath = "$imgDir${File.separator}${param.queryPlanEntity.b_file_name}"
        val qImagePath = "$imgDir${File.separator}${param.queryPlanEntity.q_file_name}"

        val baseBitmap =
            BitmapFactory.decodeFile(bImagePath)

        val queryBitmap =
            BitmapFactory.decodeFile(qImagePath)

        OpenCVAdapter.initializeModule()
        // 비교대상 이미지
        OpenCVAdapter.putBitmap(baseBitmap)
        // 입력 이미지
        OpenCVAdapter.putBitmap(queryBitmap)
        // 비교대상의 위치정보
        val metaData = decodeBase64(param.queryPlanEntity.metadata)
        // 위치정보 입력
        OpenCVAdapter.putMetadata(metaData)
        // 프로세싱
        OpenCVAdapter.imageProcessing()

        val res = OpenCVAdapter.getPtrOfString(1)

        Log.d(TAG, "${OpenCVAdapter.getPtrOfString(1)}")

        return true
    }
}
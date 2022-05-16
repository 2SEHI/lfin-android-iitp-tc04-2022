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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetQueryPlanUseCase @Inject constructor(
    private val queryPlanRepository: QueryPlanRepository,
) {
    companion object {
        val TAG: String = SetQueryPlanUseCase::class.java.simpleName
    }
    suspend fun setQueryPlan() {
        queryPlanRepository.insertAllQueryPlan()
    }
}
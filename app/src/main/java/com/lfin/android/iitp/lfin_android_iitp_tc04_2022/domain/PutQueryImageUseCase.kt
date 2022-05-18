package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.OpenCVAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.ImageFileEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.ImageFileRepository
import kotlinx.coroutines.Dispatchers
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PutQueryImageUseCase  @Inject constructor(
) : UseCase<PutQueryImageUseCase.Param, String>(Dispatchers.IO) {
    data class Param(
        val queryBitmap: Bitmap
    )
    companion object {
        val TAG: String = PutQueryImageUseCase::class.java.simpleName
    }

    override suspend fun execute(param: Param): String {
        OpenCVAdapter.putBitmap(param.queryBitmap)
        return OpenCVAdapter.getPtrOfString(1)
    }

}
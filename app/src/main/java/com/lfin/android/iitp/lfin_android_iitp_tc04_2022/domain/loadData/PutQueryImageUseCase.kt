package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.loadData

import android.graphics.Bitmap
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.OpenCVAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * OpenCV모듈에 metadata 넣기
 */
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
        return OpenCVAdapter.getPtrOfString(0)
    }

}
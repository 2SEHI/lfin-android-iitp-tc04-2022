package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.startTest

import android.graphics.Bitmap
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.OpenCVAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class PutBaseImageUseCase @Inject constructor(
) : UseCase<PutBaseImageUseCase.Param, String>(Dispatchers.IO) {
    data class Param(
        val baseBitmap: Bitmap
    )

    companion object {
        val TAG: String = PutBaseImageUseCase::class.java.simpleName
    }

    override suspend fun execute(param: Param): String {
        OpenCVAdapter.putBitmap(param.baseBitmap)
        return OpenCVAdapter.getPtrOfString(0)
    }

}
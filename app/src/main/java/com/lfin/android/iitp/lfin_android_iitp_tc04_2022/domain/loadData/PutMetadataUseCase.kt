package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.loadData

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.OpenCVAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * OpenCV모듈에 metadata 넣기
 */
class PutMetadataUseCase @Inject constructor(
) : UseCase<PutMetadataUseCase.Param, String>(Dispatchers.IO) {
    data class Param(
        val metadata: ByteArray
    )

    override suspend fun execute(param: Param): String {
        OpenCVAdapter.putMetadata(param.metadata)
        // status출력
        return OpenCVAdapter.getPtrOfString(0)
    }

}
package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.loadData

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.OpenCVAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class PutMetadataUseCase @Inject constructor(
) : UseCase<PutMetadataUseCase.Param, String>(Dispatchers.IO) {
    data class Param(
        val metadata: ByteArray
    )

    companion object {
        val TAG: String = PutMetadataUseCase::class.java.simpleName
    }

    override suspend fun execute(param: Param): String {
        OpenCVAdapter.putMetadata(param.metadata)
        return OpenCVAdapter.getPtrOfString(1)
    }

}
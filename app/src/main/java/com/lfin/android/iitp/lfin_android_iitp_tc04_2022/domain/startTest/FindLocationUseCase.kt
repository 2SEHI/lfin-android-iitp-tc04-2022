package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.startTest

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.OpenCVAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCaseNonParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class FindLocationUseCase  @Inject constructor(
) : UseCaseNonParam<String>(Dispatchers.IO) {

    override suspend fun execute(): String {
        OpenCVAdapter.imageProcessing()
        return OpenCVAdapter.getPtrOfString(1)
    }

}
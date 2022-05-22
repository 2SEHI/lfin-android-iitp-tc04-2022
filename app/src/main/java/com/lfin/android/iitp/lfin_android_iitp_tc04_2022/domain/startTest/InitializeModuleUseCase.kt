package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.startTest

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.OpenCVAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCaseNonParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class InitializeModuleUseCase @Inject constructor(
) : UseCaseNonParam<Boolean>(Dispatchers.IO) {

    override suspend fun execute(): Boolean {
        return OpenCVAdapter.initializeModule() >= 0
    }

}
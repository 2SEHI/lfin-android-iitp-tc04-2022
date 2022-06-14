package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.resetDatabase

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.ImageFileRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCaseNonParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ResetMetadataUseCase @Inject constructor(
    private val imageFileRepository: ImageFileRepository
) : UseCaseNonParam<Boolean>(Dispatchers.IO) {

    override suspend fun execute(): Boolean {
        imageFileRepository.deleteAllImageFile()
        return true
    }

}
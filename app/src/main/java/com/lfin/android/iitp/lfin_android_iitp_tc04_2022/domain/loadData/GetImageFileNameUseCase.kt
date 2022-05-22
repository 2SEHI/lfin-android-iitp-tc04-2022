package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.loadData

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.ImageFileRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCaseNonParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetImageFileNameUseCase @Inject constructor(
    private val imageFileRepository: ImageFileRepository
) : UseCaseNonParam<List<String>>(Dispatchers.IO) {

    override suspend fun execute(): List<String> {
        return imageFileRepository.getAllImageFile()
    }
}
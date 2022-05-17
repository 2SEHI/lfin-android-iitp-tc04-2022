package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.ImageFileRepository
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetImageFileUseCase  @Inject constructor(
    private val imageFileRepository: ImageFileRepository
){
    companion object {
        val TAG: String = GetImageFileUseCase::class.java.simpleName
    }
    fun imageFileList() = imageFileRepository.getAllImageFile()
    suspend fun loadBitmap(fileName: String ) : ResponseBody? = imageFileRepository.loadImageFile(fileName)
}
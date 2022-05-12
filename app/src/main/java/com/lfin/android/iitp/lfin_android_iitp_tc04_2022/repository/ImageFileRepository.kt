package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.ImageFileEntity
import okhttp3.ResponseBody

interface ImageFileRepository {

    fun getAllImageFile(): List<String>

    suspend fun insertAllImageFile(imageFileList: List<ImageFileEntity>)

    suspend fun loadImageFile(fileName: String): ResponseBody?
}
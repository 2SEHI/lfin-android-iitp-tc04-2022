package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.api.NetworkApi
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.ImageFileEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dataSource.ImageFileLocalDataSource
import okhttp3.ResponseBody
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageFileRepositoryImpl @Inject constructor(
    retrofit: Retrofit,
    private val imageFileLocalDataSource: ImageFileLocalDataSource
) : ImageFileRepository {

    private val networkApi by lazy { retrofit.create(NetworkApi::class.java) }

    override fun getAllImageFile(): List<String> = imageFileLocalDataSource.selectAll()

    override suspend fun insertAllImageFile(fileNameList: List<ImageFileEntity>) {
        imageFileLocalDataSource.insertAll(fileNameList)
    }

    override suspend fun loadImageFile(fileName: String): ResponseBody? {
        return networkApi.downloadImage(fileName).body()
    }

    override fun deleteAllImageFile() {
        imageFileLocalDataSource.deleteAll()
    }
}
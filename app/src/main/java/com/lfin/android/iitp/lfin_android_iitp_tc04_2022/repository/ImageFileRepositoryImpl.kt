package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.api.NetworkApi
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dao.ImageFileDao
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.ImageFileEntity
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageFileRepositoryImpl @Inject constructor(
    retrofit: Retrofit,
    private val dao: ImageFileDao
) : ImageFileRepository {
    companion object {
        val TAG: String = ImageFileRepositoryImpl::class.java.simpleName
    }

    private val networkApi by lazy { retrofit.create(NetworkApi::class.java) }

    override fun getAllImageFile(): List<String> = dao.selectAll()

    override suspend fun insertAllImageFile(fileNameList: List<ImageFileEntity>) {
        dao.insertAll(fileNameList)
    }

    // TODO 이미지 서버에서 가져오기
}
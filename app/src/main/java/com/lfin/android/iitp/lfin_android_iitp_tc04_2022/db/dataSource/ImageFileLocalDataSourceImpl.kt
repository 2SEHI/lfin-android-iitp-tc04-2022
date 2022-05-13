package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dataSource

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dao.ImageFileDao
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.ImageFileEntity
import javax.inject.Inject

class ImageFileLocalDataSourceImpl @Inject constructor(
    private val dao: ImageFileDao,
): ImageFileLocalDataSource{
    
    override fun selectAll(): List<String> = dao.selectAll()

    override fun insertAll(value: List<ImageFileEntity>) {
        dao.insertAll(value)
    }
}
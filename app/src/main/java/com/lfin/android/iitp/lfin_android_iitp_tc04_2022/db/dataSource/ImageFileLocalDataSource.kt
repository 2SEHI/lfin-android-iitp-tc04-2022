package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dataSource

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.ImageFileEntity

interface ImageFileLocalDataSource {

    fun selectAll(): List<String>

    fun insertAll(value: List<ImageFileEntity>)

}
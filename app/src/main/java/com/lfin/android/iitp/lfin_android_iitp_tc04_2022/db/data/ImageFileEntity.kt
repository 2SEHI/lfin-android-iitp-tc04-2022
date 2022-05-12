package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_file")
data class ImageFileEntity(

    @PrimaryKey
    var file_name: String
)
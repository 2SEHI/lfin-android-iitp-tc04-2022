package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.ImageFileEntity

@Dao
interface ImageFileDao {

    @Query("SELECT file_name FROM image_file")
    fun selectAll(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(value: List<ImageFileEntity>)
}
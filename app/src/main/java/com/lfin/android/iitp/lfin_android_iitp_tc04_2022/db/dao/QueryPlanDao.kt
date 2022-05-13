package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity

@Dao
interface QueryPlanDao {

    @Query("SELECT * FROM query_plan")
    fun selectAll(): List<QueryPlanEntity>

    @Query("SELECT b_file_name FROM query_plan")
    fun selectBaseFile(): List<String>

    @Query("SELECT q_file_name FROM query_plan")
    fun selectQueryFile(): List<String>

    @Insert
    fun insertAll(items: List<QueryPlanEntity>)

    @Query("DELETE FROM query_plan")
    fun deleteAll()
}
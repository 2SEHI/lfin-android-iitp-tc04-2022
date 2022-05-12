package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity

@Dao
interface QueryPlanDao {
    @Query("SELECT * FROM query_plan")
    fun selectAll(): List<QueryPlanEntity>

    @Insert
    fun insertAll(items: List<QueryPlanEntity>)
}
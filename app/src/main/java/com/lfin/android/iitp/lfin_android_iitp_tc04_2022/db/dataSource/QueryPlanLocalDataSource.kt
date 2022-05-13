package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dataSource

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity

interface QueryPlanLocalDataSource {

    fun selectAll(): List<QueryPlanEntity>

    fun selectBaseFile(): List<String>

    fun selectQueryFile(): List<String>

    fun insertAll(items: List<QueryPlanEntity>)

    fun deleteAll()

}
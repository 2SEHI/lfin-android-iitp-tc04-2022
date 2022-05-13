package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity

interface QueryPlanRepository {

    // QueryPlan 전체검색
    fun getAllQueryPlan(): List<QueryPlanEntity>

    // base file name 전체검색
    fun getAllBaseFile(): List<String>

    // query file name 전체검색
    fun getAllQueryFile(): List<String>

    // queryPlan 전체삽입
    suspend fun insertAllQueryPlan()

    fun deleteAllQueryPlan()
}
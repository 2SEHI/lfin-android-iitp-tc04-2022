package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity

interface QueryPlanRepository {
    fun getAllQueryPlan() : List<QueryPlanEntity>
    suspend fun insertAllQueryPlan()
}
package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetQueryPlanUseCase  @Inject constructor(
    private val queryPlanRepository: QueryPlanRepository
) {

    fun getQueryPlanList() : List<QueryPlanEntity> = queryPlanRepository.getAllQueryPlan()

}
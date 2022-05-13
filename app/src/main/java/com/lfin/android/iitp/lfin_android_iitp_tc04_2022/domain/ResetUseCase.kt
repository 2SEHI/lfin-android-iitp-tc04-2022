package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResetUseCase @Inject constructor(
    private val queryPlanRepository: QueryPlanRepository,
) {

    fun deleteQueryPlan() {
        queryPlanRepository.deleteAllQueryPlan()
    }

}
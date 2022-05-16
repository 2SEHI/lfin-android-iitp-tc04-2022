package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResetUseCase @Inject constructor(
    private val queryPlanRepository: QueryPlanRepository,
) {
    companion object {
        val TAG: String = ResetUseCase::class.java.simpleName
    }
    fun deleteQueryPlan() {
        queryPlanRepository.deleteAllQueryPlan()
    }

}
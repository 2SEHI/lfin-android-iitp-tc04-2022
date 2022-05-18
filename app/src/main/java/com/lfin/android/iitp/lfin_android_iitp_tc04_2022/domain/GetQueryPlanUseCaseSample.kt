package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetQueryPlanUseCaseSample @Inject constructor(
    private val queryPlanRepository: QueryPlanRepository
) : UseCaseNonParam<List<QueryPlanEntity>>(Dispatchers.IO) {

    companion object {
        val TAG: String = GetQueryPlanUseCaseSample::class.java.simpleName
    }

    override suspend fun execute(): List<QueryPlanEntity> {
        return queryPlanRepository.getAllQueryPlan()
    }
}
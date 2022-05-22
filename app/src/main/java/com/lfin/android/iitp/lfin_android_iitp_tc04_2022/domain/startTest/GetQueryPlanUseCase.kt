package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.startTest

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCaseNonParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetQueryPlanUseCase @Inject constructor(
    private val queryPlanRepository: QueryPlanRepository
) : UseCaseNonParam<List<QueryPlanEntity>>(Dispatchers.IO) {

    override suspend fun execute(): List<QueryPlanEntity> {
        return queryPlanRepository.getAllQueryPlan()
    }
}
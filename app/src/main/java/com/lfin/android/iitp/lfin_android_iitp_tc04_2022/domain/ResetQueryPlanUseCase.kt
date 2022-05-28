package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCaseNonParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ResetQueryPlanUseCase @Inject constructor(
    private val queryPlanRepository: QueryPlanRepository,
) : UseCaseNonParam<Boolean>(Dispatchers.IO) {

    override suspend fun execute(): Boolean {
        queryPlanRepository.deleteAllQueryPlan()
        return true
    }

}
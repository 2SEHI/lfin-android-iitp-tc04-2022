package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.loadData

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCaseNonParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Server로부터 QueryPlan을 다운받아 DB에 저장
 */
class DownloadQueryPlanUseCase @Inject constructor(
    private val queryPlanRepository: QueryPlanRepository,
) : UseCaseNonParam<Boolean>(Dispatchers.IO) {

    override suspend fun execute() : Boolean {
        return queryPlanRepository.insertAllQueryPlan()
    }

}
package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain
import retrofit2.Retrofit
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.api.NetworkApi
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadQueryPlanUseCase  @Inject constructor(
    private val itemRepository: QueryPlanRepository
) {
    suspend fun insertText() {
        itemRepository.insertAllQueryPlan()
    }
}
package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain

import android.content.Context
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.ImageFileRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestStartUseCase @Inject constructor(
) {
    companion object {
        val TAG: String = LoadQueryPlanUseCase::class.java.simpleName
    }


}
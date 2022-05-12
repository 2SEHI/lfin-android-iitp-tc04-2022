package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository

import android.util.Log
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.api.NetworkApi
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dao.QueryPlanDao
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueryPlanRepositoryImpl @Inject constructor(
    retrofit: Retrofit,
    private val dao: QueryPlanDao
) : QueryPlanRepository {
    companion object {
        val TAG: String = QueryPlanRepositoryImpl::class.java.simpleName
    }

    private val networkApi by lazy { retrofit.create(NetworkApi::class.java) }

    override fun getAllQueryPlan(): List<QueryPlanEntity> = dao.selectAll()

    override suspend fun insertAllQueryPlan() {
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            Log.d(TAG, "insertItem2 시작")
            try {
                // 이미지 메타데이터 가져와서 DB에 저장
                networkApi.loadQueryPlanInfo(
                    "Item.json"
                ).items.map {
                    QueryPlanEntity(
                        it.title,
                        it.description
                    )
                }.apply {
                    dao.insertAll(this)
                    Log.d(TAG, "데이터 저장 개수: ${this.size}")
                }
            } catch (e: Exception) {
                Log.d(TAG, "${e.printStackTrace()}")
            }
        }
    }

}
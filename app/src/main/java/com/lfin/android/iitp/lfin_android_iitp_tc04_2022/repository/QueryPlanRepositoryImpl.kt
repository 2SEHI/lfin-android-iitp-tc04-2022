package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository

import android.util.Log
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.api.NetworkApi
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dataSource.QueryPlanLocalDataSource
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueryPlanRepositoryImpl @Inject constructor(
    retrofit: Retrofit,
    private val queryPlanLocalDataSource: QueryPlanLocalDataSource
) : QueryPlanRepository {

    companion object {
        val TAG: String = QueryPlanRepositoryImpl::class.java.simpleName
    }

    private val networkApi by lazy { retrofit.create(NetworkApi::class.java) }

    override fun getAllQueryPlan(): List<QueryPlanEntity> = queryPlanLocalDataSource.selectAll()

    override fun getAllBaseFile(): List<String> = queryPlanLocalDataSource.selectBaseFile()

    override fun getAllQueryFile(): List<String> = queryPlanLocalDataSource.selectQueryFile()

    override suspend fun insertAllQueryPlan(): Boolean{

        Log.d(TAG, "insertItem2 시작")
        try {
            // 이미지 메타데이터 가져와서 DB에 저장
            networkApi.loadQueryPlanInfo(
                Constants.QUERY_PLAN
            ).items.map {
                QueryPlanEntity(
                    it.b_file_name,
                    it.q_file_name,
                    it.metadata
                )
            }.apply {
                queryPlanLocalDataSource.insertAll(this)
                Log.d(TAG, "데이터 저장 개수: ${queryPlanLocalDataSource.selectAll().size}")
            }
            return true
        } catch (e: Exception) {
            Log.d(TAG, "${e.printStackTrace()}")
            return false
        }
        return true
    }

    override fun deleteAllQueryPlan() {
        queryPlanLocalDataSource.deleteAll()
    }

}
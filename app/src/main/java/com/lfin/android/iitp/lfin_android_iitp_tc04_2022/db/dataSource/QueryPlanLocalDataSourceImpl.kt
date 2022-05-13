package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dataSource

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dao.QueryPlanDao
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity
import javax.inject.Inject

class QueryPlanLocalDataSourceImpl @Inject constructor(
    private val dao: QueryPlanDao,
): QueryPlanLocalDataSource{

    override fun selectAll(): List<QueryPlanEntity> = dao.selectAll()

    override fun selectBaseFile(): List<String> = dao.selectBaseFile()

    override fun selectQueryFile(): List<String>  = dao.selectQueryFile()

    override fun insertAll(items: List<QueryPlanEntity>) { dao.insertAll(items) }

    override fun deleteAll() { dao.deleteAll() }

}
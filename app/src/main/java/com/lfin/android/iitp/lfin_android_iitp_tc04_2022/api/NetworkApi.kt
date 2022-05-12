package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.api

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.model.QueryPlanResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApi {

    /**
     * 시험실행순서 json 파일에 접근
     */
    @GET("/data/json/{fileName}")
    suspend fun loadQueryPlanInfo(
        @Path("fileName")
        filename:String
    ): QueryPlanResponse

}
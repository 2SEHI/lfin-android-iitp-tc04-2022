package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.api

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.model.QueryPlanResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface NetworkApi {

    /**
     * 시험실행순서 json 파일에 접근
     */
    @GET("/data/json/{fileName}")
    suspend fun loadQueryPlanInfo(
        @Path("fileName")
        filename: String
    ): QueryPlanResponse

    /**
     * 이미지파일 다운로드
     */
    @GET("/data/images/tc04_2/{fileName}")
    @Streaming
    suspend fun downloadImage(
        @Path("fileName")
        fileName: String
    ): Response<ResponseBody>
}
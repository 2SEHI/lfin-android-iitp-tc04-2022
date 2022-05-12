package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.model

/**
 * 서버에서 전달받는 시험실행순서 데이터의 구조
 */
data class QueryPlanResponse(
    val items: List<Item>
) {
    data class Item(
        val title: String,
        val description: String
    )

}

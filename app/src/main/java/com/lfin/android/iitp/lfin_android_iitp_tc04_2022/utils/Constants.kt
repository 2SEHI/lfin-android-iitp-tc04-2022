package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils

object Constants {
    const val IMAGE_DIR = "tc04/images"
    const val CSV_DIR = "tc04/csv"
    const val CSV_FILE_NAME = "result_"
    const val CSV_FILE_TIME ="yyyy-MM-dd_HHmmss"

    const val BASE_URL = "https://22tta.lfin.kr"
    // 10000건 실행시
//    const val QUERY_PLAN = "queryPlan_v1_10000.json"
    // 1000건 실행시 주석 해제
    const val QUERY_PLAN = "queryPlan_v1.json"

    const val REQUEST_EXTERNAL_STORAGE = 1
    /**
     * CurrentState Message
     */
    const val CS_BEFORE_TEST_DATA = "초기화 완료"
    const val CS_LOADING_META_DATA = "입력 데이터 불러오는 중... "
    const val CS_LOADING_IMAGE = "시험 세트 준비중"
    const val CS_FINISH_TEST = "시험완료"

    /**
     * NextBehavior Message
     */
    const val NB_CLICK_DATA_LOADING = "데이터 가져오기"
    const val NB_WAIT = "처리 완료까지 대기"
    const val NB_CLICK_TEST_START = "시험시작 클릭"
    const val NB_CLICK_SHARE_TEST_RESULT = "시험결과 내보내기 클릭"

    const val BACK_PROCESS = "뒤로가기 버튼을 한번 더 누르면 종료됩니다"


}
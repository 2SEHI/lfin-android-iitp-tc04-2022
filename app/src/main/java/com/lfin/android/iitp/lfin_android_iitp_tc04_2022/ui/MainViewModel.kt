package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.GetQueryPlanUseCase
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.LoadQueryPlanUseCase
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val setUseCase: LoadQueryPlanUseCase,
    private val getUseCase: GetQueryPlanUseCase
) : ViewModel() {
    private val _currentState =
        MutableLiveData<String>().apply { value = Constants.CS_BEFORE_TEST_DATA }
    val currentState: LiveData<String> get() = _currentState

    private val _nextBehavior =
        MutableLiveData<String>().apply { value = Constants.NB_CLICK_DATA_LOADING }
    val nextBehavior: LiveData<String> get() = _nextBehavior

    private val _baseImage = MutableLiveData<String>().apply { value = "" }
    val baseImage = _baseImage

    private val _queryImage = MutableLiveData<String>().apply { value = "" }
    val queryImage = _queryImage

    private val _logDataList = MutableLiveData<List<QueryPlanEntity>>()
    val logDataList: LiveData<List<QueryPlanEntity>> = _logDataList
    private val list = mutableListOf<QueryPlanEntity>()

    init {
        _logDataList.value = getUseCase.getQueryPlanList()
    }

    suspend fun addText() {
        setUseCase.loadTestData()
        list.addAll(getUseCase.getQueryPlanList())
        _logDataList.postValue(list)
    }
}
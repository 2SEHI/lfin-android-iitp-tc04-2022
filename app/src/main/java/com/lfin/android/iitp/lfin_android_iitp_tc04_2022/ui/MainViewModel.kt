package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.GetQueryPlanUseCase
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.LoadQueryPlanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val setUseCase: LoadQueryPlanUseCase,
    private val getUseCase: GetQueryPlanUseCase
): ViewModel() {
    private val _logDataList = MutableLiveData<List<QueryPlanEntity>>()
    val logDataList: LiveData<List<QueryPlanEntity>> = _logDataList
    private val list = mutableListOf<QueryPlanEntity>()

    init{
        _logDataList.value = getUseCase.getQueryPlanList()
    }

    suspend fun addText(){
        setUseCase.insertText()
        list.addAll(getUseCase.getQueryPlanList())
        _logDataList.postValue(list)
    }
}
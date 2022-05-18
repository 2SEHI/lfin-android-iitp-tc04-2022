package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.ui

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.*
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.onEach
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loadMetaData: LoadMetaDataUseCase,
    private val startTestUseCase: StartTestUseCase,
    private val resetUseCase: ResetUseCase,
    // ClearCsvDataUseCase
    // PutCsvDataUseCase
    // ExportCsvDataUseCase
    // GetImageProcessingState
    private val getQueryPlanUseCase: GetQueryPlanUseCaseSample,
    private val findLocationProcessingUseCase: FindLocationProcessingUseCase
) : ViewModel() {

    companion object {
        val TAG: String = MainViewModel::class.java.simpleName

    }

    private val _currentState =
        MutableLiveData<String>().apply { value = Constants.CS_BEFORE_TEST_DATA }
    val currentState: LiveData<String> get() = _currentState

//    private val _currentState : MutableStateFlow<String> = MutableStateFlow(Constants.CS_BEFORE_TEST_DATA)
//    val currentState: StateFlow<String> get() = _currentState

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
        resetDisplay()

    }

    /**
     * 이미지 프로세싱 시작
     */
    val processingStart = View.OnClickListener {
        viewModelScope.launch {
            // Clear
            // val getQueryPlanAsync = async { getQueryPlanUseCase.invoke() }
            val response = getQueryPlanUseCase.invoke()
            if (response.succeeded) {
                // TODO 데이터 처리
                response.data
                //
                // Put
                response.data?.forEach {
                    it.b_file_name
                }
            } else {

            }
        }
    }

    /**
     * 이미지 프로세싱 시작
     */
    val processingStart2 = View.OnClickListener {
        viewModelScope.launch {
            // Clear
            // val getQueryPlanAsync = async { getQueryPlanUseCase.invoke() }
            val response = getQueryPlanUseCase.invoke()
            if (response.succeeded) {
                // TODO 데이터 처리
                response.data
                //
                // Put
                response.data?.forEach {
                    val processingResult =
                        findLocationProcessingUseCase.invoke(FindLocationProcessingUseCase.Param(it))
                    if (processingResult.succeeded) {
                        // CSV 저장할곳에 데이터 입력

                    }
                }
            } else {

            }
        }
    }

    fun reset() {
        resetData()
        resetDisplay()
    }

    private fun resetDisplay() {
        _logDataList.value = emptyList()
        _currentState.value = Constants.CS_BEFORE_TEST_DATA
        _nextBehavior.value = Constants.NB_CLICK_DATA_LOADING
        _baseImage.value = ""
        _queryImage.value = ""
    }

    private fun resetData() {
        resetUseCase.deleteQueryPlan()
    }

    suspend fun readyForTest() {
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            loadMetaData.loadMetaData()
        }
    }

    fun startTest() {

        viewModelScope.launch {
            startTestUseCase.imageProcessing().onEach {
                _currentState.postValue("${it.currentState}")
                _baseImage.postValue("${it.baseImage}")
                _queryImage.postValue("${it.queryImage}")
                Log.d("_queryImage.postValue", "${it.queryImage}")
            }.conflate().collect {

            }
        }
    }
}
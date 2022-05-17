package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.LoadMetaDataUseCase
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.ResetUseCase
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.StartTestUseCase
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loadMetaData: LoadMetaDataUseCase,
    private val startTestUseCase: StartTestUseCase,
    private val resetUseCase: ResetUseCase,
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

    fun reset() {
        resetDisplay()
        resetData()
    }

    fun resetDisplay() {
        _logDataList.value = emptyList()
        _currentState.value = Constants.CS_BEFORE_TEST_DATA
        _nextBehavior.value = Constants.NB_CLICK_DATA_LOADING
        _baseImage.value = ""
        _queryImage.value = ""
    }

    fun resetData() {
        resetUseCase.deleteQueryPlan()
    }

    suspend fun readyForTest() {
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            loadMetaData.loadMetaData()
        }
    }

    fun startTest() {
        startTestUseCase.startTest()
//        viewModelScope.launch {
//            startTestUseCase.startTest().collect {
//                _currentState.postValue("emit $it")
//            }
//        }
    }
}
package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.ui
import android.content.Context
import android.graphics.BitmapFactory
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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.onEach
import java.io.File
import android.net.Uri
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.decodeBase64
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loadMetaData: LoadMetaDataUseCase,
    private val initializeModuleUseCase: InitializeModuleUseCase,
    private val putBaseImageUseCase: PutBaseImageUseCase,
    private val putQueryImageUseCase: PutQueryImageUseCase,
    private val putMetadataUseCase: PutMetadataUseCase,
    private val findLocationUseCase: FindLocationUseCase,
    private val getResultForDisplayUseCase: GetResultForDisplayUseCase,

    private val startTestUseCase: StartTestUseCase,
    private val resetQueryPlanUseCase: ResetQueryPlanUseCase,
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

    private val _nextBehavior =
        MutableLiveData<String>().apply { value = Constants.NB_CLICK_DATA_LOADING }
    val nextBehavior: LiveData<String> get() = _nextBehavior

    private val _baseImage = MutableLiveData<Uri>()
    val baseImage = _baseImage

    private val _queryImage = MutableLiveData<Uri>()
    val queryImage = _queryImage

    private val _logDataList = MutableLiveData<List<String?>>()
    val logDataList: LiveData<List<String?>> = _logDataList
    private val list = mutableListOf<String?>()

    init {


        _logDataList.value = list
        _currentState.value = Constants.CS_BEFORE_TEST_DATA
        _nextBehavior.value = Constants.NB_CLICK_DATA_LOADING
        _baseImage.value = Uri.fromFile(File(""))
        _queryImage.value = Uri.fromFile(File(""))
    }

    /**
     * 데이터 다운로드 및 저장 시작
     */
    val dataLoadingStart = View.OnClickListener {
        viewModelScope.launch {
            loadMetaData.loadMetaData()
        }
    }

    /**
     * 이미지 프로세싱 시작
     */
    var processingStart = View.OnClickListener {
        viewModelScope.launch {
            val imgDir = File("${context.filesDir}${File.separator}${Constants.IMAGE_DIR}")

            // 모듈 초기화
            val initializeReponse = initializeModuleUseCase.invoke()

            var baseImagePath: String
            var queryImagePath: String

            // list of QueryPlan from <QueryPlan> Table
            val response = getQueryPlanUseCase.invoke()
            if (response.succeeded) {
                // Put
                response.data?.forEach {
                    // 이미지 화면출력
                    baseImagePath = "$imgDir${File.separator}${it.b_file_name}"
                    queryImagePath = "$imgDir${File.separator}${it.q_file_name}"

                    _baseImage.postValue(Uri.fromFile(File(baseImagePath)))
                    _queryImage.postValue(Uri.fromFile(File(queryImagePath)))
//                    delay(500L)
                    val baseBitmap = BitmapFactory.decodeFile(baseImagePath)
                    val queryBitmap = BitmapFactory.decodeFile(queryImagePath)

                    _currentState.postValue(putBaseImageUseCase.invoke(PutBaseImageUseCase.Param(baseBitmap)).data)
                    _currentState.postValue(putQueryImageUseCase.invoke(PutQueryImageUseCase.Param(queryBitmap)).data)

                    // convert base64 to ByteArray
                    val metaData = decodeBase64(it.metadata)
                    _currentState.postValue(putMetadataUseCase.invoke(PutMetadataUseCase.Param(metaData)).data)
                    _currentState.postValue(findLocationUseCase.invoke().data)
                    list.add(getResultForDisplayUseCase.invoke().data)
                    _logDataList.postValue(list)
                }


            } else {

            }
        }
    }

    fun reset() {
        viewModelScope.launch {
            resetQueryPlanUseCase.invoke()
        }
    }

}
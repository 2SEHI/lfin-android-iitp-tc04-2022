package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.ui
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.MainApplication
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.OpenCVAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.resetDatabase.ResetMetadataUseCase
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.resetDatabase.ResetQueryPlanUseCase
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.loadData.*
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.startTest.*
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.data
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.decodeBase64
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.succeeded
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val downloadQueryPlanUseCase: DownloadQueryPlanUseCase,
    private val saveFileNameUseCase: SaveFileNameUseCase,
    private val getImageFileNameUseCase: GetImageFileNameUseCase,
    private val downloadImageFileUseCase: DownloadImageFileUseCase,

    private val initializeModuleUseCase: InitializeModuleUseCase,
    private val putBaseImageUseCase: PutBaseImageUseCase,
    private val putQueryImageUseCase: PutQueryImageUseCase,
    private val putMetadataUseCase: PutMetadataUseCase,
    private val findLocationUseCase: FindLocationUseCase,
    private val getResultForDisplayUseCase: GetResultForDisplayUseCase,
    private val getQueryPlanUseCase: GetQueryPlanUseCase,
    private val saveCsvUseCase: SaveCsvUseCase,

    private val resetQueryPlanUseCase: ResetQueryPlanUseCase,
    private val resetMetadataUseCase: ResetMetadataUseCase,

    ) : ViewModel() {

    companion object {
        val TAG: String = MainViewModel::class.java.simpleName
        val imgDir = File("${MainApplication.applicationContext().filesDir}${File.separator}${Constants.IMAGE_DIR}")
        val csvDir = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}${File.separator}${Constants.CSV_DIR}")
//        val csvDir = File("${Environment.getExternalStorageDirectory()}${File.separator}${Constants.CSV_DIR}")

    }

    val _currentState =
        MutableLiveData<String>().apply { value = Constants.CS_BEFORE_TEST_DATA }
    val currentState: LiveData<String> get() = _currentState

    val _nextBehavior =
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
        viewModelScope.launch {
//            resetQueryPlanUseCase.invoke()
        }
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
            // 모듈 초기화
            val initializeReponse = initializeModuleUseCase.invoke()
            _currentState.postValue("queryPlan 다운로드 중")
            _nextBehavior.postValue("")
            // 서버에서 queryPlan 다운로드 후, DB에 저장
            downloadQueryPlanUseCase.invoke()
            _currentState.postValue("이미지 파일명 저장 중")
            // 이미지 파일명 목록을 DB에 저장
            saveFileNameUseCase.invoke()

            // 이미지 파일명 목록 가져오기
            val response = getImageFileNameUseCase.invoke()
            if (response.succeeded) {
                var totalSize = response.data?.size
                var current = 0

                // 서버에서 이미지파일 다운로드 후, 디바이스에 저장
                response.data?.forEach {
                    current++
                    _currentState.postValue("이미지 파일 $current/$totalSize 다운로드 중...")
                    Log.d(TAG, it)

                    _baseImage.postValue(downloadImageFileUseCase.invoke(DownloadImageFileUseCase.Param(it)).data)
                }
                _currentState.postValue("데이터 다운로드 완료")
                _nextBehavior.postValue("시험 시작")

            }
        }
    }

    /**
     * 이미지 프로세싱 시작
     */
    var processingStart = View.OnClickListener {
        viewModelScope.launch {
            // TODO
            OpenCVAdapter.restart()

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
                saveCsvUseCase.invoke()
//                _currentState.postValue("시험 완료")
                _nextBehavior.postValue("시험 결과 내보내기")
            } else {

            }
        }
    }

    fun reset() {
        viewModelScope.launch {
//            resetQueryPlanUseCase.invoke()
//            resetMetadataUseCase.invoke()
        }
    }

}
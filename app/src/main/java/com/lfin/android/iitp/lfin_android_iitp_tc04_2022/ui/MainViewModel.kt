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
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.DecimalFormat
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
    private val _loadDataBtn = MutableLiveData<String>().apply { value="?????????\n????????????" }
    val loadDataBtn: LiveData<String> get() = _loadDataBtn

//    var testCnt = 0
    private val _startTestBtn = MutableLiveData<String>().apply { value="????????????" }
    val startTestBtn: LiveData<String> get() = _startTestBtn

    val _sendResultBtn = MutableLiveData<String>().apply { value="????????????\n????????????" }
    val sendResultBtn: LiveData<String> get() = _sendResultBtn

    val _currentState =
        MutableLiveData<String>().apply { value = Constants.CS_BEFORE_TEST_DATA }
    val currentState: LiveData<String> get() = _currentState

    val _nextBehavior =
        MutableLiveData<String>().apply { value = Constants.NB_CLICK_DATA_LOADING }
    val nextBehavior: LiveData<String> get() = _nextBehavior

    private val _baseImage = MutableLiveData<Uri>()
    val baseImage = _baseImage

    private val _baseFileName =
        MutableLiveData<String>().apply { value = "" }
    val baseFileName: LiveData<String> get() = _baseFileName

    private val _queryImage = MutableLiveData<Uri>()
    val queryImage = _queryImage

    private val _queryFileName =
        MutableLiveData<String>().apply { value = "" }
    val queryFileName: LiveData<String> get() = _queryFileName

    private val _logDataList = MutableLiveData<List<String?>>()
    val logDataList: LiveData<List<String?>> = _logDataList
    private val list = mutableListOf<String?>()

    init {
        viewModelScope.launch {
            resetQueryPlanUseCase.invoke()
        }
        _logDataList.value = list
        _currentState.value = Constants.CS_BEFORE_TEST_DATA
        _nextBehavior.value = Constants.NB_CLICK_DATA_LOADING
        _baseImage.value = Uri.fromFile(File(""))
        _queryImage.value = Uri.fromFile(File(""))
    }

    /**
     * ????????? ???????????? ??? ?????? ??????
     */
    val dataLoadingStart = View.OnClickListener {
        viewModelScope.launch {
            // ?????? ?????????
            val initializeReponse = initializeModuleUseCase.invoke()
            _currentState.postValue("queryPlan ???????????? ???")
            _nextBehavior.postValue("")
            // ???????????? queryPlan ???????????? ???, DB??? ??????
            downloadQueryPlanUseCase.invoke()
            _currentState.postValue("????????? ????????? ?????? ???")
            // ????????? ????????? ????????? DB??? ??????
            saveFileNameUseCase.invoke()

            // ????????? ????????? ?????? ????????????
            val response = getImageFileNameUseCase.invoke()
            if (response.succeeded) {
                var totalSize = getDecimalFormat(response.data?.size)
                var current = 0

                // ???????????? ??????????????? ???????????? ???, ??????????????? ??????
                response.data?.forEach {
                    current++
                    _currentState.postValue("????????? ?????? ${getDecimalFormat(current)}/${totalSize} ???????????? ???...")
                    Log.d(TAG, it)

                    _baseImage.postValue(downloadImageFileUseCase.invoke(DownloadImageFileUseCase.Param(it)).data)
                }
                _loadDataBtn.postValue("?????????\n????????????\n???")
                _currentState.postValue("????????? ?????? ??????(${getDecimalFormat(current)}/${totalSize})")
                _nextBehavior.postValue("?????? ??????")
//                _isLoadData.value = true

            }
        }
    }

    /**
     * ????????? ???????????? ??????
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
                    // ????????? ????????????
                    baseImagePath = "$imgDir${File.separator}${it.b_file_name}"
                    queryImagePath = "$imgDir${File.separator}${it.q_file_name}"

                    // ????????? ??????
                    _baseImage.postValue(Uri.fromFile(File(baseImagePath)))
                    _queryImage.postValue(Uri.fromFile(File(queryImagePath)))
                    // ????????? ??????
                    _baseFileName.postValue(it.b_file_name)
                    _queryFileName.postValue(it.q_file_name)

//                    delay(500L)
                    val baseBitmap = BitmapFactory.decodeFile(baseImagePath)
                    val queryBitmap = BitmapFactory.decodeFile(queryImagePath)

                    _currentState.postValue(putBaseImageUseCase.invoke(PutBaseImageUseCase.Param(baseBitmap)).data)
                    _currentState.postValue(putQueryImageUseCase.invoke(PutQueryImageUseCase.Param(queryBitmap)).data)

                    // convert base64 to ByteArray
                    val metadata = AndroidBase64.decode(it.metadata)
                    Log.d("original metadata: ", metadata.toString(Charsets.UTF_8))
                    _currentState.postValue(putMetadataUseCase.invoke(PutMetadataUseCase.Param(metadata)).data)
                    _currentState.postValue(findLocationUseCase.invoke().data)
                    list.add(getResultForDisplayUseCase.invoke().data)
                    _logDataList.postValue(list)
                }
                saveCsvUseCase.invoke()
                _currentState.postValue("????????????")
                _startTestBtn.postValue("????????????\n???")
                _nextBehavior.postValue("???????????? ????????????")
            } else {

            }
        }
    }

    fun getDecimalFormat(number: Int?): String {
        val deciamlFormat = DecimalFormat("##,###")
        return deciamlFormat.format(number)
    }

    fun reset() {
        viewModelScope.launch {
            resetQueryPlanUseCase.invoke()
            resetMetadataUseCase.invoke()
        }
    }

}
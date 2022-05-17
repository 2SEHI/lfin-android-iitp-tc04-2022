package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.LogAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.databinding.ActivityMainBinding
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.model.PrintData
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var logAdapter: LogAdapter

    private var pressedTime: Long = 0
    private val duration = Toast.LENGTH_SHORT

    // 현재 Editor 에 표현된 데이터
    private lateinit var printData: PrintData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logAdapter = LogAdapter()
        binding.recyclerview.adapter = logAdapter

        mainViewModel.reset()

        // 이미지처리결과 리스트 RecycleView 업데이트
        mainViewModel.logDataList.observe(this, Observer {
            it?.let {
//                mainViewModel.smoothScrollToPosition(it.size)
                logAdapter.submitList(it?.toMutableList())
            }
        })
        // 테스트 로그 RecycleView 업데이트
        mainViewModel.currentState.observe(this, Observer {
            CoroutineScope(Dispatchers.Main).launch {
                binding.printStatus.text = it
            }
        })
//        binding.vm?.setDisplay(this.printData)
        // 데이터 가져오기 클릭
        binding.loadDataBtn.setOnClickListener(this)
        // 시험 시작 클릭
        binding.startTestBtn.setOnClickListener(this)
        // 시험 결과 내보내기 클릭
        binding.sendResultBtn.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        CoroutineScope(Dispatchers.Main).launch {
            when (view) {

                // 데이터 가져오기
                binding.loadDataBtn -> {
                    mainViewModel.readyForTest()
                }
                // 시험시작
                binding.startTestBtn -> {
                    mainViewModel.startTest()
                }
                // 결과 보내기
                binding.sendResultBtn -> {
//                    openFile()
                }
            }
        }
    }

    private fun sendFile(){
        val csvDirUri = Uri.parse("${filesDir}${File.separator}${Constants.CSV_DIR}${File.separator}")
        val shareIntent = Intent().apply {
            action = Intent.ACTION_OPEN_DOCUMENT_TREE
            putExtra(Intent.EXTRA_STREAM, csvDirUri)
        }
        getResult.launch(shareIntent)
    }

    /**
     * CSV파일을 공유하는 처리
     */
    private var getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK){
            Log.d(TAG,"")
        }

    }

            /**
     * CSV파일을 공유하는 처리
     * 파일 선택
     */
    private fun openFile() {
//        val csvDirUri = Uri.parse("${filesDir}${File.separator}${Constants.CSV_DIR}${File.separator}")
                val csvDir = File("${getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)}${File.separator}${Constants.CSV_DIR}")
        val csvDirUri = Uri.fromFile(File("${getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)}"))

        Log.d(TAG, "csvDirUri - $csvDirUri")
        // 파일 선택 Intent
        val pickIntent = Intent().apply {
            action = Intent.ACTION_OPEN_DOCUMENT_TREE // 권한 설정 없이도 파일을 공용저장소에서 읽어올 수 있음
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//            type = "text/*"
//            setDataAndType(csvDirUri, "*/*")
//            addCategory(Intent.CATEGORY_OPENABLE) // open가능한 것들로 카테고리를 묶기
            putExtra(
                DocumentsContract.EXTRA_INITIAL_URI,
                csvDirUri
            ) // 파일 선택 도구가 처음 로드될 때 표시해야 하는 파일이나 디렉터리의 URI

        }
        // Intent를 처리할 수 있는 앱이 존재하는 경우
        if (intent.resolveActivity(packageManager) != null) {
            // CSV파일 공유 처리 호출
            sendLauncher.launch(Intent.createChooser(pickIntent, "Choose file"))
        }
    }

    /**
     * CSV파일을 공유하는 처리
     */
    private var sendLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            // TODO send기능은 작동하지만, 한번 보냈던 방식을 기억하고 있음
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result?.data
                when {
                    data?.data != null -> {
                        val uri = data?.data as Uri
                        Log.d(TAG, "uri: $uri")
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND_MULTIPLE
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            type = "text/*"
                            putExtra(Intent.EXTRA_STREAM, uri)
                        }

                        if (sendIntent.resolveActivity(packageManager) != null) {
                            startActivity(sendIntent)
                        } else {
                            Toast.makeText(this@MainActivity, "전송 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        } catch (e: Exception) {
            Log.d("TAG", e.toString())
        }
    }

    /**
     * 뒤로가기버튼 두번누르면 앱 종료
     */
    override fun onBackPressed() {
        if (System.currentTimeMillis() > pressedTime + 2000) {
            pressedTime = System.currentTimeMillis()
            Toast.makeText(applicationContext, Constants.BACK_PROCESS, duration).show()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Reset Database(QueryPlan Table) and App Display
     */
    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.reset()
        Log.d(TAG, "onDestroy() 호출")
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

}
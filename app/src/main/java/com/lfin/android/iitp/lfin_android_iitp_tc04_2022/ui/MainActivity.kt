package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.ui

import android.app.Activity
import android.content.Intent
import android.content.Intent.EXTRA_ALLOW_MULTIPLE
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.DocumentsContract.EXTRA_INITIAL_URI
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.LogAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.databinding.ActivityMainBinding
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.model.PrintData
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var logAdapter: LogAdapter

    private var pressedTime: Long = 0
    private val duration = Toast.LENGTH_SHORT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vm = mainViewModel

        logAdapter = LogAdapter()
        binding.recyclerview.adapter = logAdapter

        // 이미지처리결과 리스트 RecycleView 업데이트
        mainViewModel.logDataList.observe(this, Observer {
            it?.let {
//                mainViewModel.smoothScrollToPosition(it.size)
                logAdapter.submitList(it?.toMutableList())
            }
        })

        // 진행상태 textView 업데이트
        mainViewModel.currentState.observe(this, Observer {
            CoroutineScope(Dispatchers.Main).launch {
                binding.printStatus.text = it
            }
        })

        // 다음 지시사항 textView 업데이트
        mainViewModel.nextBehavior.observe(this, Observer {
            CoroutineScope(Dispatchers.Main).launch {
                binding.printBehavior.text = it
            }
        })

        mainViewModel.baseImage.observe(this, Observer {
            CoroutineScope(Dispatchers.Main).launch {
                binding.baseImage.setImageURI(it)
            }
        })
        mainViewModel.queryImage.observe(this, Observer {
            CoroutineScope(Dispatchers.Main).launch    {
                binding.queryImage.setImageURI(it)
            }
        })
        binding.sendResultBtn.setOnClickListener {
            openFile()
        }
    }

    /**
     * CSV파일을 공유하는 처리
     * 파일 선택
     */
    private fun openFile() {
        Log.d("File(MainViewModel.csvDir.path).exists() : ", "${ File(MainViewModel.csvDir.path).exists() }")
        val csvDirUri = Uri.parse(MainViewModel.csvDir.path)

        Log.d(TAG, "csvDirUri - $csvDirUri")
        // 파일 선택 Intent
        val pickIntent = Intent().apply {
            action = Intent.ACTION_OPEN_DOCUMENT // 권한 설정 없이도 파일을 공용저장소에서 읽어올 수 있음
            type = "text/*"
            addCategory(Intent.CATEGORY_OPENABLE) // open가능한 것들로 카테고리를 묶기
            putExtra(EXTRA_INITIAL_URI, csvDirUri) // 파일 선택 도구가 처음 로드될 때 표시해야 하는 파일이나 디렉터리의 URI
            putExtra(EXTRA_ALLOW_MULTIPLE, true) // 파일 선택 도구가 처음 로드될 때 표시해야 하는 파일이나 디렉터리의 URI

        }
        // Intent를 처리할 수 있는 앱이 존재하는 경우
        if (intent.resolveActivity(packageManager) != null) {
            // CSV파일 공유 처리 호출
            sendLauncher.launch(Intent.createChooser(pickIntent, "Choose file"))
        }
    }
//
//    private fun openFile() {
//        Log.d("File(MainViewModel.csvDir.path).exists() : ", "${ File(MainViewModel.csvDir.path).exists() }")
//        val csvDirUri = Uri.parse(MainViewModel.csvDir.path)
//
//        Log.d(TAG, "csvDirUri - $csvDirUri")
//        // 파일 선택 Intent
//        val pickIntent = Intent().apply {
//            action = Intent.ACTION_SEND_MULTIPLE // 권한 설정 없이도 파일을 공용저장소에서 읽어올 수 있음
//            type = "text/*"
////            addCategory(Intent.CATEGORY_OPENABLE) // open가능한 것들로 카테고리를 묶기
//            putExtra(EXTRA_INITIAL_URI, csvDirUri) // 파일 선택 도구가 처음 로드될 때 표시해야 하는 파일이나 디렉터리의 URI
//            putExtra(EXTRA_ALLOW_MULTIPLE, true) // 파일 선택 도구가 처음 로드될 때 표시해야 하는 파일이나 디렉터리의 URI
//
//        }
//        // Intent를 처리할 수 있는 앱이 존재하는 경우
//        if (intent.resolveActivity(packageManager) != null) {
//            Intent.createChooser(pickIntent, "Choose file")
//            // CSV파일 공유 처리 호출
//            startActivity()
//        }
//    }

    /**
     * CSV파일을 공유하는 처리
     */
    private var sendLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result?.data
                when {
                    data?.data != null -> {
//                        var uriList = ArrayList<Uri>()
//                        val clipdata2 =  data?.clipData!!.itemCount
//                        Log.d("clipdata2", "$clipdata2")
//                        val clipdata =  data?.clipData
//                        Log.d("ddd", "${clipdata}")
//                        if (clipdata != null) {
//                            val cnt = clipdata.itemCount
//
//                            for (i in 0..cnt){
//                                Log.d("ddd", "${clipdata.getItemAt(i).uri}")
//                                uriList.add(clipdata.getItemAt(i).uri)
//
//                            }
//                            // TODO
//                        }


                        val uri = data?.data as Uri
                        Log.d(TAG, "uri: $uri")
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
//                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            type = "text/*"
                            putExtra(Intent.EXTRA_STREAM, uri)
//                            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList)
                        }

                        if (sendIntent.resolveActivity(packageManager) != null) {

                            startActivity(sendIntent)

                            mainViewModel._currentState.postValue("파일 전송 완료")
                            mainViewModel._nextBehavior.postValue("시험 결과 확인")
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
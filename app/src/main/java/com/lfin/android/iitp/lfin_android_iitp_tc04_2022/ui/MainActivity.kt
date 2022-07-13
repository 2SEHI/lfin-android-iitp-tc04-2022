package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Intent.EXTRA_ALLOW_MULTIPLE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract.EXTRA_INITIAL_URI
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.R
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.LogAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.databinding.ActivityMainBinding
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
//    private lateinit var logAdapter: LogAdapter

    private var pressedTime: Long = 0
    private val duration = Toast.LENGTH_SHORT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.reset()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vm = mainViewModel
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // 테스트 로그 출력 RecycleView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        // 로그 Adapter
        val logAdapter = LogAdapter()

        binding.recyclerview.adapter = logAdapter
        recyclerView.adapter = logAdapter


        // 외부 접근 메모리 접근 가능할 경우,
        // json, 이미지 가져오기
        if (!checkPersmission()) {
            Log.d("TAG", "카메라 허가 받아야함")
            requestPermission()
        }
        // 이미지처리결과 리스트 RecycleView 업데이트
        mainViewModel.logDataList.observe(this, Observer {
            it?.let {
                recyclerView.smoothScrollToPosition(it.size)
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

        mainViewModel.baseFileName.observe(this, Observer {
            CoroutineScope(Dispatchers.Main).launch {
                binding.baseFileName.text = it
            }
        })
        mainViewModel.queryFileName.observe(this, Observer {
            CoroutineScope(Dispatchers.Main).launch    {
                binding.queryFileName.text = it
            }
        })
        mainViewModel.loadDataBtn.observe(this, Observer {
            CoroutineScope(Dispatchers.Main).launch    {
                binding.loadDataBtn.text = it
            }
        })
        mainViewModel.startTestBtn.observe(this, Observer {
            CoroutineScope(Dispatchers.Main).launch    {
                binding.startTestBtn.text = it
            }
        })
        mainViewModel.sendResultBtn.observe(this, Observer {
            CoroutineScope(Dispatchers.Main).launch    {
                binding.sendResultBtn.text = it
            }
        })

        binding.sendResultBtn.setOnClickListener {
            openFile()
        }
    }

    // 권한요청 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var isAllGranted = true
        for (grantResult in grantResults) {
            isAllGranted = isAllGranted and (grantResult == PackageManager.PERMISSION_GRANTED)
        }
        if (!isAllGranted) {
            Toast.makeText(this@MainActivity, "권한 거부", Toast.LENGTH_SHORT).show()
        }
    }

    // 외부 메모리 접근 권한 체크
    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    // 외부 메모리 접근 권한 요청
    private fun requestPermission() {
        Log.d("권한확인", "OK")
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), Constants.REQUEST_EXTERNAL_STORAGE
        )
    }

    /**
     * CSV파일을 공유하는 처리
     * 파일 선택
     */
    private fun openFile() {
        Log.d("openFile", "${ File(MainViewModel.csvDir.path).exists() }")
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
                            ""
                            mainViewModel._currentState.postValue("시험결과 내보내기 완료")
                            mainViewModel._nextBehavior.postValue("앱 종료하기")
                            mainViewModel._sendResultBtn.postValue("시험결과\n내보내기\n✅")

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
            finish()
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
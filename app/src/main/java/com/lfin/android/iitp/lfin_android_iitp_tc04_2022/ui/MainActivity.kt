package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.databinding.ActivityMainBinding
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.LogAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.model.PrintData
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

            }
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
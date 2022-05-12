package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.LogAdaptor
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val itemViewModel: MainViewModel by viewModels()
    private lateinit var logAdapter: LogAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logAdapter = LogAdaptor()
        binding.recyclerview.adapter = logAdapter

        binding.addTextBtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                itemViewModel.addText()
            }
        }

        // 테스트 로그 RecycleView 업데이트
        itemViewModel.logDataList.observe(this, Observer {
            it?.let {
//                itemViewModel.smoothScrollToPosition(it.size)
                logAdapter.submitList(it?.toMutableList())
            }
        })
    }

    companion object {
        // Used to load the 'lfin_android_iitp_tc04_2022' library on application startup.
        init {
            System.loadLibrary("lfin_android_iitp_tc04_2022")
        }
    }
}
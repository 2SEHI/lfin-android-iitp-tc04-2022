package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.startTest

import android.content.Context
import android.util.Log
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.OpenCVAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.ui.MainViewModel
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Result
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCase
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCaseNonParam
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import javax.inject.Inject

class SaveCsvUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) : UseCaseNonParam<Boolean>(Dispatchers.IO) {

    override suspend fun execute(): Boolean {
        val csvDir = MainViewModel.csvDir

        if (!csvDir.exists()) {
            Log.d("csvDir", csvDir.exists().toString())
            csvDir.mkdirs()
        }

        // saveCSV
        val simpleDateFormat = SimpleDateFormat(Constants.CSV_FILE_TIME)
        val resultTime = simpleDateFormat.format(System.currentTimeMillis())
        val fileName = "result_$resultTime.csv"
        val csvPath = "${ MainViewModel.csvDir.path }${File.separator}$fileName"
        Log.d("exportPATH", csvPath)
        var fileWriter: FileWriter? = null
        var buffer: BufferedWriter? = null
        try {
            fileWriter = FileWriter(csvPath)
            buffer = BufferedWriter(fileWriter)
            buffer.write(OpenCVAdapter.getPtrOfString(2))
            Log.d("exportData", OpenCVAdapter.getPtrOfString(2))
            buffer.flush()
            true
        } catch (e: IOException) {
            Timber.d(e)
            Result.Error(e)
            Log.d("saveFile", e.toString())
            false
        } finally {
            buffer?.close()
            fileWriter?.close()
        }
        return true
    }
}
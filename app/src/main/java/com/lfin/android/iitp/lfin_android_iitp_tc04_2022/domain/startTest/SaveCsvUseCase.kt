package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.startTest

import android.content.Context
import android.util.Log
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.OpenCVAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Result
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import javax.inject.Inject

class SaveCsvUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) : UseCase<SaveCsvUseCase.Param, Boolean>(Dispatchers.IO) {
    data class Param(
        val queryPlanEntity: QueryPlanEntity
    )

    companion object {
        val TAG: String = SaveCsvUseCase::class.java.simpleName
    }

    override suspend fun execute(param: Param): Boolean {
        val csvDir = File("${context.filesDir}${File.separator}${Constants.CSV_DIR}")

        if (!csvDir.exists()) {
            csvDir.mkdirs()
        }

        // saveCSV
        val filename = "result_${System.currentTimeMillis()}.csv"
        val csvPath = File("${csvDir.path}${File.separator}${filename}")

        var fileWriter: FileWriter? = null
        var buffer: BufferedWriter? = null
        try {
            fileWriter = FileWriter(csvPath)
            buffer = BufferedWriter(fileWriter)
            buffer.write(OpenCVAdapter.getPtrOfString(2))
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
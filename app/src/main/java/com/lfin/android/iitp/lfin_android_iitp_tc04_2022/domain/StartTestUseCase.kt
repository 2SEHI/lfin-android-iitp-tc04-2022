package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter.OpenCVAdapter
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.model.PrintData
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.Constants
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.decodeBase64
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartTestUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getQueryPlanUseCase: GetQueryPlanUseCase,
) {
    companion object {
        val TAG: String = StartTestUseCase::class.java.simpleName
    }

//    fun startTest(){
//        imageProcessing()
//        saveCSV()
//    }

    fun imageProcessing() = flow<PrintData> {
        val imgDir = File("${context.filesDir}${File.separator}${Constants.IMAGE_DIR}")
        val queryPlanList = getQueryPlanUseCase.getQueryPlanList()
        var baseImagePath: String
        var queryImagePath: String
        var printData = PrintData()
        for (queryPlan in queryPlanList) {

            baseImagePath = "$imgDir${File.separator}${queryPlan.b_file_name}"
            queryImagePath = "$imgDir${File.separator}${queryPlan.q_file_name}"
            printData.baseImage = baseImagePath
            printData.queryImage = queryImagePath
            emit(printData)
            // TODO 이미지 화면 표시하기
            val baseBitmap =
                BitmapFactory.decodeFile(baseImagePath)
            val queryBitmap =
                BitmapFactory.decodeFile(queryImagePath)
            // TODO: initialize할 때 Trial 숫자 리셋해야함
            OpenCVAdapter.initializeModule()
            OpenCVAdapter.putBitmap(baseBitmap)

            printData.currentState = OpenCVAdapter.getPtrOfString(0)
            emit(printData)
            OpenCVAdapter.putBitmap(queryBitmap)

            printData.currentState = OpenCVAdapter.getPtrOfString(0)
            emit(printData)

            // convert base64 to ByteArray
            val metaData = decodeBase64(queryPlan.metadata)
            // send MetaData
            OpenCVAdapter.putMetadata(metaData)
            printData.currentState = OpenCVAdapter.getPtrOfString(0)
            emit(printData)

            // start imageProcessing
            OpenCVAdapter.imageProcessing()
            printData.currentState = OpenCVAdapter.getPtrOfString(0)
            emit(printData)

            // get imageProcess Result for export CSV
            Log.d(TAG,"${OpenCVAdapter.getPtrOfString(1)}")

        }
    }

    private fun saveCSV() {
        val csvDir = File("${context.filesDir}${File.separator}${Constants.CSV_DIR}")
//        val csvDir = File("${context.getExternalFilesDir(DIRECTORY_DOWNLOADS)}${File.separator}${Constants.CSV_DIR}")
        Log.d(TAG,"saveCSV csvDir: ${csvDir}")
//        val csvDir = File("${context.filesDir}${File.separator}${Constants.CSV_DIR}")
        if (!csvDir.exists()) {
            csvDir.mkdirs()
        }
        val ed = SimpleDateFormat(Constants.CSV_FILE_TIME).format(Date(System.currentTimeMillis()))
        val csvFileName = "${Constants.CSV_FILE_NAME}${ed}"
        val csvPath  = File("${csvDir}${File.separator}${csvFileName}")
        var fileWriter: FileWriter? = null
        var buffer: BufferedWriter? = null
        try{

            fileWriter = FileWriter(csvPath)
            buffer = BufferedWriter(fileWriter)
            buffer.write(OpenCVAdapter.getPtrOfString(2))

        }catch(e: java.lang.Exception){
            Log.d(TAG, e.printStackTrace().toString())
        }finally {
            try{
                buffer?.flush()
                buffer?.close()
                fileWriter?.close()
            } catch (e: IOException) {
                Log.e("csvHeader()","${e.printStackTrace()}")
            }
        }
    }

}
package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.loadData

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.ImageFileRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.ui.MainViewModel
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCase
import kotlinx.coroutines.Dispatchers
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

/**
 * Server로부터 QueryPlan을 다운받아 DB에 저장
 */
class DownloadImageFileUseCase @Inject constructor(
    private val saveImageFileUseCase: SaveImageFileUseCase,
    private val imageFileRepository: ImageFileRepository
) : UseCase<DownloadImageFileUseCase.Param, Uri>(Dispatchers.IO) {

    data class Param(
        val fileName: String
    )

    override suspend fun execute(param: Param): Uri {

        val imgDir = MainViewModel.imgDir
        // 이미지 저장 디렉토리가 없는 경우 디렉토리 생성
        if (!imgDir.exists()) {
            imgDir.mkdirs()
        }
        var responseBody: ResponseBody?
        val imgPath = File("${MainViewModel.imgDir.path }${File.separator}${param.fileName}")
//        val imgPath = File(imgDir, param.fileName)
        // 동일한 이미지 파일이 존재하는 경우 삭제
        if (imgPath.exists()) {
            imgPath.delete()
        }
        try {
            responseBody = imageFileRepository.loadImageFile(param.fileName)
            // 서버에서 다운받은 이미지를 디바이스에 저장
            saveImageFileUseCase.invoke(SaveImageFileUseCase.Param(responseBody, imgPath))

        } catch (e: Exception) {
            Log.d("onResponse", "${e.printStackTrace()}")
            e.printStackTrace()
//                return false
        }
        return imgPath.toUri()
    }

}
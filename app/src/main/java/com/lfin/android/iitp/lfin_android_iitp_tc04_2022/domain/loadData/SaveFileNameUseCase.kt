package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.domain.loadData

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.ImageFileEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.ImageFileRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.utils.UseCaseNonParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SaveFileNameUseCase @Inject constructor(
    private val queryPlanRepository: QueryPlanRepository,
    private val setImageFileUseCase: SetImageFileUseCase,
    private val imageFileRepository: ImageFileRepository
    ) : UseCaseNonParam<Boolean>(Dispatchers.IO) {

    override suspend fun execute(): Boolean {
        val baseEntityList: List<ImageFileEntity> = stringToImageFileEntity( queryPlanRepository.getAllBaseFile())
        setImageFileUseCase.setImageFileList(baseEntityList)

        val queryEntityList: List<ImageFileEntity> = stringToImageFileEntity(queryPlanRepository.getAllQueryFile())
        setImageFileUseCase.setImageFileList(queryEntityList)
        return imageFileRepository.getAllImageFile().isNotEmpty()
    }

    private fun stringToImageFileEntity(fileNameList: List<String>): ArrayList<ImageFileEntity> {
        var oList: ArrayList<ImageFileEntity> = arrayListOf()
        for (fileName in fileNameList) {
            oList.add(ImageFileEntity(fileName))
        }
        return oList
    }

}
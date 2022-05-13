package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.module

import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dataSource.ImageFileLocalDataSource
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dataSource.QueryPlanLocalDataSource
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.ImageFileRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.ImageFileRepositoryImpl
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepository
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.repository.QueryPlanRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideQueryPlanRepository(
        retrofit: Retrofit,
        queryPlanLocalDataSource: QueryPlanLocalDataSource
    ): QueryPlanRepository = QueryPlanRepositoryImpl(retrofit, queryPlanLocalDataSource)

    @Singleton
    @Provides
    fun provideImageFileRepository(
        retrofit: Retrofit,
        imageFileLocalDataSource: ImageFileLocalDataSource
    ): ImageFileRepository = ImageFileRepositoryImpl(retrofit, imageFileLocalDataSource)
}
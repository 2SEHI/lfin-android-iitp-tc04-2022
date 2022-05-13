package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.module

import android.content.Context
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.AppDatabase
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dao.ImageFileDao
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dao.QueryPlanDao
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dataSource.ImageFileLocalDataSource
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dataSource.ImageFileLocalDataSourceImpl
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dataSource.QueryPlanLocalDataSource
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dataSource.QueryPlanLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideQueryPlanDao(appDatabase: AppDatabase): QueryPlanDao =
        appDatabase.queryPlanDao()

    @Provides
    @Singleton
    fun provideImageFileDao(appDatabase: AppDatabase): ImageFileDao =
        appDatabase.imageFileDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.buildDatabase(context)

    @Provides
    @Singleton
    fun provideQueryPlanLocalDataSource(dao: QueryPlanDao): QueryPlanLocalDataSource {
        return QueryPlanLocalDataSourceImpl(dao)
    }

    @Provides
    @Singleton
    fun provideImageFileLocalDataSource(dao: ImageFileDao): ImageFileLocalDataSource {
        return ImageFileLocalDataSourceImpl(dao)
    }
}
package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.module

import android.content.Context
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.AppDatabase
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dao.QueryPlanDao
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
    fun provideItemDao(appDatabase: AppDatabase): QueryPlanDao =
        appDatabase.queryPlanDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.buildDatabase(context)
}
package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dao.QueryPlanDao
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity

@Database(
    entities = [QueryPlanEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun queryPlanDao(): QueryPlanDao

    companion object {
        private const val databaseName = "item-db"

        fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }
}
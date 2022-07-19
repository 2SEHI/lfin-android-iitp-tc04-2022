package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db

import android.content.Context
import android.telecom.Call
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dao.ImageFileDao
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.dao.QueryPlanDao
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.ImageFileEntity
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity

@Database(
    entities = [QueryPlanEntity::class, ImageFileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun queryPlanDao(): QueryPlanDao
    abstract fun imageFileDao(): ImageFileDao

    companion object {
        private const val databaseName = "tc04-db"

        fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // add this code
                        db.execSQL("PRAGMA encoding='UTF-8';")
                    }
                })
                .build()

    }
}
package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "query_plan")
data class QueryPlanEntity(
//    @SerializedName("b_file_name")
    val b_file_name: String,
    val q_file_name: String,
    val metadata: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
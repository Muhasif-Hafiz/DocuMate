package com.muhasib.documate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val color: String?,
    val capacity: String?,
    val price: Double?,
    val generation: String?,
    val year: Int?,
    val cpuModel: String?,
    val hardDiskSize: String?,
    val strapColor: String?,
    val caseSize: String?,
    val description: String?,
    val screenSize: Double?,
    val isSynced: Boolean = true,
    val isDeleted: Boolean = false
)
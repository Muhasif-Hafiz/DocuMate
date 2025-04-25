package com.muhasib.documate.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val uid: String,
    val name: String?,
    val email: String?,
    val photoUrl: String?,
    val token: String
)

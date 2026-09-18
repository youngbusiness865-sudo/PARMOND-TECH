package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: String = "USER", // "USER" or "ADMIN"
    val createdAt: Long = System.currentTimeMillis(),
    val status: String = "ACTIVE" // "ACTIVE" or "BLOCKED"
)

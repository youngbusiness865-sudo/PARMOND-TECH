package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val summary: String,
    val content: String,
    val imageUrl: String = "",
    val category: String,
    val author: String = "PARMOND TECH",
    val date: String,
    val status: String = "PUBLISHED", // "PUBLISHED" or "DRAFT"
    val views: Int = 0
)

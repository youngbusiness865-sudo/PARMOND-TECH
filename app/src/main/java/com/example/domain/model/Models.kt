package com.example.domain.model

enum class UserRole {
    USER,
    ADMIN;

    companion object {
        fun fromString(role: String): UserRole = when (role.uppercase()) {
            "ADMIN" -> ADMIN
            else -> USER
        }
    }
}

enum class UserStatus {
    ACTIVE,
    BLOCKED;

    companion object {
        fun fromString(status: String): UserStatus = when (status.uppercase()) {
            "BLOCKED" -> BLOCKED
            else -> ACTIVE
        }
    }
}

enum class ArticleStatus {
    PUBLISHED,
    DRAFT;

    companion object {
        fun fromString(status: String): ArticleStatus = when (status.uppercase()) {
            "DRAFT" -> DRAFT
            else -> PUBLISHED
        }
    }
}

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val role: UserRole,
    val createdAt: Long,
    val status: UserStatus
)

data class Article(
    val id: Long,
    val title: String,
    val summary: String,
    val content: String,
    val imageUrl: String,
    val category: String,
    val author: String,
    val date: String,
    val status: ArticleStatus,
    val views: Int,
    val isFavorite: Boolean = false
)

data class Category(
    val id: Long,
    val name: String,
    val iconName: String,
    val description: String
)

data class AdminDashboardStats(
    val totalUsers: Int = 0,
    val totalArticles: Int = 0,
    val publishedArticles: Int = 0,
    val draftArticles: Int = 0,
    val totalViews: Int = 0
)

data class NotificationPlaceholder(
    val id: Long,
    val title: String,
    val message: String,
    val timeAgo: String,
    val read: Boolean = false
)

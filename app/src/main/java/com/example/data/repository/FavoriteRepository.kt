package com.example.data.repository

import com.example.data.local.dao.FavoriteDao
import com.example.data.local.entity.ArticleEntity
import com.example.data.local.entity.FavoriteEntity
import com.example.domain.model.Article
import com.example.domain.model.ArticleStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    fun getFavoriteArticles(userId: Long): Flow<List<Article>> {
        return favoriteDao.getFavoriteArticlesForUser(userId).map { list ->
            list.map { it.toDomain(isFavorite = true) }
        }
    }

    fun isFavorite(userId: Long, articleId: Long): Flow<Boolean> {
        return favoriteDao.isFavorite(userId, articleId)
    }

    suspend fun toggleFavorite(userId: Long, articleId: Long, isCurrentlyFav: Boolean) {
        if (isCurrentlyFav) {
            favoriteDao.deleteFavorite(userId, articleId)
        } else {
            favoriteDao.insertFavorite(FavoriteEntity(userId = userId, articleId = articleId))
        }
    }

    fun getFavoriteCount(userId: Long): Flow<Int> {
        return favoriteDao.getFavoriteCountForUser(userId)
    }

    private fun ArticleEntity.toDomain(isFavorite: Boolean) = Article(
        id = id,
        title = title,
        summary = summary,
        content = content,
        imageUrl = imageUrl,
        category = category,
        author = author,
        date = date,
        status = ArticleStatus.fromString(status),
        views = views,
        isFavorite = isFavorite
    )
}

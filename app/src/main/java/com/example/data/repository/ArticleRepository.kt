package com.example.data.repository

import com.example.data.local.dao.ArticleDao
import com.example.data.local.dao.FavoriteDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.ArticleEntity
import com.example.domain.model.AdminDashboardStats
import com.example.domain.model.Article
import com.example.domain.model.ArticleStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ArticleRepository(
    private val articleDao: ArticleDao,
    private val favoriteDao: FavoriteDao,
    private val userDao: UserDao
) {

    fun getPublishedArticles(currentUserId: Long? = null): Flow<List<Article>> {
        return articleDao.getAllPublishedArticles().map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getAllArticles(): Flow<List<Article>> {
        return articleDao.getAllArticles().map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getArticleById(id: Long, currentUserId: Long? = null): Flow<Article?> {
        val articleFlow = articleDao.getArticleById(id)
        return if (currentUserId != null) {
            val isFavFlow = favoriteDao.isFavorite(currentUserId, id)
            articleFlow.combine(isFavFlow) { article, isFav ->
                article?.toDomain(isFavorite = isFav)
            }
        } else {
            articleFlow.map { it?.toDomain(isFavorite = false) }
        }
    }

    fun searchArticles(query: String): Flow<List<Article>> {
        val trimmed = query.trim()
        return if (trimmed.isBlank()) {
            getPublishedArticles()
        } else {
            articleDao.searchArticles(trimmed).map { list ->
                list.map { it.toDomain() }
            }
        }
    }

    fun getArticlesByCategory(category: String): Flow<List<Article>> {
        return articleDao.getArticlesByCategory(category).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun insertArticle(article: Article): Long {
        val entity = ArticleEntity(
            title = article.title,
            summary = article.summary,
            content = article.content,
            imageUrl = article.imageUrl,
            category = article.category,
            author = article.author,
            date = article.date,
            status = article.status.name,
            views = article.views
        )
        return articleDao.insertArticle(entity)
    }

    suspend fun updateArticle(article: Article) {
        val entity = ArticleEntity(
            id = article.id,
            title = article.title,
            summary = article.summary,
            content = article.content,
            imageUrl = article.imageUrl,
            category = article.category,
            author = article.author,
            date = article.date,
            status = article.status.name,
            views = article.views
        )
        articleDao.updateArticle(entity)
    }

    suspend fun deleteArticle(id: Long) {
        favoriteDao.deleteFavoritesForArticle(id)
        articleDao.deleteArticleById(id)
    }

    suspend fun incrementViews(id: Long) {
        articleDao.incrementViews(id)
    }

    fun getAdminStats(): Flow<AdminDashboardStats> {
        return combine(
            userDao.getUserCount(),
            articleDao.getTotalArticleCount(),
            articleDao.getPublishedCount(),
            articleDao.getDraftCount(),
            articleDao.getTotalViews()
        ) { users, totalArticles, published, drafts, views ->
            AdminDashboardStats(
                totalUsers = users,
                totalArticles = totalArticles,
                publishedArticles = published,
                draftArticles = drafts,
                totalViews = views ?: 0
            )
        }
    }

    private fun ArticleEntity.toDomain(isFavorite: Boolean = false) = Article(
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

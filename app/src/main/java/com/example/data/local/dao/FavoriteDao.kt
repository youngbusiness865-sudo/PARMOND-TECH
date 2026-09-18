package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.ArticleEntity
import com.example.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("""
        SELECT a.* FROM articles a
        INNER JOIN favorites f ON a.id = f.articleId
        WHERE f.userId = :userId AND a.status = 'PUBLISHED'
        ORDER BY f.createdAt DESC
    """)
    fun getFavoriteArticlesForUser(userId: Long): Flow<List<ArticleEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND articleId = :articleId)")
    fun isFavorite(userId: Long, articleId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE userId = :userId AND articleId = :articleId")
    suspend fun deleteFavorite(userId: Long, articleId: Long)

    @Query("DELETE FROM favorites WHERE articleId = :articleId")
    suspend fun deleteFavoritesForArticle(articleId: Long)

    @Query("SELECT COUNT(*) FROM favorites WHERE userId = :userId")
    fun getFavoriteCountForUser(userId: Long): Flow<Int>
}

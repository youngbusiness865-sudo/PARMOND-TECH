package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles WHERE status = 'PUBLISHED' ORDER BY id DESC")
    fun getAllPublishedArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles ORDER BY id DESC")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    fun getArticleById(id: Long): Flow<ArticleEntity?>

    @Query("SELECT * FROM articles WHERE status = 'PUBLISHED' AND category = :category ORDER BY id DESC")
    fun getArticlesByCategory(category: String): Flow<List<ArticleEntity>>

    @Query("""
        SELECT * FROM articles 
        WHERE status = 'PUBLISHED' AND (
            title LIKE '%' || :query || '%' OR 
            summary LIKE '%' || :query || '%' OR 
            content LIKE '%' || :query || '%' OR 
            category LIKE '%' || :query || '%'
        ) 
        ORDER BY id DESC
    """)
    fun searchArticles(query: String): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleEntity): Long

    @Update
    suspend fun updateArticle(article: ArticleEntity)

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)

    @Query("DELETE FROM articles WHERE id = :id")
    suspend fun deleteArticleById(id: Long)

    @Query("UPDATE articles SET views = views + 1 WHERE id = :id")
    suspend fun incrementViews(id: Long)

    @Query("SELECT COUNT(*) FROM articles")
    fun getTotalArticleCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM articles WHERE status = 'PUBLISHED'")
    fun getPublishedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM articles WHERE status = 'DRAFT'")
    fun getDraftCount(): Flow<Int>

    @Query("SELECT SUM(views) FROM articles")
    fun getTotalViews(): Flow<Int?>
}

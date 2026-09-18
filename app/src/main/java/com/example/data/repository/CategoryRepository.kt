package com.example.data.repository

import com.example.data.local.dao.CategoryDao
import com.example.data.local.entity.CategoryEntity
import com.example.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepository(private val categoryDao: CategoryDao) {

    fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { list ->
            list.map { Category(id = it.id, name = it.name, iconName = it.iconName, description = it.description) }
        }
    }

    suspend fun addCategory(name: String, iconName: String = "category", description: String = ""): Result<Long> {
        val cleanName = name.trim()
        if (cleanName.isBlank()) return Result.failure(Exception("O nome da categoria não pode estar vazio."))
        val entity = CategoryEntity(name = cleanName, iconName = iconName, description = description.trim())
        val id = categoryDao.insertCategory(entity)
        return Result.success(id)
    }

    suspend fun updateCategory(id: Long, name: String, iconName: String, description: String): Result<Unit> {
        val cleanName = name.trim()
        if (cleanName.isBlank()) return Result.failure(Exception("O nome da categoria não pode estar vazio."))
        val entity = CategoryEntity(id = id, name = cleanName, iconName = iconName, description = description.trim())
        categoryDao.updateCategory(entity)
        return Result.success(Unit)
    }

    suspend fun deleteCategory(id: Long) {
        categoryDao.deleteCategoryById(id)
    }
}

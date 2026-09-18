package com.example.data.repository

import com.example.data.local.SecurityUtils
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.UserEntity
import com.example.domain.model.User
import com.example.domain.model.UserRole
import com.example.domain.model.UserStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(private val userDao: UserDao) {

    suspend fun login(email: String, password: String): Result<User> {
        val cleanEmail = email.trim().lowercase()
        val userEntity = userDao.getUserByEmail(cleanEmail)
            ?: return Result.failure(Exception("Email ou senha incorretos."))

        if (userEntity.status.uppercase() == "BLOCKED") {
            return Result.failure(Exception("Esta conta está temporariamente bloqueada pela administração."))
        }

        val isValid = SecurityUtils.verifyPassword(password, userEntity.passwordHash)
        if (!isValid) {
            return Result.failure(Exception("Email ou senha incorretos."))
        }

        return Result.success(userEntity.toDomain())
    }

    suspend fun register(name: String, email: String, password: String): Result<User> {
        val cleanEmail = email.trim().lowercase()
        val cleanName = name.trim()

        if (cleanName.isBlank()) {
            return Result.failure(Exception("O nome é obrigatório."))
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            return Result.failure(Exception("Por favor, insira um endereço de email válido."))
        }
        if (password.length < 6) {
            return Result.failure(Exception("A senha deve ter pelo menos 6 caracteres."))
        }

        val existing = userDao.getUserByEmail(cleanEmail)
        if (existing != null) {
            return Result.failure(Exception("Já existe uma conta com este endereço de email."))
        }

        val hash = SecurityUtils.hashPassword(password)
        val entity = UserEntity(
            name = cleanName,
            email = cleanEmail,
            passwordHash = hash,
            role = "USER",
            createdAt = System.currentTimeMillis(),
            status = "ACTIVE"
        )

        val id = userDao.insertUser(entity)
        return Result.success(entity.copy(id = id).toDomain())
    }

    suspend fun getUserById(id: Long): User? {
        return userDao.getUserById(id)?.toDomain()
    }

    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun updateUserStatus(userId: Long, status: UserStatus) {
        userDao.updateUserStatus(userId, status.name)
    }

    suspend fun updateUserRole(userId: Long, role: UserRole) {
        userDao.updateUserRole(userId, role.name)
    }

    suspend fun updateUserName(userId: Long, newName: String): Result<Unit> {
        if (newName.isBlank()) return Result.failure(Exception("O nome não pode estar vazio."))
        val user = userDao.getUserById(userId) ?: return Result.failure(Exception("Usuário não encontrado."))
        userDao.updateUser(user.copy(name = newName.trim()))
        return Result.success(Unit)
    }

    fun getUserCount(): Flow<Int> = userDao.getUserCount()

    private fun UserEntity.toDomain() = User(
        id = id,
        name = name,
        email = email,
        role = UserRole.fromString(role),
        createdAt = createdAt,
        status = UserStatus.fromString(status)
    )
}

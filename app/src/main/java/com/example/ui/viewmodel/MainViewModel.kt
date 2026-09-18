package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.repository.ArticleRepository
import com.example.data.repository.CategoryRepository
import com.example.data.repository.FavoriteRepository
import com.example.data.repository.UserRepository
import com.example.domain.model.AdminDashboardStats
import com.example.domain.model.Article
import com.example.domain.model.ArticleStatus
import com.example.domain.model.Category
import com.example.domain.model.NotificationPlaceholder
import com.example.domain.model.User
import com.example.domain.model.UserRole
import com.example.domain.model.UserStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Splash : Screen()
    object Home : Screen()
    object Categories : Screen()
    object Search : Screen()
    object Favorites : Screen()
    object Profile : Screen()
    object Login : Screen()
    object Register : Screen()
    data class ArticleDetail(val articleId: Long) : Screen()
    object Admin : Screen()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    val userRepository = UserRepository(database.userDao())
    val articleRepository = ArticleRepository(database.articleDao(), database.favoriteDao(), database.userDao())
    val favoriteRepository = FavoriteRepository(database.favoriteDao())
    val categoryRepository = CategoryRepository(database.categoryDao())

    // App state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Splash)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    // Search query & category filter
    val searchQuery = MutableStateFlow("")
    val selectedCategoryFilter = MutableStateFlow<String?>(null)

    // Articles flow
    @OptIn(ExperimentalCoroutinesApi::class)
    val articles: StateFlow<List<Article>> = searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                articleRepository.getPublishedArticles()
            } else {
                articleRepository.searchArticles(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allArticlesForAdmin: StateFlow<List<Article>> = articleRepository.getAllArticles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUsersForAdmin: StateFlow<List<User>> = userRepository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<Category>> = categoryRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteArticles: StateFlow<List<Article>> = _currentUser
        .flatMapLatest { user ->
            if (user != null) {
                favoriteRepository.getFavoriteArticles(user.id)
            } else {
                kotlinx.coroutines.flow.flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adminStats: StateFlow<AdminDashboardStats> = articleRepository.getAdminStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdminDashboardStats())

    // Demo Notifications
    val notifications = listOf(
        NotificationPlaceholder(
            id = 1,
            title = "Bem-vindo ao PARMOND TECH!",
            message = "Explore artigos sobre Inteligência Artificial, tutoriais e inovação digital.",
            timeAgo = "Agora"
        ),
        NotificationPlaceholder(
            id = 2,
            title = "Novo artigo publicado",
            message = "Confira o novo guia: Como proteger sua conta online e dados digitais.",
            timeAgo = "Há 2 dias"
        ),
        NotificationPlaceholder(
            id = 3,
            title = "Dica de Cibersegurança",
            message = "Ative a verificação em duas etapas nas suas contas de serviços móveis.",
            timeAgo = "Há 5 dias"
        )
    )

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun showMessage(msg: String) {
        viewModelScope.launch {
            _userMessage.emit(msg)
        }
    }

    // --- Authentication ---
    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.login(email, pass)
            result.onSuccess { user ->
                _currentUser.value = user
                showMessage("Sessão iniciada com sucesso. Olá, ${user.name}!")
                onSuccess()
                navigateTo(Screen.Home)
            }.onFailure { err ->
                onError(err.message ?: "Erro ao iniciar sessão.")
            }
        }
    }

    fun register(name: String, email: String, pass: String, confirmPass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (pass != confirmPass) {
            onError("As senhas não coincidem.")
            return
        }
        viewModelScope.launch {
            val result = userRepository.register(name, email, pass)
            result.onSuccess { user ->
                _currentUser.value = user
                showMessage("Conta criada com sucesso! Bem-vindo(a), ${user.name}.")
                onSuccess()
                navigateTo(Screen.Home)
            }.onFailure { err ->
                onError(err.message ?: "Erro ao criar conta.")
            }
        }
    }

    fun logout() {
        val name = _currentUser.value?.name ?: ""
        _currentUser.value = null
        showMessage("Você saiu da sua conta.")
        navigateTo(Screen.Home)
    }

    fun updateProfile(newName: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val result = userRepository.updateUserName(user.id, newName)
            result.onSuccess {
                _currentUser.value = user.copy(name = newName.trim())
                showMessage("Perfil atualizado com sucesso!")
                onSuccess()
            }.onFailure { err ->
                onError(err.message ?: "Erro ao atualizar perfil.")
            }
        }
    }

    // --- Favorites ---
    fun toggleFavorite(articleId: Long, isCurrentlyFav: Boolean) {
        val user = _currentUser.value
        if (user == null) {
            showMessage("Faça login para salvar artigos nos seus favoritos.")
            navigateTo(Screen.Login)
            return
        }
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(user.id, articleId, isCurrentlyFav)
            if (isCurrentlyFav) {
                showMessage("Removido dos favoritos.")
            } else {
                showMessage("Artigo adicionado aos favoritos!")
            }
        }
    }

    fun incrementArticleViews(articleId: Long) {
        viewModelScope.launch {
            articleRepository.incrementViews(articleId)
        }
    }

    // --- Admin Actions ---
    fun createArticle(
        title: String,
        summary: String,
        content: String,
        category: String,
        author: String,
        status: ArticleStatus,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (currentUser.value?.role != UserRole.ADMIN) {
            onError("Ação não autorizada.")
            return
        }
        if (title.isBlank() || summary.isBlank() || content.isBlank()) {
            onError("Por favor preencha todos os campos obrigatórios.")
            return
        }
        viewModelScope.launch {
            val dateStr = "Hoje"
            val article = Article(
                id = 0,
                title = title.trim(),
                summary = summary.trim(),
                content = content.trim(),
                imageUrl = "",
                category = category,
                author = if (author.isBlank()) "PARMOND TECH" else author.trim(),
                date = dateStr,
                status = status,
                views = 0
            )
            articleRepository.insertArticle(article)
            showMessage("Artigo salvo com sucesso!")
            onSuccess()
        }
    }

    fun updateArticle(
        article: Article,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (currentUser.value?.role != UserRole.ADMIN) {
            onError("Ação não autorizada.")
            return
        }
        viewModelScope.launch {
            articleRepository.updateArticle(article)
            showMessage("Artigo atualizado com sucesso!")
            onSuccess()
        }
    }

    fun deleteArticle(articleId: Long) {
        if (currentUser.value?.role != UserRole.ADMIN) return
        viewModelScope.launch {
            articleRepository.deleteArticle(articleId)
            showMessage("Artigo excluído.")
        }
    }

    fun toggleArticlePublish(article: Article) {
        if (currentUser.value?.role != UserRole.ADMIN) return
        val newStatus = if (article.status == ArticleStatus.PUBLISHED) ArticleStatus.DRAFT else ArticleStatus.PUBLISHED
        viewModelScope.launch {
            articleRepository.updateArticle(article.copy(status = newStatus))
            val msg = if (newStatus == ArticleStatus.PUBLISHED) "Artigo publicado!" else "Artigo movido para rascunho."
            showMessage(msg)
        }
    }

    fun toggleUserBlock(user: User) {
        if (currentUser.value?.role != UserRole.ADMIN) return
        val newStatus = if (user.status == UserStatus.ACTIVE) UserStatus.BLOCKED else UserStatus.ACTIVE
        viewModelScope.launch {
            userRepository.updateUserStatus(user.id, newStatus)
            val label = if (newStatus == UserStatus.BLOCKED) "bloqueado" else "desbloqueado"
            showMessage("Usuário ${user.name} foi $label.")
        }
    }

    fun toggleUserAdminRole(user: User) {
        if (currentUser.value?.role != UserRole.ADMIN) return
        val newRole = if (user.role == UserRole.ADMIN) UserRole.USER else UserRole.ADMIN
        viewModelScope.launch {
            userRepository.updateUserRole(user.id, newRole)
            val label = if (newRole == UserRole.ADMIN) "Administrador" else "Usuário Comum"
            showMessage("Papel de ${user.name} alterado para $label.")
        }
    }

    fun createCategory(name: String, description: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (currentUser.value?.role != UserRole.ADMIN) return
        viewModelScope.launch {
            val result = categoryRepository.addCategory(name, description = description)
            result.onSuccess {
                showMessage("Categoria criada com sucesso!")
                onSuccess()
            }.onFailure {
                onError(it.message ?: "Erro ao criar categoria.")
            }
        }
    }

    fun deleteCategory(categoryId: Long) {
        if (currentUser.value?.role != UserRole.ADMIN) return
        viewModelScope.launch {
            categoryRepository.deleteCategory(categoryId)
            showMessage("Categoria removida.")
        }
    }
}

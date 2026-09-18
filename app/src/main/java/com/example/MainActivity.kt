package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.model.Article
import com.example.ui.components.NotificationsDialog
import com.example.ui.components.ParmondBottomNavigationBar
import com.example.ui.components.ParmondNavigationRail
import com.example.ui.components.ParmondTopBar
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.ArticleDetailScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.ParmondTechTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val isDarkMode by viewModel.isDarkMode.collectAsState()

            ParmondTechTheme(darkTheme = isDarkMode) {
                ParmondTechApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ParmondTechApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val articles by viewModel.articles.collectAsState()
    val allArticlesForAdmin by viewModel.allArticlesForAdmin.collectAsState()
    val allUsersForAdmin by viewModel.allUsersForAdmin.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val favoriteArticles by viewModel.favoriteArticles.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategoryFilter.collectAsState()
    val adminStats by viewModel.adminStats.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.userMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Android back handling
    BackHandler(enabled = currentScreen !is Screen.Home && currentScreen !is Screen.Splash) {
        when (currentScreen) {
            is Screen.ArticleDetail,
            is Screen.Categories,
            is Screen.Search,
            is Screen.Favorites,
            is Screen.Profile,
            is Screen.Login,
            is Screen.Register,
            is Screen.Admin -> viewModel.navigateTo(Screen.Home)
            else -> {}
        }
    }

    // Splash screen is full screen without bars
    if (currentScreen is Screen.Splash) {
        SplashScreen(
            onContinue = { viewModel.navigateTo(Screen.Home) }
        )
        return
    }

    val isTopLevelScreen = currentScreen is Screen.Home ||
            currentScreen is Screen.Categories ||
            currentScreen is Screen.Search ||
            currentScreen is Screen.Favorites ||
            currentScreen is Screen.Profile

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        val isWideScreen = maxWidth >= 600.dp

        Row(modifier = Modifier.fillMaxSize()) {
            // Responsive: Desktop / Tablet Navigation Rail
            if (isWideScreen && isTopLevelScreen) {
                ParmondNavigationRail(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }

            Scaffold(
                modifier = Modifier.weight(1f).fillMaxSize(),
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    if (isTopLevelScreen) {
                        ParmondTopBar(
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { viewModel.toggleDarkMode() },
                            onNotificationsClick = { showNotificationsDialog = true },
                            currentUser = currentUser,
                            onProfileClick = { viewModel.navigateTo(Screen.Profile) }
                        )
                    }
                },
                bottomBar = {
                    // Mobile Bottom Navigation Bar (hidden on wide screen or sub-screens)
                    if (!isWideScreen && isTopLevelScreen) {
                        ParmondBottomNavigationBar(
                            currentScreen = currentScreen,
                            onNavigate = { viewModel.navigateTo(it) }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (val screen = currentScreen) {
                        is Screen.Splash -> { /* Handled above */ }
                        is Screen.Home -> {
                            HomeScreen(
                                currentUser = currentUser,
                                articles = articles,
                                favoriteArticles = favoriteArticles,
                                searchQuery = searchQuery,
                                onSearchQueryChange = { viewModel.searchQuery.value = it },
                                selectedCategory = selectedCategory,
                                onSelectCategory = { viewModel.selectedCategoryFilter.value = it },
                                onReadArticle = { id -> viewModel.navigateTo(Screen.ArticleDetail(id)) },
                                onToggleFavorite = { id, isFav -> viewModel.toggleFavorite(id, isFav) },
                                onOpenSearchScreen = { viewModel.navigateTo(Screen.Search) }
                            )
                        }
                        is Screen.Categories -> {
                            CategoriesScreen(
                                categories = categories,
                                articles = articles,
                                onSelectCategory = { catName ->
                                    viewModel.selectedCategoryFilter.value = catName
                                    viewModel.navigateTo(Screen.Home)
                                }
                            )
                        }
                        is Screen.Search -> {
                            SearchScreen(
                                query = searchQuery,
                                onQueryChange = { viewModel.searchQuery.value = it },
                                articles = articles,
                                favoriteArticles = favoriteArticles,
                                onReadArticle = { id -> viewModel.navigateTo(Screen.ArticleDetail(id)) },
                                onToggleFavorite = { id, isFav -> viewModel.toggleFavorite(id, isFav) }
                            )
                        }
                        is Screen.Favorites -> {
                            FavoritesScreen(
                                currentUser = currentUser,
                                favoriteArticles = favoriteArticles,
                                onReadArticle = { id -> viewModel.navigateTo(Screen.ArticleDetail(id)) },
                                onToggleFavorite = { id, isFav -> viewModel.toggleFavorite(id, isFav) },
                                onGoToLogin = { viewModel.navigateTo(Screen.Login) },
                                onExploreArticles = { viewModel.navigateTo(Screen.Home) }
                            )
                        }
                        is Screen.Profile -> {
                            ProfileScreen(
                                currentUser = currentUser,
                                favoriteCount = favoriteArticles.size,
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { viewModel.toggleDarkMode() },
                                onUpdateProfile = { newName, onSuccess, onError ->
                                    viewModel.updateProfile(newName, onSuccess, onError)
                                },
                                onLogout = { viewModel.logout() },
                                onGoToLogin = { viewModel.navigateTo(Screen.Login) },
                                onGoToAdmin = { viewModel.navigateTo(Screen.Admin) }
                            )
                        }
                        is Screen.ArticleDetail -> {
                            val article = articles.find { it.id == screen.articleId }
                                ?: allArticlesForAdmin.find { it.id == screen.articleId }
                            val isFav = favoriteArticles.any { it.id == screen.articleId }
                            ArticleDetailScreen(
                                article = article,
                                isFavorite = isFav,
                                onToggleFavorite = { viewModel.toggleFavorite(screen.articleId, isFav) },
                                onBack = { viewModel.navigateTo(Screen.Home) },
                                onIncrementView = { viewModel.incrementArticleViews(screen.articleId) }
                            )
                        }
                        is Screen.Login -> {
                            LoginScreen(
                                onLogin = { email, pass, onSuccess, onError ->
                                    viewModel.login(email, pass, onSuccess, onError)
                                },
                                onNavigateToRegister = { viewModel.navigateTo(Screen.Register) },
                                onBack = { viewModel.navigateTo(Screen.Home) }
                            )
                        }
                        is Screen.Register -> {
                            RegisterScreen(
                                onRegister = { name, email, pass, confirm, onSuccess, onError ->
                                    viewModel.register(name, email, pass, confirm, onSuccess, onError)
                                },
                                onNavigateToLogin = { viewModel.navigateTo(Screen.Login) },
                                onBack = { viewModel.navigateTo(Screen.Home) }
                            )
                        }
                        is Screen.Admin -> {
                            AdminScreen(
                                currentUser = currentUser,
                                stats = adminStats,
                                articles = allArticlesForAdmin,
                                users = allUsersForAdmin,
                                categories = categories,
                                onBack = { viewModel.navigateTo(Screen.Profile) },
                                onCreateArticle = { title, summary, content, category, author, status, onSuccess, onError ->
                                    viewModel.createArticle(title, summary, content, category, author, status, onSuccess, onError)
                                },
                                onUpdateArticle = { art, onSuccess, onError ->
                                    viewModel.updateArticle(art, onSuccess, onError)
                                },
                                onDeleteArticle = { id -> viewModel.deleteArticle(id) },
                                onToggleArticlePublish = { art -> viewModel.toggleArticlePublish(art) },
                                onToggleUserBlock = { u -> viewModel.toggleUserBlock(u) },
                                onToggleUserRole = { u -> viewModel.toggleUserAdminRole(u) },
                                onCreateCategory = { name, desc, onSuccess, onError ->
                                    viewModel.createCategory(name, desc, onSuccess, onError)
                                },
                                onDeleteCategory = { id -> viewModel.deleteCategory(id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showNotificationsDialog) {
        NotificationsDialog(
            notifications = viewModel.notifications,
            onDismiss = { showNotificationsDialog = false }
        )
    }
}

package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AdminDashboardStats
import com.example.domain.model.Article
import com.example.domain.model.ArticleStatus
import com.example.domain.model.Category
import com.example.domain.model.User
import com.example.domain.model.UserRole
import com.example.domain.model.UserStatus
import com.example.ui.theme.ParmondAccentAmber
import com.example.ui.theme.ParmondAccentCyan
import com.example.ui.theme.ParmondAccentGreen
import com.example.ui.theme.ParmondAccentPurple
import com.example.ui.theme.ParmondAccentRose
import com.example.ui.theme.ParmondPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    currentUser: User?,
    stats: AdminDashboardStats,
    articles: List<Article>,
    users: List<User>,
    categories: List<Category>,
    onBack: () -> Unit,
    onCreateArticle: (String, String, String, String, String, ArticleStatus, () -> Unit, (String) -> Unit) -> Unit,
    onUpdateArticle: (Article, () -> Unit, (String) -> Unit) -> Unit,
    onDeleteArticle: (Long) -> Unit,
    onToggleArticlePublish: (Article) -> Unit,
    onToggleUserBlock: (User) -> Unit,
    onToggleUserRole: (User) -> Unit,
    onCreateCategory: (String, String, () -> Unit, (String) -> Unit) -> Unit,
    onDeleteCategory: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Protection: Only users with role ADMIN can access
    if (currentUser == null || currentUser.role != UserRole.ADMIN) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("admin_unauthorized_screen"),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.errorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Acesso Restrito",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "O Painel Administrativo do PARMOND TECH é protegido e requer credenciais de Administrador com permissões elevadas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onBack,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Retornar ao Aplicativo")
                    }
                }
            }
        }
        return
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Dashboard", "Artigos", "Usuários", "Categorias", "Config")

    // State for Article Form Dialog
    var showArticleDialog by remember { mutableStateOf(false) }
    var editingArticle by remember { mutableStateOf<Article?>(null) }
    var articleTitle by remember { mutableStateOf("") }
    var articleSummary by remember { mutableStateOf("") }
    var articleContent by remember { mutableStateOf("") }
    var articleCategory by remember { mutableStateOf(categories.firstOrNull()?.name ?: "Tecnologia") }
    var articleAuthor by remember { mutableStateOf("PARMOND TECH") }
    var articleStatus by remember { mutableStateOf(ArticleStatus.PUBLISHED) }
    var articleError by remember { mutableStateOf<String?>(null) }

    // State for Category Dialog
    var showCategoryDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }
    var newCategoryDesc by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = modifier.testTag("admin_screen"),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("admin_back_button")
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ParmondAccentAmber.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = ParmondAccentAmber,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "PARMOND TECH ADMIN",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Painel de Gestão e Publicação",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                PrimaryTabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                            modifier = Modifier.testTag("admin_tab_$index")
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (selectedTab == 1) {
                FloatingActionButton(
                    onClick = {
                        editingArticle = null
                        articleTitle = ""
                        articleSummary = ""
                        articleContent = ""
                        articleCategory = categories.firstOrNull()?.name ?: "Tecnologia"
                        articleAuthor = "PARMOND TECH"
                        articleStatus = ArticleStatus.PUBLISHED
                        articleError = null
                        showArticleDialog = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("admin_fab_add_article")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Criar Artigo")
                }
            } else if (selectedTab == 3) {
                FloatingActionButton(
                    onClick = {
                        newCategoryName = ""
                        newCategoryDesc = ""
                        categoryError = null
                        showCategoryDialog = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("admin_fab_add_category")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Nova Categoria")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> AdminDashboardTab(stats)
                1 -> AdminArticlesTab(
                    articles = articles,
                    onEdit = { art ->
                        editingArticle = art
                        articleTitle = art.title
                        articleSummary = art.summary
                        articleContent = art.content
                        articleCategory = art.category
                        articleAuthor = art.author
                        articleStatus = art.status
                        articleError = null
                        showArticleDialog = true
                    },
                    onDelete = onDeleteArticle,
                    onTogglePublish = onToggleArticlePublish
                )
                2 -> AdminUsersTab(
                    users = users,
                    currentUserId = currentUser.id,
                    onToggleBlock = onToggleUserBlock,
                    onToggleRole = onToggleUserRole
                )
                3 -> AdminCategoriesTab(
                    categories = categories,
                    onDeleteCategory = onDeleteCategory
                )
                4 -> AdminSettingsTab()
            }
        }
    }

    // Article Create/Edit Dialog
    if (showArticleDialog) {
        var categoryExpanded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showArticleDialog = false },
            title = {
                Text(if (editingArticle == null) "Novo Artigo" else "Editar Artigo")
            },
            text = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = articleTitle,
                        onValueChange = { articleTitle = it },
                        label = { Text("Título do artigo *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("admin_article_title_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Category dropdown
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded }
                    ) {
                        OutlinedTextField(
                            value = articleCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoria") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.name) },
                                    onClick = {
                                        articleCategory = cat.name
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = articleAuthor,
                        onValueChange = { articleAuthor = it },
                        label = { Text("Autor") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = articleSummary,
                        onValueChange = { articleSummary = it },
                        label = { Text("Resumo do artigo *") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth().testTag("admin_article_summary_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = articleContent,
                        onValueChange = { articleContent = it },
                        label = { Text("Conteúdo completo *") },
                        minLines = 5,
                        modifier = Modifier.fillMaxWidth().testTag("admin_article_content_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Status da Publicação:", style = MaterialTheme.typography.bodyMedium)
                        Button(
                            onClick = {
                                articleStatus = if (articleStatus == ArticleStatus.PUBLISHED) ArticleStatus.DRAFT else ArticleStatus.PUBLISHED
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (articleStatus == ArticleStatus.PUBLISHED) ParmondAccentGreen else ParmondAccentAmber
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(if (articleStatus == ArticleStatus.PUBLISHED) "Publicado" else "Rascunho")
                        }
                    }

                    if (articleError != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = articleError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (articleTitle.isBlank() || articleSummary.isBlank() || articleContent.isBlank()) {
                            articleError = "Preencha todos os campos obrigatórios (*)"
                            return@Button
                        }
                        if (editingArticle == null) {
                            onCreateArticle(
                                articleTitle,
                                articleSummary,
                                articleContent,
                                articleCategory,
                                articleAuthor,
                                articleStatus,
                                { showArticleDialog = false },
                                { articleError = it }
                            )
                        } else {
                            val updated = editingArticle!!.copy(
                                title = articleTitle.trim(),
                                summary = articleSummary.trim(),
                                content = articleContent.trim(),
                                category = articleCategory,
                                author = articleAuthor.trim(),
                                status = articleStatus
                            )
                            onUpdateArticle(
                                updated,
                                { showArticleDialog = false },
                                { articleError = it }
                            )
                        }
                    },
                    modifier = Modifier.testTag("admin_article_save_button")
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showArticleDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Category Dialog
    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showCategoryDialog = false },
            title = { Text("Nova Categoria") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Nome da Categoria") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("admin_category_name_input")
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newCategoryDesc,
                        onValueChange = { newCategoryDesc = it },
                        label = { Text("Descrição (opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (categoryError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = categoryError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCreateCategory(
                            newCategoryName,
                            newCategoryDesc,
                            { showCategoryDialog = false },
                            { categoryError = it }
                        )
                    },
                    modifier = Modifier.testTag("admin_category_save_button")
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCategoryDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// 1. Dashboard Tab with 5 requested metrics
@Composable
fun AdminDashboardTab(stats: AdminDashboardStats) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_dashboard_tab"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Métricas Gerais",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Visão geral de atividade e alcance da plataforma PARMOND TECH.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AdminStatCard(
                    title = "Total Usuários",
                    value = "${stats.totalUsers}",
                    icon = Icons.Default.People,
                    color = ParmondAccentCyan,
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    title = "Total Artigos",
                    value = "${stats.totalArticles}",
                    icon = Icons.Default.Article,
                    color = ParmondAccentPurple,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AdminStatCard(
                    title = "Publicados",
                    value = "${stats.publishedArticles}",
                    icon = Icons.Default.CheckCircle,
                    color = ParmondAccentGreen,
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    title = "Rascunhos",
                    value = "${stats.draftArticles}",
                    icon = Icons.Default.Drafts,
                    color = ParmondAccentAmber,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            AdminStatCard(
                title = "Total de Visualizações",
                value = "${stats.totalViews} views",
                icon = Icons.Default.Visibility,
                color = ParmondPrimary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// 2. Artigos Tab
@Composable
fun AdminArticlesTab(
    articles: List<Article>,
    onEdit: (Article) -> Unit,
    onDelete: (Long) -> Unit,
    onTogglePublish: (Article) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_articles_tab"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Gerenciar Artigos (${articles.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(articles, key = { it.id }) { art ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = art.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (art.status == ArticleStatus.PUBLISHED) ParmondAccentGreen.copy(alpha = 0.2f)
                                    else ParmondAccentAmber.copy(alpha = 0.2f)
                                )
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (art.status == ArticleStatus.PUBLISHED) "Publicado" else "Rascunho",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (art.status == ArticleStatus.PUBLISHED) ParmondAccentGreen else ParmondAccentAmber
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${art.category} • ${art.views} views • Por ${art.author}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { onTogglePublish(art) }) {
                            Text(if (art.status == ArticleStatus.PUBLISHED) "Despublicar" else "Publicar")
                        }
                        IconButton(onClick = { onEdit(art) }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { onDelete(art.id) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Excluir",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

// 3. Usuários Tab
@Composable
fun AdminUsersTab(
    users: List<User>,
    currentUserId: Long,
    onToggleBlock: (User) -> Unit,
    onToggleRole: (User) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_users_tab"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Gerenciar Usuários (${users.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(users, key = { it.id }) { u ->
            val isCurrent = u.id == currentUserId
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = u.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                if (isCurrent) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "(Você)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Text(
                                text = u.email,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (u.role == UserRole.ADMIN) ParmondAccentAmber.copy(alpha = 0.2f)
                                        else MaterialTheme.colorScheme.primaryContainer
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (u.role == UserRole.ADMIN) "ADMIN" else "USER",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (u.role == UserRole.ADMIN) ParmondAccentAmber else MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (u.status == UserStatus.ACTIVE) ParmondAccentGreen.copy(alpha = 0.2f)
                                        else ParmondAccentRose.copy(alpha = 0.2f)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (u.status == UserStatus.ACTIVE) "Ativo" else "Bloqueado",
                                    fontSize = 10.sp,
                                    color = if (u.status == UserStatus.ACTIVE) ParmondAccentGreen else ParmondAccentRose
                                )
                            }
                        }
                    }

                    if (!isCurrent) {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = { onToggleRole(u) }) {
                                Text(if (u.role == UserRole.ADMIN) "Tornar Usuário" else "Tornar Admin")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(
                                onClick = { onToggleBlock(u) },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = if (u.status == UserStatus.ACTIVE) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
                                )
                            ) {
                                Text(if (u.status == UserStatus.ACTIVE) "Bloquear" else "Desbloquear")
                            }
                        }
                    }
                }
            }
        }
    }
}

// 4. Categorias Tab
@Composable
fun AdminCategoriesTab(
    categories: List<Category>,
    onDeleteCategory: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_categories_tab"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Gerenciar Categorias (${categories.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(categories, key = { it.id }) { cat ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = cat.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        if (cat.description.isNotBlank()) {
                            Text(text = cat.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    IconButton(onClick = { onDeleteCategory(cat.id) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

// 5. Configurações Tab
@Composable
fun AdminSettingsTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("admin_settings_tab")
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Informações do Sistema", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                ConfigRow(label = "Nome da Aplicação", value = "PARMOND TECH")
                ConfigRow(label = "Descrição", value = "Tecnologia, Inteligência Artificial e conhecimento digital em um só lugar.")
                ConfigRow(label = "Idioma Padrão", value = "Português (Moçambique & Comunidade Lusófona)")
                ConfigRow(label = "Banco de Dados", value = "Room SQLite (Local First) + KSP")
                ConfigRow(label = "Segurança", value = "Criptografia SHA-256 (Zero senhas em texto plano)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Arquitetura e Futuras Integrações", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "• Firebase Authentication: A camada de repositório (UserRepository) já isola a lógica de autenticação. Para conectar ao Firebase em produção, basta adicionar google-services.json e alternar a implementação do repositório.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Firebase Cloud Messaging (FCM): Os modelos e diálogos de notificação estão prontos para receber mensagens push sobre novos artigos e comunicados.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Google AdMob / Publicidade Própria: Os componentes AdBannerPlaceholder já reservam os espaços dedicados no topo e no final de artigos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun ConfigRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text(text = value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
    }
}

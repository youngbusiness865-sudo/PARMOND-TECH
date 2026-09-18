package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Article
import com.example.domain.model.User
import com.example.ui.components.AdBannerPlaceholder
import com.example.ui.components.ArticleCard
import com.example.ui.components.CategoryQuickCard
import com.example.ui.theme.ParmondAccentAmber
import com.example.ui.theme.ParmondAccentCyan
import com.example.ui.theme.ParmondAccentGreen
import com.example.ui.theme.ParmondAccentPurple
import com.example.ui.theme.ParmondPrimary

@Composable
fun HomeScreen(
    currentUser: User?,
    articles: List<Article>,
    favoriteArticles: List<Article>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String?,
    onSelectCategory: (String?) -> Unit,
    onReadArticle: (Long) -> Unit,
    onToggleFavorite: (Long, Boolean) -> Unit,
    onOpenSearchScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val favIds = favoriteArticles.map { it.id }.toSet()

    val filteredArticles = articles.filter { article ->
        val matchesCategory = selectedCategory == null || article.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                article.title.contains(searchQuery, ignoreCase = true) ||
                article.summary.contains(searchQuery, ignoreCase = true) ||
                article.content.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Welcome Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_welcome_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                    ParmondAccentCyan.copy(alpha = 0.08f)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = if (currentUser != null) "Olá, ${currentUser.name}! 👋" else "Olá! 👋",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Bem-vindo ao PARMOND TECH",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tecnologia, Inteligência Artificial e conhecimento digital em um só lugar.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Search Field Trigger
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text("🔍 Pesquisar tecnologia, IA e tutoriais...")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Limpar busca")
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_search_input")
            )
        }

        // 4 Key Category Cards
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Categorias Principais",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (selectedCategory != null) {
                        Text(
                            text = "Limpar filtro",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clickable { onSelectCategory(null) }
                                .padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CategoryQuickCard(
                        title = "🤖 IA",
                        icon = Icons.Default.SmartToy,
                        accentColor = ParmondAccentCyan,
                        onClick = {
                            onSelectCategory(if (selectedCategory == "Inteligência Artificial") null else "Inteligência Artificial")
                        },
                        modifier = Modifier.weight(1f)
                    )
                    CategoryQuickCard(
                        title = "💻 Tech",
                        icon = Icons.Default.Computer,
                        accentColor = ParmondAccentPurple,
                        onClick = {
                            onSelectCategory(if (selectedCategory == "Tecnologia") null else "Tecnologia")
                        },
                        modifier = Modifier.weight(1f)
                    )
                    CategoryQuickCard(
                        title = "📱 Apps",
                        icon = Icons.Default.PhoneAndroid,
                        accentColor = ParmondAccentGreen,
                        onClick = {
                            onSelectCategory(if (selectedCategory == "Aplicativos") null else "Aplicativos")
                        },
                        modifier = Modifier.weight(1f)
                    )
                    CategoryQuickCard(
                        title = "📚 Tutoriais",
                        icon = Icons.Default.MenuBook,
                        accentColor = ParmondAccentAmber,
                        onClick = {
                            onSelectCategory(if (selectedCategory == "Tutoriais") null else "Tutoriais")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Ad Banner Placement 1
        item {
            AdBannerPlaceholder(locationLabel = "Home Banner")
        }

        // Section Title: Artigos Recentes
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedCategory != null) "Artigos em $selectedCategory" else "Artigos recentes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${filteredArticles.size} artigo(s)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Articles List
        if (filteredArticles.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nenhum artigo encontrado.",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tente outra pesquisa ou remova os filtros ativos.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredArticles, key = { it.id }) { article ->
                val isFav = favIds.contains(article.id)
                ArticleCard(
                    article = article,
                    isFavorite = isFav,
                    onReadMore = { onReadArticle(article.id) },
                    onToggleFavorite = { onToggleFavorite(article.id, isFav) }
                )
            }
        }
    }
}

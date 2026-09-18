package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import com.example.ui.viewmodel.Screen

data class NavDestination(
    val title: String,
    val screen: Screen,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

val NAV_ITEMS = listOf(
    NavDestination(
        title = "Início",
        screen = Screen.Home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        testTag = "nav_item_home"
    ),
    NavDestination(
        title = "Categorias",
        screen = Screen.Categories,
        selectedIcon = Icons.Filled.Category,
        unselectedIcon = Icons.Outlined.Category,
        testTag = "nav_item_categories"
    ),
    NavDestination(
        title = "Pesquisar",
        screen = Screen.Search,
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Filled.Search,
        testTag = "nav_item_search"
    ),
    NavDestination(
        title = "Favoritos",
        screen = Screen.Favorites,
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Filled.FavoriteBorder,
        testTag = "nav_item_favorites"
    ),
    NavDestination(
        title = "Perfil",
        screen = Screen.Profile,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Filled.PersonOutline,
        testTag = "nav_item_profile"
    )
)

@Composable
fun ParmondBottomNavigationBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("parmond_bottom_navigation_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        NAV_ITEMS.forEach { item ->
            val selected = currentScreen::class == item.screen::class
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}

@Composable
fun ParmondNavigationRail(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier.testTag("parmond_navigation_rail"),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        NAV_ITEMS.forEach { item ->
            val selected = currentScreen::class == item.screen::class
            NavigationRailItem(
                selected = selected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("${item.testTag}_rail")
            )
        }
    }
}

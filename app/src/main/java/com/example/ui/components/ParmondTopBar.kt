package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.User
import com.example.domain.model.UserRole
import com.example.ui.theme.ParmondAccentAmber
import com.example.ui.theme.ParmondAccentCyan
import com.example.ui.theme.ParmondPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParmondTopBar(
    title: String? = null,
    showBackButton: Boolean = false,
    onBack: () -> Unit = {},
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onNotificationsClick: () -> Unit,
    currentUser: User?,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("parmond_top_bar"),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left area: Back button OR Brand Logo
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showBackButton) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("top_bar_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }

                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    // PARMOND TECH Brand Monogram
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        ParmondPrimary,
                                        ParmondAccentCyan
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "P",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "PARMOND",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "TECH",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = ParmondAccentCyan,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Text(
                            text = "Inovação & IA",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            // Right area: Actions
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dark / Light Mode Toggle
                IconButton(
                    onClick = onToggleDarkMode,
                    modifier = Modifier.testTag("toggle_dark_mode_button")
                ) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = if (isDarkMode) "Modo Claro" else "Modo Escuro",
                        tint = if (isDarkMode) ParmondAccentAmber else MaterialTheme.colorScheme.onSurface
                    )
                }

                // Notification Bell with Badge
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier.testTag("notifications_button")
                ) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = ParmondAccentCyan,
                                contentColor = Color.Black
                            ) {
                                Text("3", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificações",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Profile / Login indicator
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = onProfileClick)
                        .background(
                            if (currentUser?.role == UserRole.ADMIN) {
                                ParmondAccentAmber.copy(alpha = 0.2f)
                            } else {
                                MaterialTheme.colorScheme.primaryContainer
                            }
                        )
                        .padding(6.dp)
                        .testTag("top_bar_profile_avatar"),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentUser != null) {
                        if (currentUser.role == UserRole.ADMIN) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Admin",
                                tint = ParmondAccentAmber,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(
                                text = currentUser.name.take(1).uppercase(),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Entrar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

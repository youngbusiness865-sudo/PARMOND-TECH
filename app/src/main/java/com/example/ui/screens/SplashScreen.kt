package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ParmondAccentCyan
import com.example.ui.theme.ParmondDarkBg
import com.example.ui.theme.ParmondNavy
import com.example.ui.theme.ParmondPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing))
        scale.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing))
        delay(2200)
        onContinue()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        ParmondNavy,
                        ParmondDarkBg,
                        Color(0xFF040D21)
                    )
                )
            )
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(32.dp)
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            // Futuristic Monogram Box
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(26.dp))
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
                    fontWeight = FontWeight.Black,
                    fontSize = 52.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Brand Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "PARMOND",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "TECH",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = ParmondAccentCyan,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Subtitle Tagline
            Text(
                text = "Explore o mundo da tecnologia.",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = ParmondAccentCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "IA, Inovação e Conhecimento Digital",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF64748B),
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Manual skip button
            Button(
                onClick = onContinue,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ParmondPrimary.copy(alpha = 0.85f),
                    contentColor = Color.White
                ),
                modifier = Modifier.testTag("splash_continue_button")
            ) {
                Text("Entrar no App", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

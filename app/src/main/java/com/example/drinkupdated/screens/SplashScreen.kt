package com.example.drinkupdated.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.drinkupdated.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val scale = remember { Animatable(0.6f) }
    val rotation = remember { Animatable(0f) }
    val offsetY = remember { Animatable(100f) }

    LaunchedEffect(true) {
        coroutineScope {
            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(1000, easing = FastOutSlowInEasing)
                )
            }
            launch {
                rotation.animateTo(
                    targetValue = 360f,
                    animationSpec = tween(2000, easing = LinearEasing)
                )
            }
            launch {
                offsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }

        delay(2500) // 2.5 sekundy splash
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.drinkicon),
            contentDescription = "Drink Logo",
            modifier = Modifier
                .size(150.dp)
                .graphicsLayer(
                    rotationZ = rotation.value,
                    translationY = offsetY.value
                )
                .scale(scale.value)
        )
    }
}

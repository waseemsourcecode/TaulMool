package com.greenbit.taulmool
import   androidx.compose.ui.platform.LocalDensity
import androidx.compose.animation.core.LinearEasing

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreenContent()
        }
    }
}

@Composable
fun SplashScreenContent() {
    // Animate and show logo
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            KiranaToolLogoAnimated()
        }
    }
    // Navigate to MainActivity after 2 seconds
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        delay(2000)
        context.startActivity(Intent(context, MainActivity::class.java))
        if (context is ComponentActivity) context.finish()
    }
}
@Composable
fun KiranaToolLogoAnimated() {
    // Animatable for scale and alpha
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    // Launch animation once
    LaunchedEffect(Unit) {
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
            )
        }
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
            )
        }
    }

    Text(
        text = "KiranaTool",
        fontSize = 64.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 6.sp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                cameraDistance = 16f // keep a bit of depth
            }
            .alpha(alpha.value)
            .shadow(12.dp, ambientColor = MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun KiranaToolLogoAnimatedSpin() {
    val infiniteTransition = rememberInfiniteTransition(label = "logoAnimation")

    // Gentle pulsing scale
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "scaleAnim"
    )

    // Smooth color shifting between theme primary and secondary
    val color by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.primary,
        targetValue = MaterialTheme.colorScheme.secondary,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "colorAnim"
    )

    // Continuous horizontal spin
    val rotationY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotationAnim"
    )

    // Add subtle rotation on X axis for depth
    val rotationX by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "tiltAnim"
    )

    // Camera distance for 3D look
    val density = LocalDensity.current
    val cameraDistancePx = with(density) { 16.dp.toPx() }

    Text(
        text = "KiranaTool",
        fontSize = 56.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 6.sp,
        color = color,
        modifier = Modifier
            .scale(scale)
            .graphicsLayer {
                this.rotationY = rotationY   // use animated val
                this.rotationX = rotationX   // tilt animation
                cameraDistance = cameraDistancePx
            }

            .shadow(
                elevation = 12.dp,
                ambientColor = color.copy(alpha = 0.6f),
                spotColor = color.copy(alpha = 0.8f)
            )
    )
}

//@Composable
//fun KiranaToolLogoAnimated() {
//    // Animate scale and color
//    val infiniteTransition = rememberInfiniteTransition()
//    val scale by infiniteTransition.animateFloat(
//        initialValue = 0.8f,
//        targetValue = 1.2f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1200, easing = FastOutSlowInEasing),
//            repeatMode = RepeatMode.Reverse
//        )
//    )
//    val color by infiniteTransition.animateColor(
//        initialValue = MaterialTheme.colorScheme.primary,
//        targetValue = MaterialTheme.colorScheme.secondary,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1200, easing = FastOutSlowInEasing),
//            repeatMode = RepeatMode.Reverse
//        )
//    )
//    Text(
//        text = "KiranaTool",
//        fontSize = 64.sp,
//        fontWeight = FontWeight.ExtraBold,
//        letterSpacing = 6.sp,
//        color = color,
//        modifier = Modifier
//            .scale(scale)
//            .shadow(8.dp, ambientColor = color, spotColor = color)
//    )
//}

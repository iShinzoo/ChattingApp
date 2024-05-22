package com.example.chatterbox.ui.screen.common

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatterbox.R
import com.example.chatterbox.ui.theme.ChatterBoxTheme

@RequiresApi(Build.VERSION_CODES.S)
private fun getRenderEffect(): android.graphics.RenderEffect {
    val blurEffect = android.graphics.RenderEffect
        .createBlurEffect(80f, 80f, Shader.TileMode.MIRROR)

    val alphaMatrix = android.graphics.RenderEffect.createColorFilterEffect(
        ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, 0f,
                    0f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, 50f, -5000f
                )
            )
        )
    )

    return android.graphics.RenderEffect
        .createChainEffect(alphaMatrix, blurEffect)
}

@Composable
fun AnimateScreen() {
    val isMenuExtended = remember { mutableStateOf(false) }

    val fabAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        )
    )

    val clickAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = LinearEasing
        )
    )

    val renderEffect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        getRenderEffect().asComposeRenderEffect()
    } else {
        null
    }

    MainScreenContent(
        renderEffect = renderEffect,
        fabAnimationProgress = fabAnimationProgress,
        clickAnimationProgress = clickAnimationProgress
    ) {
        isMenuExtended.value = isMenuExtended.value.not()
    }
}

@Composable
fun ExtendedFabGroup(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        // Icons in the extended fab group
        listOf(
            Icons.Default.Add,
            Icons.Default.CalendarToday,
            Icons.Default.Group,
            Icons.Default.PhotoCamera,
            Icons.Default.Settings,
            Icons.Default.ShoppingCart
        ).forEachIndexed { index, icon ->
            if (index == 0) {
                AnimatedFab(
                    icon = icon,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = onClick,
                    rotation = 45f
                )
            } else {
                AnimatedFab(
                    icon = icon,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun AnimatedFab(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onClick: () -> Unit = {},
    rotation: Float = 0f
) {
    FloatingActionButton(
        onClick = onClick,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
        backgroundColor = colorResource(id = R.color.og),
        modifier = modifier
            .size(32.dp)
            .rotate(rotation)
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
fun FabGroup(
    animationProgress: Float = 0f,
    renderEffect: androidx.compose.ui.graphics.RenderEffect? = null,
    toggleAnimation: () -> Unit = { }
) {
    Box(
        Modifier
            .padding(bottom = 28.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        // Only one main fab with Add icon
        AnimatedFab(
            icon = Icons.Default.Add,
            modifier = Modifier,
            onClick = toggleAnimation
        )
    }
}

@Composable
fun MainScreenContent(
    renderEffect: androidx.compose.ui.graphics.RenderEffect?,
    fabAnimationProgress: Float = 0f,
    clickAnimationProgress: Float = 0f,
    toggleAnimation: () -> Unit = { }
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        FabGroup(
            renderEffect = renderEffect,
            animationProgress = fabAnimationProgress,
            toggleAnimation = toggleAnimation
        )
        if (clickAnimationProgress > 0) {
            // ExtendedFabGroup with Add icon instead of cross
            ExtendedFabGroup(
                modifier = Modifier.padding(bottom = 70.dp),
                onClick = toggleAnimation
            )
        }
    }
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFF3A2F6E)
private fun MainScreenPreview() {
    ChatterBoxTheme {
        AnimateScreen()
    }
}

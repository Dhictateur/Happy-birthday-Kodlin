package com.example.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.happybirthday.ui.theme.HappyBirthdayTheme
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappyBirthdayTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        myString = "Happy Birthday!",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(myString: String, modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    var speedPx by remember { mutableStateOf(0f) }

    // Límite máximo en dp
    val maxX = 45.dp
    val maxY = 200.dp

    LaunchedEffect(density) {
        speedPx = with(density) { 1.dp.toPx() } // Reducir la velocidad a 1dp por ciclo
    }

    val animatableXOffset = remember { Animatable(0f) }
    val animatableYOffset = remember { Animatable(0f) }

    var currentAngle by remember { mutableStateOf((0..360).random().toDouble() * (PI / 180)) }

    LaunchedEffect(Unit) {
        while (true) {
            val dx = speedPx * cos(currentAngle)
            val dy = speedPx * sin(currentAngle)

            val duration = 16 // Aproximadamente 60 FPS (1000 ms / 60)

            val newX = animatableXOffset.value + dx.toFloat()
            val newY = animatableYOffset.value + dy.toFloat()

            // Verificar si se está fuera de los límites y ajustar el ángulo si es necesario
            if (newX < 0 || newX > with(density) { maxX.toPx() }) {
                currentAngle = PI - currentAngle // Invertir dirección horizontal solo si excede los límites
            }
            if (newY < 0 || newY > with(density) { maxY.toPx() }) {
                currentAngle = -currentAngle // Invertir dirección vertical solo si excede los límites
            }

            // Usar `snapTo` para actualizar inmediatamente y luego animar
            animatableXOffset.snapTo(newX.coerceIn(0f, with(density) { maxX.toPx() }))
            animatableYOffset.snapTo(newY.coerceIn(0f, with(density) { maxY.toPx() }))

            // Esperar antes de iniciar el siguiente movimiento
            delay(duration.toLong())
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = myString,
            modifier = Modifier.offset(
                x = animatableXOffset.value.dp,
                y = animatableYOffset.value.dp,
            ),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun GreetingPreview() {
    HappyBirthdayTheme {
        Greeting(myString = "Happy Birthday!")
    }
}

package org.example.project.habit.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle

import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressWithPercentage(
    percentage: Float,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = borderColor
    ),
    modifier: Modifier = Modifier
) {
    val notMarkedCircleColor = MaterialTheme.colorScheme.onSurface.copy(0.4f)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(64.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sweepAngle = 360 * (percentage / 100)
            drawCircle(
                color = notMarkedCircleColor,
                style = Stroke(width = 6.dp.toPx())
            )
            drawArc(
                color = borderColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx())
            )
        }
        Text(
            text = "${percentage.toInt()}%",
            style = textStyle,
        )
    }
}


package com.eevajonna.period.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eevajonna.period.ui.theme.PeriodTheme
import com.eevajonna.period.ui.utils.TextUtils
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun PeriodCanvas(modifier: Modifier = Modifier, startDate: LocalDate, endDate: LocalDate?) {
    val color = MaterialTheme.colorScheme.surface
    val periodColor = MaterialTheme.colorScheme.tertiaryContainer
    val textColor = MaterialTheme.colorScheme.onBackground

    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(PeriodCanvas.canvasHeight),
    ) {
        val dayWidth = (size.width - (PeriodCanvas.BackgroundOffset.x * 2).toPx()) / 14

        val endDateToCompare = endDate ?: LocalDate.now().plusDays(6)

        val periodWidth = startDate.until(endDateToCompare.plusDays(1), ChronoUnit.DAYS) * dayWidth
        val yOffset = size.height - PeriodCanvas.BackgroundOffset.y.toPx()

        drawPeriod(
            backgroundColor = color,
            periodColor = periodColor,
            yOffset = yOffset,
            dayWidth = dayWidth,
            periodWidth = periodWidth,
            endDateAvailable = endDate != null,
        )

        val startTextLayoutResult = textMeasurer.measure(TextUtils.formatDate(startDate, "MMM d"))

        drawDayIndicator(
            color = textColor,
            offset = Offset(
                x = dayWidth,
                y = yOffset - PeriodCanvas.TextOffset.y.toPx(),
            ),
            textLayoutResult = startTextLayoutResult,
            textOffset = Offset(
                x = 0f,
                y = yOffset - PeriodCanvas.TextOffset.y.toPx() - startTextLayoutResult.size.height,
            ),
        )

        endDate?.let {
            val endTextLayoutResult = textMeasurer.measure(TextUtils.formatDate(endDate, "MMM d"))

            drawDayIndicator(
                color = textColor,
                offset = Offset(
                    x = periodWidth + dayWidth,
                    y = yOffset - PeriodCanvas.TextOffset.y.toPx(),
                ),
                textLayoutResult = endTextLayoutResult,
                textOffset = Offset(
                    x = (periodWidth + dayWidth) - (endTextLayoutResult.size.width / 2),
                    y = yOffset - PeriodCanvas.TextOffset.y.toPx() - endTextLayoutResult.size.height,
                ),
            )
        }
    }
}

fun DrawScope.drawDayIndicator(color: Color, offset: Offset, textLayoutResult: TextLayoutResult, textOffset: Offset) {
    drawRect(
        color,
        size = Size(
            width = PeriodCanvas.TextIndicatorSize.width.toPx(),
            height = PeriodCanvas.TextIndicatorSize.height.toPx(),
        ),
        topLeft = offset,
    )

    drawText(
        textLayoutResult,
        color = color,
        topLeft = textOffset,
    )
}

fun DrawScope.drawPeriod(
    backgroundColor: Color,
    periodColor: Color,
    yOffset: Float,
    dayWidth: Float,
    periodWidth: Float,
    endDateAvailable: Boolean,
) {
    drawRoundRect(
        backgroundColor,
        cornerRadius = CornerRadius(PeriodCanvas.cornerRadius.toPx()),
        topLeft = Offset(
            x = PeriodCanvas.BackgroundOffset.x.toPx(),
            y = yOffset,
        ),
        size = Size(
            width = size.width - PeriodCanvas.Track.horizontalPadding.toPx(),
            height = PeriodCanvas.Track.height.toPx(),
        ),
    )

    val periodSize = Size(
        width = periodWidth,
        height = PeriodCanvas.Track.height.toPx(),
    )

    val periodOffset = Offset(
        x = dayWidth,
        y = yOffset,
    )

    if (endDateAvailable) {
        drawRoundRect(
            periodColor,
            cornerRadius = CornerRadius(PeriodCanvas.cornerRadius.toPx()),
            topLeft = periodOffset,
            size = periodSize,
        )
    } else {
        val colorStops = arrayOf(
            0.0f to periodColor,
            0.3f to backgroundColor,
            1f to backgroundColor,
        )
        drawRoundRect(
            Brush.horizontalGradient(colorStops = colorStops),
            cornerRadius = CornerRadius(PeriodCanvas.cornerRadius.toPx()),
            topLeft = periodOffset,
            size = periodSize,
        )
    }
}

@Preview
@Composable
fun PeriodCanvasPreview() {
    PeriodTheme {
        Column {
            PeriodCanvas(
                Modifier.background(MaterialTheme.colorScheme.background),
                startDate = LocalDate.now().minusDays(3),
                endDate = null,
            )
            PeriodCanvas(
                Modifier.background(MaterialTheme.colorScheme.background),
                startDate = LocalDate.now().minusMonths(1),
                endDate = LocalDate.now().minusDays(20),
            )
        }
    }
}

object PeriodCanvas {
    val canvasHeight = 80.dp
    val cornerRadius = 8.dp

    object BackgroundOffset {
        val x = 4.dp
        val y = 24.dp
    }

    object TextOffset {
        val y = 20.dp
    }

    object TextIndicatorSize {
        val height = 10.dp
        val width = 2.dp
    }

    object Track {
        val height = 12.dp
        val horizontalPadding = 8.dp
    }
}

package com.example.ocr_digital.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Composable
fun documentScannerOutlined(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "document_scanner",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(3.417f, 9.125f)
                verticalLineTo(1.708f)
                horizontalLineToRelative(7.375f)
                verticalLineToRelative(2.667f)
                horizontalLineToRelative(-4.75f)
                verticalLineToRelative(4.75f)
                close()
                moveToRelative(30.541f, 0f)
                verticalLineToRelative(-4.75f)
                horizontalLineToRelative(-4.75f)
                verticalLineTo(1.708f)
                horizontalLineToRelative(7.417f)
                verticalLineToRelative(7.417f)
                close()
                moveTo(3.417f, 38.25f)
                verticalLineToRelative(-7.375f)
                horizontalLineToRelative(2.625f)
                verticalLineToRelative(4.75f)
                horizontalLineToRelative(4.75f)
                verticalLineToRelative(2.625f)
                close()
                moveToRelative(25.791f, 0f)
                verticalLineToRelative(-2.625f)
                horizontalLineToRelative(4.75f)
                verticalLineToRelative(-4.75f)
                horizontalLineToRelative(2.667f)
                verticalLineToRelative(7.375f)
                close()
                moveToRelative(-18.166f, -7.625f)
                horizontalLineToRelative(17.916f)
                verticalLineTo(9.375f)
                horizontalLineTo(11.042f)
                close()
                moveToRelative(0f, 2.625f)
                quadToRelative(-1.042f, 0f, -1.834f, -0.792f)
                quadToRelative(-0.791f, -0.791f, -0.791f, -1.833f)
                verticalLineTo(9.375f)
                quadToRelative(0f, -1.042f, 0.791f, -1.854f)
                quadToRelative(0.792f, -0.813f, 1.834f, -0.813f)
                horizontalLineToRelative(17.916f)
                quadToRelative(1.042f, 0f, 1.854f, 0.813f)
                quadToRelative(0.813f, 0.812f, 0.813f, 1.854f)
                verticalLineToRelative(21.25f)
                quadToRelative(0f, 1.042f, -0.813f, 1.833f)
                quadToRelative(-0.812f, 0.792f, -1.854f, 0.792f)
                close()
                moveToRelative(4.333f, -16.958f)
                horizontalLineToRelative(9.292f)
                verticalLineToRelative(-2.625f)
                horizontalLineToRelative(-9.292f)
                close()
                moveToRelative(0f, 5f)
                horizontalLineToRelative(9.292f)
                verticalLineToRelative(-2.625f)
                horizontalLineToRelative(-9.292f)
                close()
                moveToRelative(0f, 5f)
                horizontalLineToRelative(9.292f)
                verticalLineToRelative(-2.625f)
                horizontalLineToRelative(-9.292f)
                close()
                moveToRelative(-4.333f, 4.333f)
                verticalLineTo(9.375f)
                verticalLineToRelative(21.25f)
                close()
            }
        }.build()
    }
}
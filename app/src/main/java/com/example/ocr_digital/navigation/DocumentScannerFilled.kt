package com.example.ocr_digital.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Composable
fun documentScannerFilled(): ImageVector {
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
                moveTo(4.708f, 9.125f)
                quadToRelative(-0.541f, 0f, -0.916f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.958f)
                verticalLineToRelative(-4.75f)
                quadToRelative(0f, -0.542f, 0.375f, -0.938f)
                quadToRelative(0.375f, -0.396f, 0.916f, -0.396f)
                horizontalLineTo(9.5f)
                quadToRelative(0.542f, 0f, 0.917f, 0.396f)
                reflectiveQuadToRelative(0.375f, 0.938f)
                quadToRelative(0f, 0.583f, -0.375f, 0.958f)
                reflectiveQuadToRelative(-0.917f, 0.375f)
                horizontalLineTo(6.042f)
                verticalLineToRelative(3.417f)
                quadToRelative(0f, 0.583f, -0.375f, 0.958f)
                reflectiveQuadToRelative(-0.959f, 0.375f)
                close()
                moveToRelative(30.584f, 0f)
                quadToRelative(-0.542f, 0f, -0.938f, -0.375f)
                quadToRelative(-0.396f, -0.375f, -0.396f, -0.958f)
                verticalLineTo(4.375f)
                horizontalLineToRelative(-3.416f)
                quadToRelative(-0.584f, 0f, -0.959f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.958f)
                quadToRelative(0f, -0.542f, 0.375f, -0.938f)
                quadToRelative(0.375f, -0.396f, 0.959f, -0.396f)
                horizontalLineToRelative(4.75f)
                quadToRelative(0.541f, 0f, 0.937f, 0.396f)
                reflectiveQuadToRelative(0.396f, 0.938f)
                verticalLineToRelative(4.75f)
                quadToRelative(0f, 0.583f, -0.396f, 0.958f)
                reflectiveQuadToRelative(-0.937f, 0.375f)
                close()
                moveTo(4.708f, 38.25f)
                quadToRelative(-0.541f, 0f, -0.916f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.917f)
                verticalLineToRelative(-4.791f)
                quadToRelative(0f, -0.542f, 0.375f, -0.917f)
                reflectiveQuadToRelative(0.916f, -0.375f)
                quadToRelative(0.584f, 0f, 0.959f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.917f)
                verticalLineToRelative(3.458f)
                horizontalLineTo(9.5f)
                quadToRelative(0.542f, 0f, 0.917f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.958f)
                quadToRelative(0f, 0.542f, -0.375f, 0.917f)
                reflectiveQuadToRelative(-0.917f, 0.375f)
                close()
                moveToRelative(25.834f, 0f)
                quadToRelative(-0.584f, 0f, -0.959f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.917f)
                quadToRelative(0f, -0.583f, 0.375f, -0.958f)
                reflectiveQuadToRelative(0.959f, -0.375f)
                horizontalLineToRelative(3.416f)
                verticalLineToRelative(-3.458f)
                quadToRelative(0f, -0.542f, 0.375f, -0.917f)
                reflectiveQuadToRelative(0.959f, -0.375f)
                quadToRelative(0.541f, 0f, 0.937f, 0.375f)
                reflectiveQuadToRelative(0.396f, 0.917f)
                verticalLineToRelative(4.791f)
                quadToRelative(0f, 0.542f, -0.396f, 0.917f)
                reflectiveQuadToRelative(-0.937f, 0.375f)
                close()
                moveToRelative(-19.5f, -5f)
                quadToRelative(-1.084f, 0f, -1.854f, -0.771f)
                quadToRelative(-0.771f, -0.771f, -0.771f, -1.854f)
                verticalLineTo(9.375f)
                quadToRelative(0f, -1.083f, 0.771f, -1.875f)
                quadToRelative(0.77f, -0.792f, 1.854f, -0.792f)
                horizontalLineToRelative(17.916f)
                quadToRelative(1.084f, 0f, 1.875f, 0.792f)
                quadToRelative(0.792f, 0.792f, 0.792f, 1.875f)
                verticalLineToRelative(21.25f)
                quadToRelative(0f, 1.083f, -0.792f, 1.854f)
                quadToRelative(-0.791f, 0.771f, -1.875f, 0.771f)
                close()
                moveToRelative(5.625f, -16.958f)
                horizontalLineToRelative(6.666f)
                quadToRelative(0.542f, 0f, 0.938f, -0.375f)
                quadToRelative(0.396f, -0.375f, 0.396f, -0.917f)
                reflectiveQuadToRelative(-0.396f, -0.937f)
                quadToRelative(-0.396f, -0.396f, -0.938f, -0.396f)
                horizontalLineToRelative(-6.666f)
                quadToRelative(-0.542f, 0f, -0.917f, 0.396f)
                quadToRelative(-0.375f, 0.395f, -0.375f, 0.937f)
                reflectiveQuadToRelative(0.375f, 0.917f)
                quadToRelative(0.375f, 0.375f, 0.917f, 0.375f)
                close()
                moveToRelative(0f, 5f)
                horizontalLineToRelative(6.666f)
                quadToRelative(0.542f, 0f, 0.938f, -0.375f)
                quadToRelative(0.396f, -0.375f, 0.396f, -0.917f)
                reflectiveQuadToRelative(-0.396f, -0.938f)
                quadToRelative(-0.396f, -0.395f, -0.938f, -0.395f)
                horizontalLineToRelative(-6.666f)
                quadToRelative(-0.542f, 0f, -0.917f, 0.395f)
                quadToRelative(-0.375f, 0.396f, -0.375f, 0.938f)
                quadToRelative(0f, 0.542f, 0.375f, 0.917f)
                reflectiveQuadToRelative(0.917f, 0.375f)
                close()
                moveToRelative(0f, 5f)
                horizontalLineToRelative(6.666f)
                quadToRelative(0.542f, 0f, 0.938f, -0.375f)
                quadToRelative(0.396f, -0.375f, 0.396f, -0.917f)
                reflectiveQuadToRelative(-0.396f, -0.938f)
                quadToRelative(-0.396f, -0.395f, -0.938f, -0.395f)
                horizontalLineToRelative(-6.666f)
                quadToRelative(-0.542f, 0f, -0.917f, 0.395f)
                quadToRelative(-0.375f, 0.396f, -0.375f, 0.938f)
                quadToRelative(0f, 0.542f, 0.375f, 0.917f)
                reflectiveQuadToRelative(0.917f, 0.375f)
                close()
            }
        }.build()
    }
}
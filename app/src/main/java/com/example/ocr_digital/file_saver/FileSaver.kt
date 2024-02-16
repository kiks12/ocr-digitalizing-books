package com.example.ocr_digital.file_saver

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.element.Paragraph
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException

class FileSaver {

    private fun saveTextToDocx(context: Context, text: String, filename: String) : Uri? {
        try {
            val document = XWPFDocument()
            val paragraph = document.createParagraph()

            val run = paragraph.createRun()
            run.setText(text)

            val outputPath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "$filename.docx")
            val outputStream = FileOutputStream(outputPath)
            document.write(outputStream)

            document.close()
            outputStream.close()

            return FileProvider.getUriForFile(context, "${context.packageName}.provider", outputPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun saveTextToPng(context: Context, text: String, filename: String) : Uri? {
        try {
            val shortBondWidthPoints = 8.5 * 72

            val displayMetrics = context.resources.displayMetrics
            val shortBondWidthPixels = (shortBondWidthPoints * displayMetrics.density).toInt()

            val textPaint = TextPaint().apply {
                color = Color.BLACK
                textSize = 24f
            }

            val staticLayout = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, shortBondWidthPixels)
                .build()

            val canvasHeight = staticLayout.height

            val bitmap = Bitmap.createBitmap(shortBondWidthPixels, canvasHeight, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)

            staticLayout.draw(canvas)

            val outputPath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$filename.png")
            val outputStream = FileOutputStream(outputPath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            outputStream.close()

            return FileProvider.getUriForFile(context, "${context.packageName}.provider", outputPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun saveTextToJpeg(context: Context, text: String, filename: String) : Uri? {
        try {
            val shortBondWidthPoints = 8.5 * 72

            val displayMetrics = context.resources.displayMetrics
            val shortBondWidthPixels = (shortBondWidthPoints * displayMetrics.density).toInt()

            val textPaint = TextPaint().apply {
                color = Color.BLACK
                textSize = 24f
            }

            val staticLayout = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, shortBondWidthPixels)
                .build()

            val canvasHeight = staticLayout.height

            val bitmap = Bitmap.createBitmap(shortBondWidthPixels, canvasHeight, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)

            staticLayout.draw(canvas)

            val outputPath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$filename.jpg")
            val outputStream = FileOutputStream(outputPath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            outputStream.close()

            return FileProvider.getUriForFile(context, "${context.packageName}.provider", outputPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun saveTextToPdf(context: Context, text: String, filename: String) : Uri? {
        try {
            val outputPath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "$filename.pdf")
            val outputStream = FileOutputStream(outputPath)

            val writer = PdfWriter(outputStream)
            val pdf = PdfDocument(writer)
            val document = Document(pdf)

            document.add(Paragraph(text))

            document.close()
            outputStream.close()

            return FileProvider.getUriForFile(context, "${context.packageName}.provider", outputPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return  null
    }

    private fun saveTextToTxt(context: Context, text: String, filename: String) : Uri? {
        try {
            val outputPath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "$filename.txt")

            FileWriter(outputPath).use { writer ->
                writer.write(text)
            }

            return outputPath.toUri()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    fun saveTextToFile(context: Context, text: String, filename: String, type: FileType) : Uri? {
        return when (type) {
            FileType.DOCX -> saveTextToDocx(context, text, filename)
            FileType.PDF -> saveTextToPdf(context, text, filename)
            FileType.JPEG -> saveTextToJpeg(context, text, filename)
            FileType.PNG -> saveTextToPng(context, text, filename)
            FileType.TXT -> saveTextToTxt(context, text, filename)
        }
    }
}
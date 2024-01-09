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
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.element.Paragraph
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream
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
            // Define the short bond size in points (1 inch = 72 points)
            val shortBondWidthPoints = 8.5 * 72

            // Convert points to pixels based on device density
            val displayMetrics = context.resources.displayMetrics
            val shortBondWidthPixels = (shortBondWidthPoints * displayMetrics.density).toInt()

            // Create a TextPaint object for styling
            val textPaint = TextPaint().apply {
                color = Color.BLACK
                textSize = 24f
            }

            // Create a StaticLayout with the specified width
            val staticLayout = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, shortBondWidthPixels)
                .build()

            // Calculate the height of the canvas based on the total height of the text
            val canvasHeight = staticLayout.height

            // Create a bitmap with variable height
            val bitmap = Bitmap.createBitmap(shortBondWidthPixels, canvasHeight, Bitmap.Config.ARGB_8888)

            // Create a canvas to draw on the bitmap
            val canvas = Canvas(bitmap)

            // Set background color
            canvas.drawColor(Color.WHITE)

            // Draw the text on the canvas using the StaticLayout
            staticLayout.draw(canvas)

            // Save the bitmap as a PNG file
            val outputPath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$filename.png")
            val outputStream = FileOutputStream(outputPath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            outputStream.close()

            // Return the URI of the created file
            return FileProvider.getUriForFile(context, "${context.packageName}.provider", outputPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun saveTextToJpeg(context: Context, text: String, filename: String) : Uri? {
        try {
            // Define the short bond size in points (1 inch = 72 points)
            val shortBondWidthPoints = 8.5 * 72

            // Convert points to pixels based on device density
            val displayMetrics = context.resources.displayMetrics
            val shortBondWidthPixels = (shortBondWidthPoints * displayMetrics.density).toInt()

            // Create a TextPaint object for styling
            val textPaint = TextPaint().apply {
                color = Color.BLACK
                textSize = 24f
            }

            // Create a StaticLayout with the specified width
            val staticLayout = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, shortBondWidthPixels)
                .build()

            // Calculate the height of the canvas based on the total height of the text
            val canvasHeight = staticLayout.height

            // Create a bitmap with variable height
            val bitmap = Bitmap.createBitmap(shortBondWidthPixels, canvasHeight, Bitmap.Config.ARGB_8888)

            // Create a canvas to draw on the bitmap
            val canvas = Canvas(bitmap)

            // Set background color
            canvas.drawColor(Color.WHITE)

            // Draw the text on the canvas using the StaticLayout
            staticLayout.draw(canvas)

            // Save the bitmap as a PNG file
            val outputPath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$filename.jpg")
            val outputStream = FileOutputStream(outputPath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            outputStream.close()

            // Return the URI of the created file
            return FileProvider.getUriForFile(context, "${context.packageName}.provider", outputPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun saveTextToPdf(context: Context, text: String, filename: String) : Uri? {
        try {
            // Create the output file
            val outputPath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "$filename.pdf")
            val outputStream = FileOutputStream(outputPath)

            // Initialize PdfWriter and Document
            val writer = PdfWriter(outputStream)
            val pdf = PdfDocument(writer)
            val document = Document(pdf)

            // Add a paragraph with the text to the document
            document.add(Paragraph(text))

            // Close the document
            document.close()

            // Close the stream
            outputStream.close()

            return FileProvider.getUriForFile(context, "${context.packageName}.provider", outputPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return  null
    }

    fun saveTextToFile(context: Context, text: String, filename: String, type: FileType) : Uri? {
        return when (type) {
            FileType.DOCX -> saveTextToDocx(context, text, filename)
            FileType.PDF -> saveTextToPdf(context, text, filename)
            FileType.JPEG -> saveTextToJpeg(context, text, filename)
            FileType.PNG -> saveTextToPng(context, text, filename)
        }
    }
}